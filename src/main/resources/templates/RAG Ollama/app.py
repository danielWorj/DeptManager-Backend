import os
import json
from pathlib import Path
from flask import Flask, request, jsonify, send_from_directory
from flask_cors import CORS

# PDF & embeddings
import fitz  # PyMuPDF
import numpy as np
import requests

app = Flask(__name__, static_folder="static")
CORS(app)

DOCS_DIR = Path("docs")
OLLAMA_BASE_URL = os.getenv("OLLAMA_BASE_URL", "http://localhost:11434")
EMBED_MODEL = os.getenv("EMBED_MODEL", "nomic-embed-text")   # modèle d'embedding gratuit
CHAT_MODEL  = os.getenv("CHAT_MODEL",  "mistral")            # LLM gratuit via Ollama
CHUNK_SIZE  = 500   # caractères par chunk
CHUNK_OVERLAP = 80
TOP_K = 4           # nombre de chunks renvoyés

# ── Store en mémoire (pas besoin de vectorstore externe) ──────────────────────
vector_store: list[dict] = []   # [{text, embedding, source, page}]


# ═════════════════════════════════════════════════════════════════════════════
# Utilitaires
# ═════════════════════════════════════════════════════════════════════════════

def extract_text_from_pdf(pdf_path: Path) -> list[dict]:
    """Extrait le texte page par page depuis un PDF."""
    doc = fitz.open(str(pdf_path))
    pages = []
    for i, page in enumerate(doc):
        text = page.get_text().strip()
        if text:
            pages.append({"text": text, "source": pdf_path.name, "page": i + 1})
    return pages


def chunk_text(text: str, size: int = CHUNK_SIZE, overlap: int = CHUNK_OVERLAP) -> list[str]:
    """Découpe un texte en chunks avec chevauchement."""
    chunks, start = [], 0
    while start < len(text):
        end = min(start + size, len(text))
        chunks.append(text[start:end])
        start += size - overlap
    return chunks


def get_embedding(text: str) -> list[float]:
    """Appelle l'API Ollama pour obtenir un embedding."""
    resp = requests.post(
        f"{OLLAMA_BASE_URL}/api/embeddings",
        json={"model": EMBED_MODEL, "prompt": text},
        timeout=60,
    )
    resp.raise_for_status()
    return resp.json()["embedding"]


def cosine_similarity(a: list[float], b: list[float]) -> float:
    va, vb = np.array(a), np.array(b)
    norm = np.linalg.norm(va) * np.linalg.norm(vb)
    return float(np.dot(va, vb) / norm) if norm else 0.0


def retrieve(query_embedding: list[float], top_k: int = TOP_K) -> list[dict]:
    """Retourne les top_k chunks les plus similaires."""
    scored = [
        {**chunk, "score": cosine_similarity(query_embedding, chunk["embedding"])}
        for chunk in vector_store
    ]
    scored.sort(key=lambda x: x["score"], reverse=True)
    return scored[:top_k]


def ask_ollama(prompt: str, context: str) -> str:
    """Envoie le prompt + contexte au LLM Ollama."""
    system = (
        "Tu es un assistant expert. Réponds en te basant UNIQUEMENT sur le contexte fourni. "
        "Si la réponse n'est pas dans le contexte, dis-le clairement. "
        "Sois précis, structuré et concis."
    )
    full_prompt = f"""Contexte extrait des documents :
---
{context}
---

Question : {prompt}

Réponse :"""

    resp = requests.post(
        f"{OLLAMA_BASE_URL}/api/generate",
        json={"model": CHAT_MODEL, "prompt": full_prompt, "system": system, "stream": False},
        timeout=120,
    )
    resp.raise_for_status()
    return resp.json()["response"]


# ═════════════════════════════════════════════════════════════════════════════
# Routes Flask
# ═════════════════════════════════════════════════════════════════════════════

@app.route("/")
def index():
    return send_from_directory("static", "index.html")


@app.route("/api/index", methods=["POST"])
def index_documents():
    """Indexe (ou ré-indexe) tous les PDFs du dossier docs/."""
    global vector_store
    vector_store = []

    DOCS_DIR.mkdir(exist_ok=True)
    pdf_files = list(DOCS_DIR.glob("*.pdf"))
    if not pdf_files:
        return jsonify({"error": "Aucun PDF trouvé dans le dossier docs/"}), 404

    indexed, total_chunks = [], 0
    for pdf_path in pdf_files:
        pages = extract_text_from_pdf(pdf_path)
        for page in pages:
            for chunk in chunk_text(page["text"]):
                if chunk.strip():
                    emb = get_embedding(chunk)
                    vector_store.append({
                        "text": chunk,
                        "embedding": emb,
                        "source": page["source"],
                        "page": page["page"],
                    })
                    total_chunks += 1
        indexed.append(pdf_path.name)

    return jsonify({
        "status": "ok",
        "indexed_files": indexed,
        "total_chunks": total_chunks,
    })


@app.route("/api/query", methods=["POST"])
def query():
    """Répond à une question en utilisant le RAG."""
    body = request.get_json(force=True)
    question = (body.get("question") or "").strip()
    if not question:
        return jsonify({"error": "Champ 'question' manquant"}), 400
    if not vector_store:
        return jsonify({"error": "Aucun document indexé. Lancez d'abord /api/index"}), 400

    query_emb = get_embedding(question)
    top_chunks = retrieve(query_emb)

    context = "\n\n".join(
        f"[{c['source']} – p.{c['page']}]\n{c['text']}" for c in top_chunks
    )
    answer = ask_ollama(question, context)

    sources = [
        {"source": c["source"], "page": c["page"], "score": round(c["score"], 4)}
        for c in top_chunks
    ]
    return jsonify({"answer": answer, "sources": sources})


@app.route("/api/status", methods=["GET"])
def status():
    """Retourne l'état du système."""
    docs = list(DOCS_DIR.glob("*.pdf")) if DOCS_DIR.exists() else []
    ollama_ok = False
    try:
        r = requests.get(f"{OLLAMA_BASE_URL}/api/tags", timeout=5)
        ollama_ok = r.ok
        models = [m["name"] for m in r.json().get("models", [])]
    except Exception:
        models = []

    return jsonify({
        "ollama_connected": ollama_ok,
        "available_models": models,
        "chat_model": CHAT_MODEL,
        "embed_model": EMBED_MODEL,
        "docs_found": [p.name for p in docs],
        "chunks_indexed": len(vector_store),
    })


if __name__ == "__main__":
    app.run(debug=True, port=5000)

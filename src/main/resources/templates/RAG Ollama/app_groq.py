import os
from pathlib import Path
from flask import Flask, request, jsonify, send_from_directory
from flask_cors import CORS

import fitz
import numpy as np
import requests
from sentence_transformers import SentenceTransformer

app = Flask(__name__, static_folder="static")
CORS(app)

# ── Config ────────────────────────────────────────────────────────────────────
GROQ_API_KEY  = os.getenv("GROQ_API_KEY", "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")
GROQ_BASE_URL = "https://api.groq.com/openai/v1"
CHAT_MODEL    = "llama-3.3-70b-versatile"

DOCS_DIR      = Path("docs")
CHUNK_SIZE    = 500
CHUNK_OVERLAP = 80
TOP_K         = 4

# Chargement du modele d'embedding local (telecharge une seule fois ~90Mo)
print("Chargement du modele d'embedding...")
embedder = SentenceTransformer("all-MiniLM-L6-v2")
print("Modele pret.")

vector_store: list[dict] = []

# ─── PDF ─────────────────────────────────────────────────────────────────────

def extract_text_from_pdf(pdf_path: Path) -> list[dict]:
    doc = fitz.open(str(pdf_path))
    pages = []
    for i, page in enumerate(doc):
        text = page.get_text().strip()
        if text:
            pages.append({"text": text, "source": pdf_path.name, "page": i + 1})
    return pages

def chunk_text(text: str) -> list[str]:
    chunks, start = [], 0
    while start < len(text):
        chunks.append(text[start:start + CHUNK_SIZE])
        start += CHUNK_SIZE - CHUNK_OVERLAP
    return chunks

# ─── Embeddings (local, gratuit) ──────────────────────────────────────────────

def get_embedding(text: str) -> list[float]:
    return embedder.encode(text).tolist()

def cosine_similarity(a, b) -> float:
    va, vb = np.array(a), np.array(b)
    norm = np.linalg.norm(va) * np.linalg.norm(vb)
    return float(np.dot(va, vb) / norm) if norm else 0.0

def retrieve(query_emb: list[float]) -> list[dict]:
    scored = [{**c, "score": cosine_similarity(query_emb, c["embedding"])} for c in vector_store]
    scored.sort(key=lambda x: x["score"], reverse=True)
    return scored[:TOP_K]

# ─── LLM via Groq ─────────────────────────────────────────────────────────────

def ask_groq(question: str, context: str) -> str:
    headers = {
        "Authorization": f"Bearer {GROQ_API_KEY}",
        "Content-Type": "application/json",
    }
    messages = [
        {
            "role": "system",
            "content": (
                "Tu es un assistant expert. Reponds en te basant UNIQUEMENT sur le contexte fourni. "
                "Si la reponse n est pas dans le contexte, dis-le clairement. "
                "Sois precis et concis."
            ),
        },
        {
            "role": "user",
            "content": f"Contexte :\n---\n{context}\n---\n\nQuestion : {question}",
        },
    ]
    resp = requests.post(
        f"{GROQ_BASE_URL}/chat/completions",
        headers=headers,
        json={"model": CHAT_MODEL, "messages": messages, "max_tokens": 1024},
        timeout=60,
    )
    resp.raise_for_status()
    return resp.json()["choices"][0]["message"]["content"]

# ─── Routes Flask ─────────────────────────────────────────────────────────────

@app.route("/")
def index():
    return send_from_directory("static", "index.html")

@app.route("/test")
def test_page():
    return send_from_directory("static", "test.html")

@app.route("/api/status")
def status():
    docs = list(DOCS_DIR.glob("*.pdf")) if DOCS_DIR.exists() else []
    groq_ok = False
    try:
        r = requests.get(
            f"{GROQ_BASE_URL}/models",
            headers={"Authorization": f"Bearer {GROQ_API_KEY}"},
            timeout=5,
        )
        groq_ok = r.ok
    except Exception:
        pass
    return jsonify({
        "ollama_connected": groq_ok,
        "chat_model": CHAT_MODEL,
        "embed_model": "all-MiniLM-L6-v2 (local)",
        "docs_found": [p.name for p in docs],
        "chunks_indexed": len(vector_store),
    })

@app.route("/api/index", methods=["POST"])
def index_documents():
    global vector_store
    vector_store = []
    DOCS_DIR.mkdir(exist_ok=True)
    pdf_files = list(DOCS_DIR.glob("*.pdf"))
    if not pdf_files:
        return jsonify({"error": "Aucun PDF trouve dans le dossier docs/"}), 404

    indexed, total_chunks = [], 0
    for pdf_path in pdf_files:
        for page in extract_text_from_pdf(pdf_path):
            for chunk in chunk_text(page["text"]):
                if chunk.strip():
                    vector_store.append({
                        "text": chunk,
                        "embedding": get_embedding(chunk),
                        "source": page["source"],
                        "page": page["page"],
                    })
                    total_chunks += 1
        indexed.append(pdf_path.name)
    return jsonify({"status": "ok", "indexed_files": indexed, "total_chunks": total_chunks})

@app.route("/api/query", methods=["POST"])
def query():
    body = request.get_json(force=True)
    question = (body.get("question") or "").strip()
    if not question:
        return jsonify({"error": "Champ question manquant"}), 400
    if not vector_store:
        return jsonify({"error": "Aucun document indexe. Lancez d abord /api/index"}), 400

    top_chunks = retrieve(get_embedding(question))
    context = "\n\n".join(f"[{c['source']} - p.{c['page']}]\n{c['text']}" for c in top_chunks)
    answer = ask_groq(question, context)
    return jsonify({
        "answer": answer,
        "sources": [{"source": c["source"], "page": c["page"], "score": round(c["score"], 4)} for c in top_chunks],
    })

if __name__ == "__main__":
    app.run(debug=True, port=5000, host="0.0.0.0")
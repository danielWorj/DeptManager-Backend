"""
API Flask - RAG sur documents PDF internes
Stack : LangChain + Chroma + sentence-transformers + Ollama (Mistral)
"""

import os
import logging
from pathlib import Path
from flask import Flask, request, jsonify
from werkzeug.utils import secure_filename

from rag.pipeline import RAGPipeline

# ---------------------------------------------------------------------------
# Configuration
# ---------------------------------------------------------------------------

logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s [%(levelname)s] %(name)s - %(message)s"
)
logger = logging.getLogger(__name__)

app = Flask(__name__)

# Dossiers
DOCS_DIR     = os.getenv("DOCS_DIR",     "./documents")
VECTORDB_DIR = os.getenv("VECTORDB_DIR", "./vectordb")
ALLOWED_EXT  = {"pdf"}

os.makedirs(DOCS_DIR, exist_ok=True)
os.makedirs(VECTORDB_DIR, exist_ok=True)

# Instanciation unique du pipeline RAG (chargé une seule fois au démarrage)
rag = RAGPipeline(docs_dir=DOCS_DIR, vectordb_dir=VECTORDB_DIR)


# ---------------------------------------------------------------------------
# Helpers
# ---------------------------------------------------------------------------

def allowed_file(filename: str) -> bool:
    return "." in filename and filename.rsplit(".", 1)[1].lower() in ALLOWED_EXT


# ---------------------------------------------------------------------------
# Routes
# ---------------------------------------------------------------------------

@app.route("/health", methods=["GET"])
def health():
    """Vérification que l'API est opérationnelle."""
    return jsonify({"status": "ok", "docs_indexed": rag.doc_count()}), 200


@app.route("/ask", methods=["POST"])
def ask():
    """
    Poser une question au RAG.

    Body JSON :
        {
            "question": "Quelle est la politique de congés ?",
            "k": 5          # optionnel, nombre de chunks à récupérer (défaut 5)
        }

    Réponse :
        {
            "answer": "...",
            "sources": ["fichier.pdf - page 3", ...]
        }
    """
    data = request.get_json(silent=True)
    if not data or "question" not in data:
        return jsonify({"error": "Le champ 'question' est requis."}), 400

    question = data["question"].strip()
    if not question:
        return jsonify({"error": "La question ne peut pas être vide."}), 400

    k = int(data.get("k", 5))

    if rag.doc_count() == 0:
        return jsonify({
            "error": "Aucun document indexé. Veuillez d'abord indexer des PDFs via /index."
        }), 409

    try:
        result = rag.query(question, k=k)
        return jsonify(result), 200
    except Exception as e:
        logger.exception("Erreur lors du traitement de la question.")
        return jsonify({"error": str(e)}), 500


@app.route("/index", methods=["POST"])
def index_documents():
    """
    (Re)indexer tous les PDFs présents dans DOCS_DIR.
    Utile après avoir ajouté de nouveaux fichiers manuellement.

    Réponse :
        { "message": "Indexation terminée.", "docs_indexed": 42 }
    """
    try:
        count = rag.index_documents()
        return jsonify({"message": "Indexation terminée.", "docs_indexed": count}), 200
    except Exception as e:
        logger.exception("Erreur lors de l'indexation.")
        return jsonify({"error": str(e)}), 500


@app.route("/upload", methods=["POST"])
def upload():
    """
    Uploader un ou plusieurs PDFs et les indexer automatiquement.

    Form-data : champ 'files' (multipart)

    Réponse :
        { "uploaded": ["doc1.pdf", "doc2.pdf"], "docs_indexed": 10 }
    """
    if "files" not in request.files:
        return jsonify({"error": "Aucun fichier reçu (champ 'files' manquant)."}), 400

    files = request.files.getlist("files")
    uploaded = []

    for f in files:
        if f and allowed_file(f.filename):
            filename = secure_filename(f.filename)
            dest = os.path.join(DOCS_DIR, filename)
            f.save(dest)
            uploaded.append(filename)
            logger.info(f"Fichier sauvegardé : {dest}")

    if not uploaded:
        return jsonify({"error": "Aucun PDF valide reçu."}), 400

    count = rag.index_documents()
    return jsonify({"uploaded": uploaded, "docs_indexed": count}), 200


@app.route("/documents", methods=["GET"])
def list_documents():
    """Lister les PDFs disponibles dans DOCS_DIR."""
    pdfs = [f.name for f in Path(DOCS_DIR).glob("*.pdf")]
    return jsonify({"documents": pdfs, "total": len(pdfs)}), 200


# ---------------------------------------------------------------------------
# Point d'entrée
# ---------------------------------------------------------------------------

if __name__ == "__main__":
    # Indexation initiale au démarrage si des docs existent déjà
    if any(Path(DOCS_DIR).glob("*.pdf")):
        logger.info("Documents détectés — indexation initiale...")
        rag.index_documents()

    app.run(host="0.0.0.0", port=5000, debug=False)

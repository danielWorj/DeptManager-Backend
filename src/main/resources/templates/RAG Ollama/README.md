# RAG Local — Ollama + Flask + PDF

Un système RAG (Retrieval-Augmented Generation) **100 % local** et **gratuit** :
- **Ollama** pour les embeddings et la génération de texte
- **Flask** pour l'API REST
- **PyMuPDF** pour la lecture des PDFs
- **NumPy** pour la similarité cosinus (pas de vectorstore externe)

---

## 🗂 Structure du projet

```
rag-ollama/
├── app.py                # Serveur Flask + logique RAG
├── requirements.txt
├── static/
│   └── index.html        # Interface web intégrée
└── docs/                 # ← Déposez vos PDFs ici
    ├── document1.pdf
    └── document2.pdf
```

---

## ⚙️ Installation

### 1. Installer Ollama
```bash
# Linux / macOS
curl -fsSL https://ollama.com/install.sh | sh

# Windows : télécharger depuis https://ollama.com/download
```

### 2. Télécharger les modèles gratuits
```bash
# Modèle LLM (génération de réponses) — ~4 Go
ollama pull mistral

# Modèle d'embedding (vectorisation) — ~270 Mo
ollama pull nomic-embed-text
```

> **Alternatives LLM légères** : `ollama pull phi3` (3.8B), `ollama pull llama3.2` (3B), `ollama pull gemma2:2b`

### 3. Installer les dépendances Python
```bash
python -m venv .venv
source .venv/bin/activate   # Windows : .venv\Scripts\activate

pip install -r requirements.txt
```

### 4. Ajouter vos PDFs
```bash
mkdir docs
cp /chemin/vers/vos/fichiers/*.pdf docs/
```

---

## 🚀 Lancer l'application

```bash
# S'assurer qu'Ollama tourne
ollama serve   # si pas déjà démarré automatiquement

# Lancer Flask
python app.py
```

Ouvrir **http://localhost:5000** dans votre navigateur.

---

## 📡 API REST

| Méthode | Endpoint       | Description                          |
|---------|----------------|--------------------------------------|
| GET     | `/api/status`  | État Ollama, modèles, chunks indexés |
| POST    | `/api/index`   | Indexe les PDFs du dossier `docs/`   |
| POST    | `/api/query`   | Pose une question (body JSON)        |

### Exemple `/api/query`
```bash
curl -X POST http://localhost:5000/api/query \
  -H "Content-Type: application/json" \
  -d '{"question": "Quels sont les points clés du document ?"}'
```

Réponse :
```json
{
  "answer": "Les points clés sont...",
  "sources": [
    { "source": "document1.pdf", "page": 3, "score": 0.8821 }
  ]
}
```

---

## 🔧 Variables d'environnement

| Variable         | Défaut                    | Description              |
|------------------|---------------------------|--------------------------|
| `OLLAMA_BASE_URL`| `http://localhost:11434`  | URL du serveur Ollama    |
| `CHAT_MODEL`     | `mistral`                 | Modèle LLM               |
| `EMBED_MODEL`    | `nomic-embed-text`        | Modèle d'embedding       |

```bash
CHAT_MODEL=llama3.2 EMBED_MODEL=nomic-embed-text python app.py
```

---

## 🏗 Architecture

```
PDF → Extraction texte (PyMuPDF)
    → Découpage en chunks (500 chars, overlap 80)
    → Embedding via Ollama (nomic-embed-text)
    → Store en mémoire (liste Python)

Question utilisateur
    → Embedding de la question
    → Similarité cosinus avec tous les chunks
    → Top-4 chunks récupérés
    → Contexte + question → Ollama (mistral)
    → Réponse affichée avec sources
```

---

## 💡 Conseils

- Pour de meilleurs résultats, utilisez des PDFs avec du **texte sélectionnable** (pas scannés).
- Augmentez `TOP_K` dans `app.py` pour plus de contexte (au détriment de la vitesse).
- Réduisez `CHUNK_SIZE` si vos documents ont des paragraphes courts.
- L'index est **en mémoire** : relancez `/api/index` après redémarrage du serveur.

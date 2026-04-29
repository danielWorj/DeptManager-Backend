# API Flask — RAG sur documents PDF internes

Chatbot interne basé sur un RAG (Retrieval-Augmented Generation).  
100 % local, open source, aucune donnée envoyée à l'extérieur.

---

## Stack technique

| Rôle | Outil |
|---|---|
| API | Flask |
| Orchestration | LangChain |
| Embeddings | `sentence-transformers` (all-MiniLM-L6-v2) |
| Base vectorielle | Chroma (persistée sur disque) |
| LLM | Ollama + Mistral 7B |

---

## Installation

### 1. Installer Ollama et Mistral

Télécharger Ollama sur https://ollama.ai puis :

```bash
ollama pull mistral
```

### 2. Installer les dépendances Python

```bash
pip install -r requirements.txt
```

### 3. Déposer vos PDFs

```bash
mkdir documents
# Copiez vos PDFs dans le dossier documents/
```

### 4. Lancer l'API

```bash
python app.py
```

L'API démarre sur `http://localhost:5000`.  
Si des PDFs sont déjà présents dans `documents/`, l'indexation se lance automatiquement.

---

## Endpoints

### `GET /health`
Vérifie que l'API fonctionne.

```json
{ "status": "ok", "docs_indexed": 42 }
```

---

### `POST /ask`
Poser une question au RAG.

**Body :**
```json
{
  "question": "Quelle est la politique de congés ?",
  "k": 5
}
```

**Réponse :**
```json
{
  "answer": "Selon le règlement intérieur, les congés annuels sont...",
  "sources": [
    "reglement_interieur.pdf — page 4",
    "contrat_type.pdf — page 2"
  ]
}
```

---

### `POST /upload`
Uploader un ou plusieurs PDFs et les indexer automatiquement.

```bash
curl -X POST http://localhost:5000/upload \
  -F "files=@mon_document.pdf" \
  -F "files=@autre_document.pdf"
```

---

### `POST /index`
Réindexer manuellement tous les PDFs du dossier `documents/`.  
Utile si vous y avez ajouté des fichiers manuellement.

```bash
curl -X POST http://localhost:5000/index
```

---

### `GET /documents`
Lister les PDFs disponibles.

```json
{
  "documents": ["reglement.pdf", "procedure_rh.pdf"],
  "total": 2
}
```

---

## Structure du projet

```
rag_api/
├── app.py              # API Flask — routes
├── rag/
│   ├── __init__.py
│   └── pipeline.py     # Logique RAG (indexation + requête)
├── documents/          # Vos PDFs (à créer)
├── vectordb/           # Base Chroma persistée (auto-créée)
├── requirements.txt
└── .env.example
```

---

## Paramètres ajustables dans `pipeline.py`

| Paramètre | Défaut | Effet |
|---|---|---|
| `chunk_size` | 600 | Taille des chunks en caractères |
| `chunk_overlap` | 80 | Chevauchement entre chunks |
| `k` | 5 | Nombre de chunks envoyés au LLM |
| `temperature` | 0.1 | Créativité du LLM (0 = factuel) |
| `model_name` | `mistral` | Modèle Ollama utilisé |

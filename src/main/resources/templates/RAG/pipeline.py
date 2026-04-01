"""
RAG Pipeline — Retrieval-Augmented Generation
Indexation et interrogation de PDFs locaux.

Composants :
  - Loader       : PyMuPDF  (lecture PDF natif Word→PDF)
  - Splitter     : RecursiveCharacterTextSplitter
  - Embeddings   : HuggingFace sentence-transformers (all-MiniLM-L6-v2)
  - VectorStore  : Chroma  (persisté sur disque)
  - LLM          : Ollama  (Mistral 7B en local)
"""

import logging
from pathlib import Path
from typing import Dict, List, Any

from langchain_community.document_loaders import PyMuPDFLoader, DirectoryLoader
from langchain.text_splitter import RecursiveCharacterTextSplitter
from langchain_huggingface import HuggingFaceEmbeddings
from langchain_chroma import Chroma
from langchain_ollama import OllamaLLM
from langchain.chains import RetrievalQA
from langchain.prompts import PromptTemplate

logger = logging.getLogger(__name__)

# ---------------------------------------------------------------------------
# Prompt système — guide le LLM pour rester factuel
# ---------------------------------------------------------------------------
RAG_PROMPT = PromptTemplate(
    input_variables=["context", "question"],
    template="""Tu es un assistant expert qui répond uniquement à partir des documents fournis.

Règles :
- Réponds en français, de façon claire et structurée.
- Appuie-toi UNIQUEMENT sur le contexte ci-dessous.
- Si la réponse n'est pas dans le contexte, dis-le clairement sans inventer.
- Cite les éléments pertinents du contexte dans ta réponse.

Contexte extrait des documents :
---------------------------------
{context}
---------------------------------

Question : {question}

Réponse :"""
)


class RAGPipeline:
    """
    Pipeline RAG complet : indexation + interrogation.

    Params :
        docs_dir     : dossier contenant les PDFs
        vectordb_dir : dossier de persistance Chroma
        model_name   : modèle Ollama à utiliser
        embed_model  : modèle HuggingFace pour les embeddings
    """

    def __init__(
        self,
        docs_dir: str = "./documents",
        vectordb_dir: str = "./vectordb",
        model_name: str = "mistral",
        embed_model: str = "all-MiniLM-L6-v2",
    ):
        self.docs_dir     = Path(docs_dir)
        self.vectordb_dir = str(vectordb_dir)
        self.model_name   = model_name

        logger.info(f"Chargement du modèle d'embeddings : {embed_model}")
        self.embeddings = HuggingFaceEmbeddings(
            model_name=embed_model,
            model_kwargs={"device": "cpu"},
            encode_kwargs={"normalize_embeddings": True},
        )

        logger.info(f"Connexion au LLM Ollama : {model_name}")
        self.llm = OllamaLLM(
            model=model_name,
            temperature=0.1,       # Réponses factuelles, peu de créativité
            num_predict=1024,      # Longueur max de la réponse
        )

        # Chargement ou création de la base vectorielle
        self.vectorstore = self._load_or_create_vectorstore()
        self._qa_chain   = None  # Initialisé à la première requête


    # -----------------------------------------------------------------------
    # Vectorstore
    # -----------------------------------------------------------------------

    def _load_or_create_vectorstore(self) -> Chroma:
        """Charge Chroma depuis le disque si elle existe, sinon crée une vide."""
        return Chroma(
            persist_directory=self.vectordb_dir,
            embedding_function=self.embeddings,
            collection_name="rag_documents",
        )


    def doc_count(self) -> int:
        """Retourne le nombre de chunks indexés."""
        try:
            return self.vectorstore._collection.count()
        except Exception:
            return 0


    # -----------------------------------------------------------------------
    # Indexation
    # -----------------------------------------------------------------------

    def index_documents(self) -> int:
        """
        Charge tous les PDFs de docs_dir, les découpe et (ré)indexe dans Chroma.
        Retourne le nombre total de chunks indexés.
        """
        pdf_files = list(self.docs_dir.glob("*.pdf"))
        if not pdf_files:
            logger.warning(f"Aucun PDF trouvé dans {self.docs_dir}")
            return 0

        logger.info(f"{len(pdf_files)} PDF(s) trouvé(s) — chargement en cours...")

        # Chargement de tous les PDFs
        all_docs = []
        for pdf_path in pdf_files:
            try:
                loader = PyMuPDFLoader(str(pdf_path))
                docs   = loader.load()
                # Enrichissement des métadonnées
                for doc in docs:
                    doc.metadata["source_file"] = pdf_path.name
                all_docs.extend(docs)
                logger.info(f"  ✓ {pdf_path.name} — {len(docs)} page(s)")
            except Exception as e:
                logger.error(f"  ✗ Erreur sur {pdf_path.name} : {e}")

        if not all_docs:
            return 0

        # Découpage en chunks
        splitter = RecursiveCharacterTextSplitter(
            chunk_size=600,         # Taille optimale pour PDFs Word
            chunk_overlap=80,       # Chevauchement pour ne pas perdre le contexte
            separators=["\n\n", "\n", ".", " ", ""],
            length_function=len,
        )
        chunks = splitter.split_documents(all_docs)
        logger.info(f"Découpage : {len(all_docs)} pages → {len(chunks)} chunks")

        # Réinitialisation + indexation
        self.vectorstore.delete_collection()
        self.vectorstore = Chroma(
            persist_directory=self.vectordb_dir,
            embedding_function=self.embeddings,
            collection_name="rag_documents",
        )
        self.vectorstore.add_documents(chunks)
        self._qa_chain = None   # Reset la chain pour prendre le nouveau store

        count = self.doc_count()
        logger.info(f"Indexation terminée — {count} chunks dans Chroma.")
        return count


    # -----------------------------------------------------------------------
    # Interrogation
    # -----------------------------------------------------------------------

    def _get_qa_chain(self, k: int) -> RetrievalQA:
        """Construit (ou recrée) la chain RetrievalQA avec le retriever configuré."""
        retriever = self.vectorstore.as_retriever(
            search_type="mmr",              # Max Marginal Relevance : diversité + pertinence
            search_kwargs={
                "k": k,                     # Chunks retournés au LLM
                "fetch_k": k * 3,           # Candidats examinés avant sélection MMR
                "lambda_mult": 0.7,         # Équilibre pertinence/diversité (0=diversité, 1=pertinence)
            },
        )
        return RetrievalQA.from_chain_type(
            llm=self.llm,
            chain_type="stuff",             # Injecte tous les chunks dans un seul prompt
            retriever=retriever,
            return_source_documents=True,
            chain_type_kwargs={"prompt": RAG_PROMPT},
        )


    def query(self, question: str, k: int = 5) -> Dict[str, Any]:
        """
        Interroge le RAG et retourne la réponse + les sources.

        Params :
            question : question en langage naturel
            k        : nombre de chunks à récupérer

        Retourne :
            {
                "answer": "...",
                "sources": ["fichier.pdf - page 3", ...]
            }
        """
        logger.info(f"Question reçue (k={k}) : {question}")

        chain  = self._get_qa_chain(k=k)
        result = chain.invoke({"query": question})

        answer = result.get("result", "").strip()

        # Déduplication et formatage des sources
        sources = []
        seen    = set()
        for doc in result.get("source_documents", []):
            meta = doc.metadata
            ref  = f"{meta.get('source_file', 'inconnu')} — page {meta.get('page', '?') + 1}"
            if ref not in seen:
                seen.add(ref)
                sources.append(ref)

        logger.info(f"Réponse générée ({len(sources)} source(s) citée(s))")
        return {"answer": answer, "sources": sources}

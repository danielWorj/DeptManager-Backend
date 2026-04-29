package com.example.DeptManager.UTILS;

import com.example.DeptManager.ENTITY.Evaluation.Note;
import com.example.DeptManager.ENTITY.Scolarite.Repartition;
import com.example.DeptManager.ENTITY.Utilisateur.Etudiant;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.*;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class NotesPdfGenerator {

    // ── Couleurs ──────────────────────────────────────────────────────────
    private static final DeviceRgb COLOR_TABLE_HEADER = new DeviceRgb(220, 220, 220);
    private static final DeviceRgb COLOR_ROW_ALT      = new DeviceRgb(245, 245, 245);

    // ── Logos (classpath) ─────────────────────────────────────────────────
    private static final String LOGO_UDA_PATH   = "static/images/udo.png";
    private static final String LOGO_ENSET_PATH = "static/images/enset.png";

    // ── Dossier de destination ────────────────────────────────────────────
    // Accessible via : http://localhost:8080/notes/<fichier>.pdf
    private static final String NOTES_FOLDER = "src/main/resources/static/notes";


    public String genererFicheNotes(List<Note> notes,
                                    Repartition repartition) throws IOException {

        // ── Nom du fichier ────────────────────────────────────────────────
        String nomFichier = buildFileName(repartition);
        String cheminTemp = "./" + nomFichier;

        // ── Génération iText ──────────────────────────────────────────────
        PdfWriter   writer   = new PdfWriter(cheminTemp);
        PdfDocument pdfDoc   = new PdfDocument(writer);
        Document    document = new Document(pdfDoc, PageSize.A4);
        document.setMargins(15, 35, 20, 35);

        PdfFont fontRegular = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont fontBold    = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        // 1 — Titre matière
        String titreMatiere = repartition.getMatiere() != null
                ? repartition.getMatiere().getIntitule() : "—";
        document.add(new Paragraph(titreMatiere)
                .setFont(fontRegular).setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(4));

        // 2 — En-tête institutionnel
        document.add(buildHeader(fontRegular, fontBold));

        // 3 — Département / Option / Niveau / Type évaluation
        document.add(buildDepartementBlock(repartition, fontBold));

        // 4 — Tableau des notes (4 colonnes : N° | Matricule | Nom | Note)
        document.add(buildNotesTable(notes, fontRegular, fontBold));

        // 5 — Zone de signature
        document.add(buildSignatureZone(repartition, fontRegular, fontBold));

        document.close();
        System.out.println("[NotesPDF] Génération OK → " + nomFichier);

        // ── Déplacement vers static/notes/ ───────────────────────────────
        copierVersDossierNotes(nomFichier);
        supprimerFichierTemp(cheminTemp);

        return nomFichier;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // GESTION FICHIERS (pattern GeneratePDF / PlanningPdfGenerator)
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Copie le fichier temp vers src/main/resources/static/notes/.
     * Crée le dossier s'il n'existe pas. Écrase si déjà existant.
     */
    private static void copierVersDossierNotes(String nomFichier) {
        Path source      = Paths.get("./" + nomFichier);
        Path dossier     = Paths.get(NOTES_FOLDER);
        Path destination = Paths.get(NOTES_FOLDER + "/" + nomFichier);
        try {
            if (!Files.isDirectory(dossier)) {
                Files.createDirectories(dossier);
                System.out.println("[NotesPDF] Dossier créé : " + dossier.toAbsolutePath());
            }
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("[NotesPDF] PDF sauvegardé : " + destination.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("[NotesPDF] Erreur copie : " + e.getMessage());
        }
    }

    /** Supprime le fichier temporaire à la racine du projet. */
    private static void supprimerFichierTemp(String cheminTemp) {
        Path source = Paths.get(cheminTemp);
        try {
            if (Files.exists(source)) {
                Files.delete(source);
                System.out.println("[NotesPDF] Temp supprimé : " + cheminTemp);
            }
        } catch (IOException e) {
            System.err.println("[NotesPDF] Erreur suppression temp : " + e.getMessage());
        }
    }

    /**
     * Construit un nom de fichier unique à partir de la répartition.
     * Ex : "notes_Algorithmique_GIL2_S1.pdf"
     */
    private static String buildFileName(Repartition rep) {
        String matiere  = (rep.getMatiere()  != null) ? rep.getMatiere().getIntitule()  : "inconnu";
        String filiere  = (rep.getFiliere()  != null) ? rep.getFiliere().getIntitule()  : "";
        String niveau   = (rep.getNiveau()   != null) ? rep.getNiveau().getIntitule()   : "";
        String semestre = (rep.getSemestre() != null) ? rep.getSemestre().getIntitule() : "";
        return "notes_" + sanitize(matiere)
                + "_" + sanitize(filiere)
                + "_" + sanitize(niveau)
                + "_" + sanitize(semestre)
                + ".pdf";
    }

    private static String sanitize(String input) {
        if (input == null || input.isBlank()) return "";
        return input.trim().replaceAll("\\s+", "_").replaceAll("[^a-zA-Z0-9_\\-]", "");
    }

    // ═══════════════════════════════════════════════════════════════════════
    // BLOC 1 — En-tête institutionnel ENSET (bilingue)
    // ═══════════════════════════════════════════════════════════════════════
    private Table buildHeader(PdfFont fontRegular, PdfFont fontBold) {

        Table header = new Table(UnitValue.createPercentArray(new float[]{18f, 64f, 18f}))
                .setWidth(UnitValue.createPercentValue(100))
                .setBorder(Border.NO_BORDER)
                .setMarginBottom(6);

        // Logo gauche (UDA)
        Cell cellLogoLeft = new Cell().setBorder(Border.NO_BORDER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setHorizontalAlignment(HorizontalAlignment.CENTER);
        try {
            ImageData imgUda = ImageDataFactory.create(
                    new ClassPathResource(LOGO_UDA_PATH).getURL());
            cellLogoLeft.add(new Image(imgUda).setWidth(55).setAutoScale(false));
        } catch (Exception ignored) {
            cellLogoLeft.add(new Paragraph(""));
        }
        header.addCell(cellLogoLeft);

        // Texte central bilingue
        Cell cellCenter = new Cell().setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.CENTER);

        Table bilingue = new Table(UnitValue.createPercentArray(new float[]{50f, 50f}))
                .setWidth(UnitValue.createPercentValue(100))
                .setBorder(Border.NO_BORDER);
        bilingue.addCell(bilingualCell(
                "REPUBLIQUE DU CAMEROUN\n********\nMinistère de l'Enseignement Supérieur\n********\nPaix-Travail-Patrie",
                fontRegular, 6.5f, TextAlignment.LEFT));
        bilingue.addCell(bilingualCell(
                "REPUBLIC OF CAMEROON\n********\nMinistry Of Higher Education\n********\nPeace-Work-Fatherland",
                fontRegular, 6.5f, TextAlignment.RIGHT));
        cellCenter.add(bilingue);

        cellCenter.add(new Paragraph("****************************")
                .setFont(fontRegular).setFontSize(5)
                .setTextAlignment(TextAlignment.CENTER).setMargin(1));
        cellCenter.add(new Paragraph("UNIVERSITE DE DOUALA")
                .setFont(fontBold).setFontSize(9)
                .setTextAlignment(TextAlignment.CENTER).setMarginBottom(0));
        cellCenter.add(new Paragraph("****************************")
                .setFont(fontRegular).setFontSize(5)
                .setTextAlignment(TextAlignment.CENTER).setMargin(1));
        cellCenter.add(new Paragraph("ECOLE NORMALE SUPERIEURE D'ENSEIGNEMENT TECHNIQUE")
                .setFont(fontBold).setFontSize(7.5f)
                .setTextAlignment(TextAlignment.CENTER).setMarginBottom(1));
        cellCenter.add(new Paragraph("B.P. : 1872-Douala-Cameroun")
                .setFont(fontRegular).setFontSize(6.5f)
                .setTextAlignment(TextAlignment.CENTER).setMarginBottom(0));
        cellCenter.add(new Paragraph("Tél. : (237) 33 01 44 00 / Fax : (237) 33 01 44 03  Email : cabenset@yahoo.fr")
                .setFont(fontRegular).setFontSize(6f)
                .setTextAlignment(TextAlignment.CENTER));
        header.addCell(cellCenter);

        // Logo droit (ENSET)
        Cell cellLogoRight = new Cell().setBorder(Border.NO_BORDER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setHorizontalAlignment(HorizontalAlignment.CENTER);
        try {
            ImageData imgEnset = ImageDataFactory.create(
                    new ClassPathResource(LOGO_ENSET_PATH).getURL());
            cellLogoRight.add(new Image(imgEnset).setWidth(55).setAutoScale(false));
        } catch (Exception ignored) {
            cellLogoRight.add(new Paragraph(""));
        }
        header.addCell(cellLogoRight);

        return header;
    }

    /** Cellule bilingue sans bordure */
    private Cell bilingualCell(String text, PdfFont font, float size, TextAlignment align) {
        return new Cell().setBorder(Border.NO_BORDER)
                .add(new Paragraph(text).setFont(font).setFontSize(size)
                        .setTextAlignment(align).setMultipliedLeading(1.2f));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // BLOC 2 — Département / Option (abréviation) / Niveau / Type évaluation
    // ═══════════════════════════════════════════════════════════════════════
    private Div buildDepartementBlock(Repartition rep, PdfFont fontBold) {
        Div div = new Div().setMarginTop(6).setMarginBottom(8);

        div.add(new Paragraph(" ")
                .setBorderBottom(new SolidBorder(ColorConstants.BLACK, 1f))
                .setMarginBottom(4));

        String departement = rep.getFiliere() != null
                ? "DEPARTEMENT DE " + rep.getFiliere().getIntitule().toUpperCase()
                : "DEPARTEMENT";
        div.add(new Paragraph(departement)
                .setFont(fontBold).setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER).setMarginBottom(2));

        // OPTION = abréviation de la filière | NIVEAU
        String option = rep.getFiliere() != null ? rep.getFiliere().getAbreviation() : "—";
        String niveau = rep.getNiveau()  != null ? rep.getNiveau().getIntitule()     : "—";

        Table optNiv = new Table(UnitValue.createPercentArray(new float[]{50f, 50f}))
                .setWidth(UnitValue.createPercentValue(100))
                .setBorder(Border.NO_BORDER);
        optNiv.addCell(new Cell().setBorder(Border.NO_BORDER)
                .add(new Paragraph("OPTION : " + option)
                        .setFont(fontBold).setFontSize(11).setTextAlignment(TextAlignment.CENTER)));
        optNiv.addCell(new Cell().setBorder(Border.NO_BORDER)
                .add(new Paragraph("NIVEAU : " + niveau)
                        .setFont(fontBold).setFontSize(11).setTextAlignment(TextAlignment.CENTER)));
        div.add(optNiv);

        // TYPE D'ÉVALUATION — centré, juste après le niveau
        String typeEval = (rep.getSemestre() != null) ? rep.getSemestre().getIntitule() : "—";
        // Si votre entité Repartition porte directement le typeEvaluation, adaptez ici :
        // String typeEval = rep.getTypeEvaluation() != null ? rep.getTypeEvaluation().getIntitule() : "—";
        div.add(new Paragraph("TYPE D'EVALUATION : " + typeEval)
                .setFont(fontBold).setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(4));

        div.add(new Paragraph(" ")
                .setBorderTop(new SolidBorder(ColorConstants.BLACK, 1f))
                .setMarginTop(4));

        return div;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // BLOC 3 — Tableau des notes (4 colonnes : N° | Matricule | Nom | Note)
    // ═══════════════════════════════════════════════════════════════════════
    private Table buildNotesTable(List<Note> notes,
                                  PdfFont fontRegular,
                                  PdfFont fontBold) {

        Table table = new Table(UnitValue.createPercentArray(new float[]{8f, 20f, 52f, 20f}))
                .setWidth(UnitValue.createPercentValue(100))
                .setBorderCollapse(BorderCollapsePropertyValue.SEPARATE)
                .setMarginBottom(16);

        SolidBorder border = new SolidBorder(ColorConstants.BLACK, 0.5f);

        // En-têtes
        table.addHeaderCell(headerCell("N°",         fontBold, border));
        table.addHeaderCell(headerCell("MATRICULE",  fontBold, border));
        table.addHeaderCell(headerCell("NOM ET PRENOM", fontBold, border));
        table.addHeaderCell(headerCell("NOTE /20",   fontBold, border));

        // Lignes de données
        int index = 1;
        for (Note note : notes) {
            DeviceRgb rowColor = (index % 2 == 0) ? COLOR_ROW_ALT : null;

            String numStr     = String.format("%02d", index);
            String matricule  = (note.getEtudiant() != null && note.getEtudiant().getMatricule() != null)
                    ? note.getEtudiant().getMatricule() : "";
            String nomPrenom  = buildNomPrenom(note.getEtudiant());
            String noteStr    = note.getNote() != null ? String.valueOf(note.getNote()) : "";

            table.addCell(dataCell(numStr,    fontRegular, border, rowColor, TextAlignment.CENTER));
            table.addCell(dataCell(matricule, fontRegular, border, rowColor, TextAlignment.CENTER));
            table.addCell(dataCell(nomPrenom, fontRegular, border, rowColor, TextAlignment.LEFT));
            table.addCell(dataCell(noteStr,   fontBold,    border, rowColor, TextAlignment.CENTER));
            index++;
        }

        // Lignes vides de remplissage jusqu'à 30 lignes minimum
        int minLines = 30;
        while (index <= minLines) {
            table.addCell(dataCell(String.format("%02d", index), fontRegular, border, null, TextAlignment.CENTER));
            table.addCell(dataCell("", fontRegular, border, null, TextAlignment.CENTER));
            table.addCell(dataCell("", fontRegular, border, null, TextAlignment.LEFT));
            table.addCell(dataCell("", fontRegular, border, null, TextAlignment.CENTER));
            index++;
        }

        return table;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // BLOC 4 — Zone de signature
    // ═══════════════════════════════════════════════════════════════════════
    private Table buildSignatureZone(Repartition rep, PdfFont fontRegular, PdfFont fontBold) {
        Table sig = new Table(UnitValue.createPercentArray(new float[]{50f, 50f}))
                .setWidth(UnitValue.createPercentValue(100))
                .setBorder(Border.NO_BORDER)
                .setMarginTop(12);

        String enseignant = rep.getEnseignant() != null
                ? rep.getEnseignant().getNom() + " " + rep.getEnseignant().getPrenom() : "—";
        String matiere = rep.getMatiere() != null
                ? rep.getMatiere().getIntitule() : "—";

        Cell cellLeft = new Cell().setBorder(Border.NO_BORDER);
        cellLeft.add(new Paragraph("Enseignant responsable :")
                .setFont(fontBold).setFontSize(9).setMarginBottom(2));
        cellLeft.add(new Paragraph("Pr. / Dr. " + enseignant)
                .setFont(fontRegular).setFontSize(9).setMarginBottom(2));
        cellLeft.add(new Paragraph("Matière : " + matiere)
                .setFont(fontRegular).setFontSize(9));
        sig.addCell(cellLeft);

        Cell cellRight = new Cell().setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT);
        cellRight.add(new Paragraph("Douala, le ___/___/________")
                .setFont(fontRegular).setFontSize(9).setMarginBottom(18));
        cellRight.add(new Paragraph("Signature & Cachet")
                .setFont(fontBold).setFontSize(9));
        sig.addCell(cellRight);

        return sig;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // HELPERS
    // ═══════════════════════════════════════════════════════════════════════

    private Cell headerCell(String text, PdfFont font, Border border) {
        return new Cell()
                .setBackgroundColor(COLOR_TABLE_HEADER)
                .setBorder(border).setPadding(4)
                .add(new Paragraph(text).setFont(font).setFontSize(8)
                        .setTextAlignment(TextAlignment.CENTER));
    }

    private Cell dataCell(String text, PdfFont font, Border border,
                          DeviceRgb bgColor, TextAlignment align) {
        Cell cell = new Cell().setBorder(border)
                .setPaddingTop(3).setPaddingBottom(3)
                .setPaddingLeft(4).setPaddingRight(4)
                .add(new Paragraph(text == null ? "" : text)
                        .setFont(font).setFontSize(9).setTextAlignment(align));
        if (bgColor != null) cell.setBackgroundColor(bgColor);
        return cell;
    }

    private String buildNomPrenom(Etudiant etudiant) {
        if (etudiant == null) return "";
        String nom    = etudiant.getNom()    != null ? etudiant.getNom().toUpperCase()    : "";
        String prenom = etudiant.getPrenom() != null ? etudiant.getPrenom() : "";
        return (nom + " " + prenom).trim();
    }
}
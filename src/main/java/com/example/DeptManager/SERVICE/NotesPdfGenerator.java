package com.example.DeptManager.SERVICE;
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
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.*;
import com.itextpdf.layout.borders.SolidBorder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Classe pour générer un PDF d'emploi du temps au format officiel
 * Compatible avec iText7
 */
@Service
public class NotesPdfGenerator {

    // ── Couleurs ──────────────────────────────────────────────────────────
    private static final DeviceRgb COLOR_HEADER_TEXT = new DeviceRgb(0, 0, 0);
    private static final DeviceRgb COLOR_TABLE_HEADER = new DeviceRgb(220, 220, 220);
    private static final DeviceRgb COLOR_ROW_ALT      = new DeviceRgb(245, 245, 245);

    // ── Chemins logos (classpath) ─────────────────────────────────────────
    private static final String LOGO_UDA_PATH   = "static/images/logo-uda.png";
    private static final String LOGO_ENSET_PATH = "static/images/logo-enset.png";

    /**
     * Génère le PDF et retourne les bytes.
     *
     * @param notes       liste des notes pour la répartition donnée
     * @param repartition la répartition concernée (matière, filière, niveau, semestre, enseignant)
     * @param typeEvalLabel libellé du type d'évaluation (ex : "CC/20", "Examen/20")
     * @return byte[] contenu du PDF
     */
    public byte[] genererFicheNotes(List<Note> notes,
                                    Repartition repartition,
                                    String typeEvalLabel) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter   writer   = new PdfWriter(baos);
        PdfDocument pdfDoc   = new PdfDocument(writer);
        Document    document = new Document(pdfDoc, PageSize.A4);

        // Marges : haut 15, bas 20, gauche 35, droite 35 (en points)
        document.setMargins(15, 35, 20, 35);

        PdfFont fontRegular = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont fontBold    = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        // ── 1. Titre de la matière (au-dessus de l'en-tête, centré) ──────
        String titreMatiere = repartition.getMatiere() != null
                ? repartition.getMatiere().getIntitule()
                : "—";
        document.add(new Paragraph(titreMatiere)
                .setFont(fontRegular)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(4));

        // ── 2. En-tête institutionnel ─────────────────────────────────────
        document.add(buildHeader(fontRegular, fontBold));

        // ── 3. Titre département / option / niveau ─────────────────────────
        document.add(buildDepartementBlock(repartition, fontBold));

        // ── 4. Tableau des notes ───────────────────────────────────────────
        document.add(buildNotesTable(notes, typeEvalLabel, fontRegular, fontBold));

        // ── 5. Zone de signature ───────────────────────────────────────────
        document.add(buildSignatureZone(repartition, fontRegular, fontBold));

        document.close();
        return baos.toByteArray();
    }

    // ═══════════════════════════════════════════════════════════════════════
    // BLOC 1 — En-tête institutionnel (reproduction fidèle du scan)
    // ═══════════════════════════════════════════════════════════════════════
    private Table buildHeader(PdfFont fontRegular, PdfFont fontBold) throws IOException {

        // Tableau 3 colonnes : logo gauche | texte central | logo droite
        Table header = new Table(UnitValue.createPercentArray(new float[]{18f, 64f, 18f}))
                .setWidth(UnitValue.createPercentValue(100))
                .setBorder(Border.NO_BORDER)
                .setMarginBottom(6);

        // ── Colonne gauche : logo UDA ─────────────────────────────────────
        Cell cellLogoLeft = new Cell()
                .setBorder(Border.NO_BORDER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setHorizontalAlignment(HorizontalAlignment.CENTER);
        try {
            ImageData imgUda = ImageDataFactory.create(
                    new ClassPathResource(LOGO_UDA_PATH).getURL());
            cellLogoLeft.add(new Image(imgUda).setWidth(55).setAutoScale(false));
        } catch (Exception e) {
            // Logo absent → cellule vide (ne bloque pas la génération)
            cellLogoLeft.add(new Paragraph(""));
        }
        header.addCell(cellLogoLeft);

        // ── Colonne centrale : textes bilingues ───────────────────────────
        Cell cellCenter = new Cell()
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.CENTER);

        // Ligne FR / EN
        Table bilingue = new Table(UnitValue.createPercentArray(new float[]{50f, 50f}))
                .setWidth(UnitValue.createPercentValue(100))
                .setBorder(Border.NO_BORDER);

        bilingue.addCell(bilingualCell("REPUBLIQUE DU CAMEROUN\n********\nMinistère de l'Enseignement Supérieur\n********\nPaix-Travail-Patrie", fontRegular, 6.5f, TextAlignment.LEFT));
        bilingue.addCell(bilingualCell("REPUBLIC OF CAMEROON\n********\nMinistry Of Higher Education\n********\nPeace-Work-Fatherland", fontRegular, 6.5f, TextAlignment.RIGHT));

        cellCenter.add(bilingue);

        // Séparateur étoiles
        cellCenter.add(new Paragraph("****************************")
                .setFont(fontRegular).setFontSize(5)
                .setTextAlignment(TextAlignment.CENTER).setMargin(1));

        // Nom université
        cellCenter.add(new Paragraph("UNIVERSITE DE DOUALA")
                .setFont(fontBold).setFontSize(9)
                .setTextAlignment(TextAlignment.CENTER).setMarginBottom(0));

        cellCenter.add(new Paragraph("****************************")
                .setFont(fontRegular).setFontSize(5)
                .setTextAlignment(TextAlignment.CENTER).setMargin(1));

        // Nom école
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

        // ── Colonne droite : logo ENSET ───────────────────────────────────
        Cell cellLogoRight = new Cell()
                .setBorder(Border.NO_BORDER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setHorizontalAlignment(HorizontalAlignment.CENTER);
        try {
            ImageData imgEnset = ImageDataFactory.create(
                    new ClassPathResource(LOGO_ENSET_PATH).getURL());
            cellLogoRight.add(new Image(imgEnset).setWidth(55).setAutoScale(false));
        } catch (Exception e) {
            cellLogoRight.add(new Paragraph(""));
        }
        header.addCell(cellLogoRight);

        return header;
    }

    /** Cellule bilingue sans bordure */
    private Cell bilingualCell(String text, PdfFont font, float size, TextAlignment align) {
        return new Cell()
                .setBorder(Border.NO_BORDER)
                .add(new Paragraph(text)
                        .setFont(font)
                        .setFontSize(size)
                        .setTextAlignment(align)
                        .setMultipliedLeading(1.2f));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // BLOC 2 — Département / Option / Niveau
    // ═══════════════════════════════════════════════════════════════════════
    private Div buildDepartementBlock(Repartition rep, PdfFont fontBold) throws IOException {
        Div div = new Div().setMarginTop(6).setMarginBottom(8);

        // Trait séparateur
        div.add(new Paragraph(" ")
                .setBorderBottom(new SolidBorder(ColorConstants.BLACK, 1f))
                .setMarginBottom(4));

        String departement = rep.getFiliere() != null
                ? "DEPARTEMENT DE " + rep.getFiliere().getIntitule().toUpperCase()
                : "DEPARTEMENT";

        div.add(new Paragraph(departement)
                .setFont(fontBold).setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(2));

        // Option | Niveau sur la même ligne
        String option = rep.getFiliere() != null ? rep.getFiliere().getIntitule() : "—";
        String niveau = rep.getNiveau()  != null ? rep.getNiveau().getIntitule()  : "—";

        Table optNiv = new Table(UnitValue.createPercentArray(new float[]{50f, 50f}))
                .setWidth(UnitValue.createPercentValue(100))
                .setBorder(Border.NO_BORDER);

        optNiv.addCell(new Cell().setBorder(Border.NO_BORDER)
                .add(new Paragraph("OPTION : " + option)
                        .setFont(fontBold).setFontSize(11)
                        .setTextAlignment(TextAlignment.CENTER)));

        optNiv.addCell(new Cell().setBorder(Border.NO_BORDER)
                .add(new Paragraph("NIVEAU : " + niveau)
                        .setFont(fontBold).setFontSize(11)
                        .setTextAlignment(TextAlignment.CENTER)));

        div.add(optNiv);

        // Trait inférieur
        div.add(new Paragraph(" ")
                .setBorderTop(new SolidBorder(ColorConstants.BLACK, 1f))
                .setMarginTop(4));

        return div;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // BLOC 3 — Tableau des notes
    // ═══════════════════════════════════════════════════════════════════════
    private Table buildNotesTable(List<Note> notes,
                                  String typeEvalLabel,
                                  PdfFont fontRegular,
                                  PdfFont fontBold) throws IOException {

        // Colonnes : N° | Noms et Prénoms | Note | (colonnes vides de réserve x2)
        Table table = new Table(UnitValue.createPercentArray(new float[]{8f, 52f, 18f, 11f, 11f}))
                .setWidth(UnitValue.createPercentValue(100))
                .setBorderCollapse(BorderCollapsePropertyValue.SEPARATE)
                .setMarginBottom(16);

        SolidBorder border = new SolidBorder(ColorConstants.BLACK, 0.5f);

        // ── En-tête du tableau ────────────────────────────────────────────
        table.addHeaderCell(headerCell("N°",            fontBold, border));
        table.addHeaderCell(headerCell("NOMS ET PRENOMS", fontBold, border));
        table.addHeaderCell(headerCell(typeEvalLabel,   fontBold, border)); // ex: "CC/20"
        table.addHeaderCell(headerCell("",              fontBold, border));
        table.addHeaderCell(headerCell("",              fontBold, border));

        // ── Lignes de données ─────────────────────────────────────────────
        int index = 1;
        for (Note note : notes) {
            boolean isAlt = (index % 2 == 0);
            DeviceRgb rowColor = isAlt ? COLOR_ROW_ALT : null;

            String numStr    = String.format("%02d", index);
            String nomPrenom = buildNomPrenom(note.getEtudiant());
            String noteStr   = note.getNote() != null ? String.valueOf(note.getNote()) : "";

            table.addCell(dataCell(numStr,    fontRegular, border, rowColor, TextAlignment.CENTER));
            table.addCell(dataCell(nomPrenom, fontRegular, border, rowColor, TextAlignment.LEFT));
            table.addCell(dataCell(noteStr,   fontBold,    border, rowColor, TextAlignment.CENTER));
            table.addCell(dataCell("",        fontRegular, border, rowColor, TextAlignment.CENTER));
            table.addCell(dataCell("",        fontRegular, border, rowColor, TextAlignment.CENTER));

            index++;
        }

        // Lignes vides de remplissage si < 30 étudiants
        int minLines = 30;
        while (index <= minLines) {
            table.addCell(dataCell(String.format("%02d", index), fontRegular, border, null, TextAlignment.CENTER));
            table.addCell(dataCell("", fontRegular, border, null, TextAlignment.LEFT));
            table.addCell(dataCell("", fontRegular, border, null, TextAlignment.CENTER));
            table.addCell(dataCell("", fontRegular, border, null, TextAlignment.CENTER));
            table.addCell(dataCell("", fontRegular, border, null, TextAlignment.CENTER));
            index++;
        }

        return table;
    }

    private Cell headerCell(String text, PdfFont font, Border border) {
        return new Cell()
                .setBackgroundColor(COLOR_TABLE_HEADER)
                .setBorder(border)
                .setPadding(4)
                .add(new Paragraph(text)
                        .setFont(font)
                        .setFontSize(8)
                        .setTextAlignment(TextAlignment.CENTER));
    }

    private Cell dataCell(String text, PdfFont font, Border border,
                          DeviceRgb bgColor, TextAlignment align) {
        Cell cell = new Cell()
                .setBorder(border)
                .setPaddingTop(3).setPaddingBottom(3)
                .setPaddingLeft(4).setPaddingRight(4)
                .add(new Paragraph(text)
                        .setFont(font)
                        .setFontSize(9)
                        .setTextAlignment(align));
        if (bgColor != null) cell.setBackgroundColor(bgColor);
        return cell;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // BLOC 4 — Zone de signature (enseignant + date + cachet)
    // ═══════════════════════════════════════════════════════════════════════
    private Table buildSignatureZone(Repartition rep, PdfFont fontRegular, PdfFont fontBold) throws IOException {
        Table sig = new Table(UnitValue.createPercentArray(new float[]{50f, 50f}))
                .setWidth(UnitValue.createPercentValue(100))
                .setBorder(Border.NO_BORDER)
                .setMarginTop(12);

        // Gauche : enseignant responsable
        String enseignant = rep.getEnseignant() != null
                ? rep.getEnseignant().getNom() + " " + rep.getEnseignant().getPrenom()
                : "—";
        String matiere = rep.getMatiere() != null ? rep.getMatiere().getIntitule() : "—";

        Cell cellLeft = new Cell().setBorder(Border.NO_BORDER);
        cellLeft.add(new Paragraph("Enseignant responsable :")
                .setFont(fontBold).setFontSize(9).setMarginBottom(2));
        cellLeft.add(new Paragraph("Pr. / Dr. " + enseignant)
                .setFont(fontRegular).setFontSize(9).setMarginBottom(2));
        cellLeft.add(new Paragraph("Matière : " + matiere)
                .setFont(fontRegular).setFontSize(9));
        sig.addCell(cellLeft);

        // Droite : date + signature
        Cell cellRight = new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT);
        cellRight.add(new Paragraph("Douala, le ___/___/________")
                .setFont(fontRegular).setFontSize(9).setMarginBottom(18));
        cellRight.add(new Paragraph("Signature & Cachet")
                .setFont(fontBold).setFontSize(9));
        sig.addCell(cellRight);

        return sig;
    }

    // ── Utilitaire : nom + prénom de l'étudiant ────────────────────────────
    private String buildNomPrenom(Etudiant etudiant) {
        if (etudiant == null) return "";
        String nom    = etudiant.getNom()    != null ? etudiant.getNom().toUpperCase()    : "";
        String prenom = etudiant.getPrenom() != null ? etudiant.getPrenom() : "";
        return (nom + " " + prenom).trim();
    }
}

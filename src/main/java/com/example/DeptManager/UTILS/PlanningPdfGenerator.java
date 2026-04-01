package com.example.DeptManager.UTILS;

import com.example.DeptManager.ENTITY.Horaire.Horaire;
import com.example.DeptManager.ENTITY.Scolarite.Repartition;
import com.example.DeptManager.ENTITY.Utilisateur.Enseignant;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.constants.StandardFonts;
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

public class PlanningPdfGenerator {

    // ── Couleurs ──────────────────────────────────────────────────────────
    private static final DeviceRgb COLOR_TABLE_HEADER = new DeviceRgb(200, 200, 200);
    private static final DeviceRgb COLOR_ROW_ALT      = new DeviceRgb(245, 245, 245);
    private static final DeviceRgb COLOR_BLACK        = new DeviceRgb(0, 0, 0);

    // ── Logos (classpath) ─────────────────────────────────────────────────
    private static final String LOGO_UDA_PATH   = "static/images/udo.png";
    private static final String LOGO_ENSET_PATH = "static/images/enset.png";

    // ── Dossier de destination ────────────────────────────────────────────
    // Le PDF sera accessible via : http://localhost:8080/planning/<fichier>.pdf
    private static final String PLANNING_FOLDER = "src/main/resources/static/planning";

    /**
     * Génère le PDF et le sauvegarde dans static/planning/.
     *
     * Pattern identique à GeneratePDF :
     *   1. PdfWriter écrit dans un fichier temp à la racine du projet
     *   2. copierVersDossierPlanning() déplace le fichier dans static/planning/
     *   3. supprimerFichierTemp() nettoie le fichier temporaire
     *
     * @return nom du fichier généré, ex: "planning_GINFO_G2I5.pdf"
     */
    public String genererPlanning(List<Horaire> horaires,
                                  String titrePlanning,
                                  String sousTitre,
                                  String departement,
                                  String classe,
                                  int effectif) throws IOException {

        // ── Nom du fichier ────────────────────────────────────────────────
        String nomFichier = buildFileName(departement, classe);
        String cheminTemp = "./" + nomFichier;

        // ── Génération iText ──────────────────────────────────────────────
        PdfWriter   writer   = new PdfWriter(cheminTemp);
        PdfDocument pdfDoc   = new PdfDocument(writer);
        Document    document = new Document(pdfDoc, PageSize.A4.rotate());
        document.setMargins(15, 25, 20, 25);

        PdfFont fontRegular = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont fontBold    = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        document.add(buildHeader(fontRegular, fontBold));
        document.add(buildTitrePlanning(titrePlanning, sousTitre, fontBold));
        document.add(buildDepartementLigne(departement, classe, effectif, fontBold));
        document.add(buildPlanningTable(horaires, fontRegular, fontBold));
        document.add(buildSignatureZone(fontRegular, fontBold));

        document.close();
        System.out.println("[PlanningPDF] Génération OK → " + nomFichier);

        // ── Déplacement vers static/planning/ ────────────────────────────
        copierVersDossierPlanning(nomFichier);
        supprimerFichierTemp(cheminTemp);

        return nomFichier;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // GESTION FICHIERS (pattern GeneratePDF.java)
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Copie le fichier temp vers src/main/resources/static/planning/.
     * Crée le dossier s'il n'existe pas. Écrase si le fichier existe déjà.
     */
    private static void copierVersDossierPlanning(String nomFichier) {
        Path source      = Paths.get("./" + nomFichier);
        Path dossier     = Paths.get(PLANNING_FOLDER);
        Path destination = Paths.get(PLANNING_FOLDER + "/" + nomFichier);
        try {
            if (!Files.isDirectory(dossier)) {
                Files.createDirectories(dossier);
                System.out.println("[PlanningPDF] Dossier créé : " + dossier.toAbsolutePath());
            }
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("[PlanningPDF] PDF sauvegardé : " + destination.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("[PlanningPDF] Erreur copie : " + e.getMessage());
        }
    }

    /** Supprime le fichier temporaire à la racine du projet. */
    private static void supprimerFichierTemp(String cheminTemp) {
        Path source = Paths.get(cheminTemp);
        try {
            if (Files.exists(source)) {
                Files.delete(source);
                System.out.println("[PlanningPDF] Temp supprimé : " + cheminTemp);
            }
        } catch (IOException e) {
            System.err.println("[PlanningPDF] Erreur suppression temp : " + e.getMessage());
        }
    }

    /** Nom de fichier sans espaces ni caractères spéciaux. */
    private static String buildFileName(String departement, String classe) {
        return "planning_" + sanitize(departement) + "_" + sanitize(classe) + ".pdf";
    }

    private static String sanitize(String input) {
        if (input == null || input.isBlank()) return "inconnu";
        return input.trim().replaceAll("\\s+", "_").replaceAll("[^a-zA-Z0-9_\\-]", "");
    }

    // ═══════════════════════════════════════════════════════════════════════
    // BLOC 1 — En-tête ENSET
    // ═══════════════════════════════════════════════════════════════════════
    private Table buildHeader(PdfFont fontRegular, PdfFont fontBold) {

        Table header = new Table(UnitValue.createPercentArray(new float[]{15f, 70f, 15f}))
                .setWidth(UnitValue.createPercentValue(100))
                .setBorder(Border.NO_BORDER)
                .setMarginBottom(4);

        Cell cellLeft = new Cell().setBorder(Border.NO_BORDER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setHorizontalAlignment(HorizontalAlignment.CENTER);
        try {
            ImageData img = ImageDataFactory.create(new ClassPathResource(LOGO_UDA_PATH).getURL());
            cellLeft.add(new Image(img).setWidth(65).setAutoScale(false));
        } catch (Exception ignored) { cellLeft.add(new Paragraph("")); }
        header.addCell(cellLeft);

        Cell cellCenter = new Cell().setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.CENTER);
        cellCenter.add(new Paragraph("UNIVERSITE DE DOUALA")
                .setFont(fontBold).setFontSize(16).setTextAlignment(TextAlignment.CENTER).setMarginBottom(1));
        cellCenter.add(new Paragraph("****************************")
                .setFont(fontRegular).setFontSize(5).setTextAlignment(TextAlignment.CENTER).setMargin(1));
        cellCenter.add(new Paragraph("Ecole Normale Supérieure")
                .setFont(fontBold).setFontSize(11).setTextAlignment(TextAlignment.CENTER).setMarginBottom(0));
        cellCenter.add(new Paragraph("D'Enseignement Technique")
                .setFont(fontBold).setFontSize(11).setTextAlignment(TextAlignment.CENTER).setMarginBottom(1));
        cellCenter.add(new Paragraph("****************************")
                .setFont(fontRegular).setFontSize(5).setTextAlignment(TextAlignment.CENTER).setMargin(1));
        cellCenter.add(new Paragraph("B.P. 1872 Douala – Cameroun")
                .setFont(fontRegular).setFontSize(7).setTextAlignment(TextAlignment.CENTER).setMarginBottom(1));
        cellCenter.add(new Paragraph("Tél. (Fax) : (237) 243 10 31 35  –  E-mail : cabenset@yahoo.fr")
                .setFont(fontRegular).setFontSize(7).setTextAlignment(TextAlignment.CENTER));
        header.addCell(cellCenter);

        Cell cellRight = new Cell().setBorder(Border.NO_BORDER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setHorizontalAlignment(HorizontalAlignment.CENTER);
        try {
            ImageData img = ImageDataFactory.create(new ClassPathResource(LOGO_ENSET_PATH).getURL());
            cellRight.add(new Image(img).setWidth(65).setAutoScale(false));
        } catch (Exception ignored) { cellRight.add(new Paragraph("")); }
        header.addCell(cellRight);

        return header;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // BLOC 2 — Titre
    // ═══════════════════════════════════════════════════════════════════════
    private Div buildTitrePlanning(String titre, String sousTitre, PdfFont fontBold) {
        Div div = new Div().setMarginTop(8).setMarginBottom(6);
        div.add(new Paragraph(titre)
                .setFont(fontBold).setFontSize(11)
                .setTextAlignment(TextAlignment.CENTER)
                .setBorderBottom(new SolidBorder(COLOR_BLACK, 0.5f))
                .setMarginBottom(2));
        if (sousTitre != null && !sousTitre.isBlank()) {
            div.add(new Paragraph(sousTitre)
                    .setFont(fontBold).setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER));
        }
        return div;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // BLOC 3 — Département / Classe / Effectif
    // ═══════════════════════════════════════════════════════════════════════
    private Table buildDepartementLigne(String dept, String classe, int effectif, PdfFont fontBold) {
        Table t = new Table(UnitValue.createPercentArray(new float[]{33f, 34f, 33f}))
                .setWidth(UnitValue.createPercentValue(100))
                .setBorder(Border.NO_BORDER).setMarginBottom(6);

        t.addCell(new Cell().setBorder(Border.NO_BORDER)
                .add(new Paragraph("DEPARTEMENT : " + dept).setFont(fontBold).setFontSize(9)));
        t.addCell(new Cell().setBorder(Border.NO_BORDER)
                .add(new Paragraph("CLASSE : " + classe).setFont(fontBold).setFontSize(9)
                        .setTextAlignment(TextAlignment.CENTER)));
        t.addCell(new Cell().setBorder(Border.NO_BORDER)
                .add(new Paragraph("EFFECTIF : " + effectif).setFont(fontBold).setFontSize(9)
                        .setTextAlignment(TextAlignment.RIGHT)));
        return t;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // BLOC 4 — Tableau
    // ═══════════════════════════════════════════════════════════════════════
    private Table buildPlanningTable(List<Horaire> horaires, PdfFont fontRegular, PdfFont fontBold) {

        // Colonnes : JOURS | HEURES | UNITE D'ENSEIGNEMENT | CODE | SALLE | ENSEIGNANTS
        Table table = new Table(UnitValue.createPercentArray(new float[]{12f, 14f, 34f, 10f, 12f, 18f}))
                .setWidth(UnitValue.createPercentValue(100)).setMarginBottom(12);

        SolidBorder border = new SolidBorder(COLOR_BLACK, 0.5f);

        for (String h : new String[]{"JOURS", "HEURES", "UNITE D'ENSEIGNEMENT", "CODE", "SALLE", "ENSEIGNANTS"}) {
            table.addHeaderCell(new Cell()
                    .setBackgroundColor(COLOR_TABLE_HEADER).setBorder(border).setPadding(4)
                    .add(new Paragraph(h).setFont(fontBold).setFontSize(8)
                            .setTextAlignment(TextAlignment.CENTER)));
        }

        int idx = 0;
        for (Horaire h : horaires) {
            DeviceRgb bg = (idx % 2 != 0) ? COLOR_ROW_ALT : null;

            String jour = h.getJour() != null ? h.getJour().getIntitule().toUpperCase() : "";

            String heures = "";
            if (h.getPeriode() != null) {
                String d = h.getPeriode().getDebut() != null ? h.getPeriode().getDebut() : "";
                String f = h.getPeriode().getFin()   != null ? h.getPeriode().getFin()   : "";
                if (!d.isEmpty() || !f.isEmpty()) heures = d + " - " + f;
            }

            String matiere = "";
            String code    = "";
            if (h.getRepartition() != null && h.getRepartition().getMatiere() != null) {
                matiere = h.getRepartition().getMatiere().getIntitule().toUpperCase();
                code    = h.getRepartition().getMatiere().getCode() != null
                        ? h.getRepartition().getMatiere().getCode().toUpperCase() : "";
            }

            String salle      = h.getSalle() != null ? h.getSalle().getIntitule().toUpperCase() : "";
            String enseignant = buildEnseignantLabel(h.getRepartition());

            table.addCell(dataCell(jour,       fontRegular, border, bg, TextAlignment.LEFT));
            table.addCell(dataCell(heures,     fontBold,    border, bg, TextAlignment.CENTER));
            table.addCell(dataCell(matiere,    fontRegular, border, bg, TextAlignment.LEFT));
            table.addCell(dataCell(code,       fontRegular, border, bg, TextAlignment.CENTER));
            table.addCell(dataCell(salle,      fontRegular, border, bg, TextAlignment.CENTER));
            table.addCell(dataCell(enseignant, fontRegular, border, bg, TextAlignment.LEFT));
            idx++;
        }

        if (horaires.isEmpty()) {
            for (int i = 0; i < 6; i++)
                table.addCell(dataCell("", fontRegular, border, null, TextAlignment.LEFT));
        }
        return table;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // BLOC 5 — Signature
    // ═══════════════════════════════════════════════════════════════════════
    private Table buildSignatureZone(PdfFont fontRegular, PdfFont fontBold) {
        Table sig = new Table(UnitValue.createPercentArray(new float[]{50f, 50f}))
                .setWidth(UnitValue.createPercentValue(100))
                .setBorder(Border.NO_BORDER).setMarginTop(10);

        Cell cl = new Cell().setBorder(Border.NO_BORDER);
        cl.add(new Paragraph("Le Chef de Département").setFont(fontBold).setFontSize(9).setMarginBottom(30));
        cl.add(new Paragraph("Signature & Cachet").setFont(fontRegular).setFontSize(8));
        sig.addCell(cl);

        Cell cr = new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT);
        cr.add(new Paragraph("Douala, le ___/___/________").setFont(fontRegular).setFontSize(9).setMarginBottom(30));
        cr.add(new Paragraph("Le Directeur").setFont(fontBold).setFontSize(9));
        sig.addCell(cr);

        return sig;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // HELPERS
    // ═══════════════════════════════════════════════════════════════════════

    private Cell dataCell(String text, PdfFont font, Border border,
                          DeviceRgb bgColor, TextAlignment align) {
        Cell cell = new Cell().setBorder(border)
                .setPaddingTop(3).setPaddingBottom(3).setPaddingLeft(4).setPaddingRight(4)
                .add(new Paragraph(text == null ? "" : text)
                        .setFont(font).setFontSize(8).setTextAlignment(align).setMultipliedLeading(1.2f));
        if (bgColor != null) cell.setBackgroundColor(bgColor);
        return cell;
    }

    private String buildEnseignantLabel(Repartition rep) {
        if (rep == null || rep.getEnseignant() == null) return "";
        Enseignant e = rep.getEnseignant();
        String nom    = e.getNom()    != null ? e.getNom().toUpperCase() : "";
        String prenom = e.getPrenom() != null ? e.getPrenom() : "";
        return (nom + " " + prenom).trim();
    }

    private String formatDate(Object date) {
        if (date == null) return "";
        try {
            if (date instanceof java.time.LocalDate ld)
                return String.format("%02d/%02d/%d", ld.getDayOfMonth(), ld.getMonthValue(), ld.getYear());
            if (date instanceof java.time.LocalDateTime ldt)
                return String.format("%02d/%02d/%d", ldt.getDayOfMonth(), ldt.getMonthValue(), ldt.getYear());
            if (date instanceof java.util.Date d)
                return new java.text.SimpleDateFormat("dd/MM/yyyy").format(d);
            return date.toString(); // String brute (ex: "07:30") → retournée telle quelle
        } catch (Exception e) {
            return date.toString();
        }
    }
}
package com.example.DeptManager.SERVICE;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.layout.border.SolidBorder;


import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe pour générer un PDF d'emploi du temps au format officiel
 * Compatible avec iText7
 */
public class GenerateEmploiPDF {

    private List<Horaire> horaires;
    private String filiere;
    private String niveau;
    private String departement;
    private String classe;
    private String reference;
    private String semestre;
    private String vague;

    // Couleurs définies
    private static final Color HEADER_COLOR = new DeviceRgb(105, 108, 255); // #696cff
    private static final Color HEADER_TEXT_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = Color.BLACK;
    private static final Color EVEN_ROW_COLOR = new DeviceRgb(247, 247, 249);

    /**
     * Constructeur principal
     * @param horaires Liste des horaires à afficher
     * @param filiere Nom de la filière
     * @param niveau Nom du niveau
     */
    public GenerateEmploiPDF(List<Horaire> horaires, String filiere, String niveau) {
        this.horaires = horaires != null ? horaires : new ArrayList<>();
        this.filiere = filiere;
        this.niveau = niveau;
        this.departement = "GINFO";
        this.classe = "GI2I5";
        this.reference = "REF 01VSFM25-26/D/DA/CSA/CBSE";
        this.semestre = "1 ER SEMESTRE 2025-2026";
        this.vague = "1 ER VAGUE : SEPTEMBRE - OCTOBRE 2025";
    }

    /**
     * Génère le PDF de l'emploi du temps
     * @param destination Chemin du fichier PDF à créer
     * @throws Exception En cas d'erreur lors de la génération
     */
    public void generatePDF(String destination) throws Exception {
        PdfWriter writer = new PdfWriter(new FileOutputStream(destination));
        PdfDocument pdfDoc = new PdfDocument(writer);

        // Format paysage A4
        pdfDoc.setDefaultPageSize(PageSize.A4.rotate());

        Document document = new Document(pdfDoc);
        document.setMargins(20, 20, 20, 20);

        // Polices
        PdfFont boldFont = PdfFontFactory.createFont("Helvetica-Bold");
        PdfFont regularFont = PdfFontFactory.createFont("Helvetica");

        // En-tête du document
        addHeader(document, boldFont, regularFont);

        // Tableau de l'emploi du temps
        addScheduleTable(document, boldFont, regularFont);

        document.close();
    }

    /**
     * Ajoute l'en-tête du document (logo, titre, référence)
     */
    private void addHeader(Document document, PdfFont boldFont, PdfFont regularFont) {
        // Titre principal
        Paragraph title = new Paragraph("PLANNING PROVISOIRE DES ENSEIGNEMENTS DU " + semestre)
                .setFont(boldFont)
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setMarginBottom(5);
        document.add(title);

        // Sous-titre
        Paragraph subtitle = new Paragraph(vague)
                .setFont(boldFont)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10);
        document.add(subtitle);

        // Informations (Département, Classe)
        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 3}))
                .useAllAvailableWidth()
                .setMarginBottom(10);

        infoTable.addCell(createInfoCell("DÉPARTEMENT", regularFont));
        infoTable.addCell(createInfoCell(departement, boldFont));
        infoTable.addCell(createInfoCell("CLASSE", regularFont));
        infoTable.addCell(createInfoCell(classe, boldFont));

        document.add(infoTable);
    }

    /**
     * Crée une cellule d'information
     */
    private Cell createInfoCell(String text, PdfFont font) {
        return new Cell()
                .add(new Paragraph(text).setFont(font).setFontSize(10))
                .setBorder(Border.NO_BORDER)
                .setPadding(2);
    }

    /**
     * Ajoute le tableau principal de l'emploi du temps
     */
    private void addScheduleTable(Document document, PdfFont boldFont, PdfFont regularFont) {
        // Colonnes: JOURS | HEURES | CODES/EC | UNITÉ D'ENSEIGNEMENTS | CRÉDIT | CM | TP/TD | SALLE | ENSEIGNANTS
        float[] columnWidths = {8, 10, 8, 25, 6, 6, 6, 8, 20};
        Table table = new Table(UnitValue.createPercentArray(columnWidths))
                .useAllAvailableWidth()
                .setMarginBottom(20);

        // En-tête du tableau
        addTableHeader(table, boldFont);

        // Tri des horaires par jour et période
        List<Horaire> sortedHoraires = horaires.stream()
                .sorted(Comparator.comparing((Horaire h) -> h.getJour().getOrdre())
                        .thenComparing(h -> h.getPeriode().getOrdre()))
                .collect(Collectors.toList());

        // Ajout des lignes de données
        int rowIndex = 0;
        for (Horaire horaire : sortedHoraires) {
            addDataRow(table, horaire, regularFont, rowIndex % 2 == 0);
            rowIndex++;
        }

        document.add(table);

        // Pied de page
        addFooter(document, regularFont);
    }

    /**
     * Ajoute l'en-tête du tableau
     */
    private void addTableHeader(Table table, PdfFont boldFont) {
        String[] headers = {
                "JOURS", "HEURES", "CODES/EC", "UNITÉ D'ENSEIGNEMENTS",
                "CRÉDIT", "CM", "TP/TD", "SALLE", "ENSEIGNANTS"
        };

        for (String header : headers) {
            Cell cell = new Cell()
                    .add(new Paragraph(header)
                            .setFont(boldFont)
                            .setFontSize(9))
                    .setBackgroundColor(HEADER_COLOR)
                    .setFontColor(HEADER_TEXT_COLOR)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setPadding(5)
                    .setBorder(new SolidBorder(BORDER_COLOR, 1));
            table.addHeaderCell(cell);
        }
    }

    /**
     * Ajoute une ligne de données dans le tableau
     */
    private void addDataRow(Table table, Horaire horaire, PdfFont regularFont, boolean isEvenRow) {
        Color bgColor = isEvenRow ? EVEN_ROW_COLOR : Color.WHITE;

        // JOUR
        table.addCell(createDataCell(horaire.getJour().getNom().toUpperCase(),
                regularFont, bgColor, true));

        // HEURES
        String heures = formatPeriode(horaire.getPeriode());
        table.addCell(createDataCell(heures, regularFont, bgColor, false));

        // CODE
        table.addCell(createDataCell(horaire.getMatiere().getCode(),
                regularFont, bgColor, false));

        // UNITÉ D'ENSEIGNEMENTS
        table.addCell(createDataCell(horaire.getMatiere().getNom(),
                regularFont, bgColor, false));

        // CRÉDIT (exemple: 2)
        table.addCell(createDataCell("2", regularFont, bgColor, true));

        // CM (exemple: 18)
        table.addCell(createDataCell("18", regularFont, bgColor, true));

        // TP/TD (exemple: 6)
        table.addCell(createDataCell("6", regularFont, bgColor, true));

        // SALLE
        table.addCell(createDataCell(horaire.getSalle().getNom(),
                regularFont, bgColor, false));

        // ENSEIGNANTS
        String enseignant = formatEnseignant(horaire.getEnseignant());
        table.addCell(createDataCell(enseignant, regularFont, bgColor, false));
    }

    /**
     * Crée une cellule de données
     */
    private Cell createDataCell(String text, PdfFont font, Color bgColor, boolean center) {
        Paragraph p = new Paragraph(text != null ? text : "")
                .setFont(font)
                .setFontSize(8);

        Cell cell = new Cell()
                .add(p)
                .setBackgroundColor(bgColor)
                .setPadding(4)
                .setBorder(new SolidBorder(BORDER_COLOR, 0.5f))
                .setVerticalAlignment(VerticalAlignment.MIDDLE);

        if (center) {
            cell.setTextAlignment(TextAlignment.CENTER);
        }

        return cell;
    }

    /**
     * Formate la période (ex: "07H00-08H00")
     */
    private String formatPeriode(Periode periode) {
        return periode.getHeureDebut().replace(":", "H") + "-" +
                periode.getHeureFin().replace(":", "H");
    }

    /**
     * Formate le nom de l'enseignant (ex: "M. DUPONT Jean")
     */
    private String formatEnseignant(Enseignant enseignant) {
        String titre = enseignant.getGenre() != null &&
                enseignant.getGenre().equalsIgnoreCase("F") ? "Mme" : "M.";
        return titre + " " + enseignant.getNom().toUpperCase() + " " +
                capitalizeFirst(enseignant.getPrenom());
    }

    /**
     * Met en majuscule la première lettre
     */
    private String capitalizeFirst(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    /**
     * Ajoute le pied de page (référence, date)
     */
    private void addFooter(Document document, PdfFont regularFont) {
        Paragraph footer = new Paragraph()
                .setFont(regularFont)
                .setFontSize(8)
                .setTextAlignment(TextAlignment.RIGHT)
                .add("Référence: " + reference + "\n")
                .add("Généré le: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        document.add(footer);
    }

    /**
     * Setters pour personnalisation
     */
    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public void setVague(String vague) {
        this.vague = vague;
    }

    /**
     * Exemple d'utilisation
     */
//    public static void main(String[] args) {
//        try {
//            // Création de données de test
//            List<Horaire> horaires = createTestData();
//
//            // Création de l'objet PrintEmploi
//            GenerateEmploiPDF printEmploi = new GenerateEmploiPDF(horaires, "Informatique", "Licence 2");
//
//            // Personnalisation optionnelle
//            printEmploi.setDepartement("GINFO");
//            printEmploi.setClasse("GI2I5");
//
//            // Génération du PDF
//            printEmploi.generatePDF("emploi-du-temps.pdf");
//
//            System.out.println("PDF généré avec succès: emploi-du-temps.pdf");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * Crée des données de test
     */
    private static List<Horaire> createTestData() {
        List<Horaire> horaires = new ArrayList<>();

        // Exemple de données
        Enseignant ens1 = new Enseignant(1, "EPEE TOUBE", "Célestin", "M");
        Filiere filiere = new Filiere(1, "Informatique", "INFO");
        Niveau niveau = new Niveau(1, "Licence 2", 2);
        Matiere matiere1 = new Matiere(1, "TECHNOLOGIE ET CONCURRENCE INTERNATIONALE", "GI500");
        Salle salle1 = new Salle(1, "CPLSE", 40);
        Jour lundi = new Jour(1, "Lundi", 1);
        Periode periode1 = new Periode(1, "10:00", "13:00", 1);

        Horaire h1 = new Horaire(1, ens1, filiere, niveau, matiere1, salle1, lundi, periode1);
        horaires.add(h1);

        // Ajoutez d'autres horaires ici...

        return horaires;
    }
}

// ============================================
// CLASSES MODÈLES (à adapter selon votre code)
// ============================================

class Horaire {
    private int id;
    private Enseignant enseignant;
    private Filiere filiere;
    private Niveau niveau;
    private Matiere matiere;
    private Salle salle;
    private Jour jour;
    private Periode periode;

    public Horaire(int id, Enseignant enseignant, Filiere filiere, Niveau niveau,
                   Matiere matiere, Salle salle, Jour jour, Periode periode) {
        this.id = id;
        this.enseignant = enseignant;
        this.filiere = filiere;
        this.niveau = niveau;
        this.matiere = matiere;
        this.salle = salle;
        this.jour = jour;
        this.periode = periode;
    }

    // Getters
    public Enseignant getEnseignant() { return enseignant; }
    public Filiere getFiliere() { return filiere; }
    public Niveau getNiveau() { return niveau; }
    public Matiere getMatiere() { return matiere; }
    public Salle getSalle() { return salle; }
    public Jour getJour() { return jour; }
    public Periode getPeriode() { return periode; }
}

class Enseignant {
    private int id;
    private String nom;
    private String prenom;
    private String genre;

    public Enseignant(int id, String nom, String prenom, String genre) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.genre = genre;
    }

    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getGenre() { return genre; }
}

class Filiere {
    private int id;
    private String nom;
    private String code;

    public Filiere(int id, String nom, String code) {
        this.id = id;
        this.nom = nom;
        this.code = code;
    }
}

class Niveau {
    private int id;
    private String nom;
    private int ordre;

    public Niveau(int id, String nom, int ordre) {
        this.id = id;
        this.nom = nom;
        this.ordre = ordre;
    }
}

class Matiere {
    private int id;
    private String nom;
    private String code;

    public Matiere(int id, String nom, String code) {
        this.id = id;
        this.nom = nom;
        this.code = code;
    }

    public String getNom() { return nom; }
    public String getCode() { return code; }
}

class Salle {
    private int id;
    private String nom;
    private int capacite;

    public Salle(int id, String nom, int capacite) {
        this.id = id;
        this.nom = nom;
        this.capacite = capacite;
    }

    public String getNom() { return nom; }
}

class Jour {
    private int id;
    private String nom;
    private int ordre;

    public Jour(int id, String nom, int ordre) {
        this.id = id;
        this.nom = nom;
        this.ordre = ordre;
    }

    public String getNom() { return nom; }
    public int getOrdre() { return ordre; }
}

class Periode {
    private int id;
    private String heureDebut;
    private String heureFin;
    private int ordre;

    public Periode(int id, String heureDebut, String heureFin, int ordre) {
        this.id = id;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.ordre = ordre;
    }

    public String getHeureDebut() { return heureDebut; }
    public String getHeureFin() { return heureFin; }
    public int getOrdre() { return ordre; }
}

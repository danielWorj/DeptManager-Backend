package com.example.DeptManager.CONTROLLER.Note;

import com.example.DeptManager.DTO.NoteDTO;
import com.example.DeptManager.ENTITY.Evaluation.Note;
import com.example.DeptManager.ENTITY.Evaluation.TypeEvaluation;
import com.example.DeptManager.ENTITY.Scolarite.Repartition;
import com.example.DeptManager.ENTITY.Server.ServerReponse;
import com.example.DeptManager.ENTITY.Utilisateur.Etudiant;
import com.example.DeptManager.REPOSITORY.Note.NoteRepository;
import com.example.DeptManager.REPOSITORY.Note.TypeEvaluationRepository;
import com.example.DeptManager.REPOSITORY.Scolarite.RepartitionRepository;
import com.example.DeptManager.REPOSITORY.Utilisateur.EtudiantRepository;
import com.example.DeptManager.UTILS.NotesPdfGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Controller
public class NoteControllerImpl implements NoteControllerInt{
    @Autowired
    private RepartitionRepository repartitionRepository;
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private EtudiantRepository etudiantRepository;
    @Autowired
    private TypeEvaluationRepository typeEvaluationRepository;

    @Override
    public ResponseEntity<List<TypeEvaluation>> findAllTypeEvaluation() {
        return ResponseEntity.ok(
                this.typeEvaluationRepository.findAll()
        );
    }

    @Override
    public ResponseEntity<ServerReponse> createNote(String note) {
        NoteDTO noteDTO = new ObjectMapper().readValue(note, NoteDTO.class);

        Note noteDB = new Note();
        noteDB.setEtudiant(this.etudiantRepository.findById(noteDTO.getEtudiant()).orElse(null));
        noteDB.setRepartition(this.repartitionRepository.findById(noteDTO.getRepartition()).orElse(null));
        noteDB.setTypeEvaluation(this.typeEvaluationRepository.findById(noteDTO.getTypeEvaluation()).orElse(null));
        noteDB.setNote(noteDTO.getNote());

        this.noteRepository.save(noteDB);
        return ResponseEntity.ok(new ServerReponse("Note created successfully", true));
    }

    @Override
    public ResponseEntity<ServerReponse> updateNote(String note) {
        NoteDTO noteDTO = new ObjectMapper().readValue(note, NoteDTO.class);
        Note noteUpdating = this.noteRepository.findById(noteDTO.getId()).orElse(null);

        if (Objects.nonNull(noteUpdating)){
            Note noteDB = new Note();

            noteDB.setId(noteUpdating.getId());
            noteDB.setEtudiant(this.etudiantRepository.findById(noteDTO.getEtudiant()).orElse(null));
            noteDB.setRepartition(this.repartitionRepository.findById(noteDTO.getRepartition()).orElse(null));
            noteDB.setTypeEvaluation(this.typeEvaluationRepository.findById(noteDTO.getTypeEvaluation()).orElse(null));
            noteDB.setNote(noteDB.getNote());

            this.noteRepository.save(noteDB);
            return ResponseEntity.ok(new ServerReponse("Note updated successfully", true));

        }else{
            return ResponseEntity.ok(new ServerReponse("Error updating Note", true));
        }

    }

    @Override
    public ResponseEntity<List<Note>> findAllByRepartition(Integer id) {
        return ResponseEntity.ok(
                this.noteRepository.findByRepartition(
                        this.repartitionRepository.findById(id).orElse(null)
                )
        );
    }

    @Override
    public ResponseEntity<List<Note>> findAllByRepartitionAndTypeEvaluation(Integer idR, Integer idE) {
        return ResponseEntity.ok(
                this.noteRepository.findByRepartitionAndTypeEvaluation(
                        this.repartitionRepository.findById(idR).orElse(null),
                        this.typeEvaluationRepository.findById(idE).orElse(null)
                )
        );
    }

    @Override
    public ResponseEntity<?> generateNote(Integer id) throws IOException {

        Repartition repartition = this.repartitionRepository.findById(id).orElse(null);

        if (repartition == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ServerReponse("Répartition introuvable", false));
        }

        List<Note> notes = this.noteRepository.findByRepartition(repartition);

        NotesPdfGenerator notesPdfGenerator = new NotesPdfGenerator();
        String nomFichier = notesPdfGenerator.genererFicheNotes(notes, repartition);

        // Lecture du fichier depuis static/notes/
        Path cheminPdf = Paths.get("src/main/resources/static/notes/" + nomFichier);

        if (!Files.exists(cheminPdf)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ServerReponse("Fichier PDF introuvable après génération", false));
        }

        byte[] contenu = Files.readAllBytes(cheminPdf);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + nomFichier + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(contenu.length)
                .body(contenu);
    }

    @Override
    public ResponseEntity<List<Note>> findAllNoteByFiliereAndNiveau(Integer idF, Integer idN) {
        return ResponseEntity.ok(
                this.noteRepository.findAllNoteByFiliereAndNiveau(
                        idF, idN)
        );
    }

    @Override
    public ResponseEntity<List<Note>> findAllNoteByEtudiantAndSemestre(Integer idE, Integer idS) {
        return ResponseEntity.ok(
                this.noteRepository.findAllNoteByEtudiantAndSemestre(idE, idS)
        );
    }
}

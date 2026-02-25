package com.example.DeptManager.CONTROLLER.Note;

import com.example.DeptManager.ENTITY.Evaluation.Note;
import com.example.DeptManager.ENTITY.Server.ServerReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/deptmanager/api/note/")
@CrossOrigin("*")
public interface NoteControllerInt {

    @PostMapping("/create")
    ResponseEntity<ServerReponse> createNote(@RequestParam("note") String note);
    @PostMapping("/update")
    ResponseEntity<ServerReponse> updateNote(@RequestParam("note") String note);
    @GetMapping("/find/all/byrepartition/{id}")
    ResponseEntity<List<Note>> findAllByRepartition(@PathVariable Integer id);
    @GetMapping("/find/all/byrepartition/typeevaluation/{idR}/{idE}")
    ResponseEntity<List<Note>> findAllByRepartitionAndTypeEvaluation(@PathVariable Integer idR, Integer idE);
}

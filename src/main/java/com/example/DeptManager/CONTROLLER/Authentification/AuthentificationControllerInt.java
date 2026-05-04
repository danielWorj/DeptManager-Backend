package com.example.DeptManager.CONTROLLER.Authentification;

import com.example.DeptManager.DTO.BasicAuthData;
import jakarta.persistence.Entity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/deptmanager/api/auth")
@CrossOrigin("*")
public interface AuthentificationControllerInt {
    @PostMapping("/login")
    ResponseEntity<BasicAuthData> basicLoginSysteme(@RequestParam("auth") String authdata);
}

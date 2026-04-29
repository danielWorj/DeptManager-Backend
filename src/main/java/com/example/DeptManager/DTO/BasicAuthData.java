package com.example.DeptManager.DTO;

import lombok.Data;

@Data
public class BasicAuthData {
    private Integer id ;
    private Integer role ;

    public BasicAuthData(Integer id, Integer role) {
        this.id = id;
        this.role = role;
    }
}

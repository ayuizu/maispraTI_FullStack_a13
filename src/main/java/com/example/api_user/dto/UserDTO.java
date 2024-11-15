package com.example.api_user.dto;
import lombok.Data;

//DTO padrão de design para transferir dados de uma camada para outra
//Abstração do User

@Data
public class UserDTO {
    private int id;
    private String username;
    private String email;
    private String role;
    private String password; //Só enquanto não tem JWT
}

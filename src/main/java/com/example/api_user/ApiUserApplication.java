package com.example.api_user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// @SpringBootApplication(exclude = {SecurityAutoConfiguration.class}) usado para não ter autenticação no Postman
@SpringBootApplication

public class  ApiUserApplication {

	public static void main(String[] args) {

		SpringApplication.run(ApiUserApplication.class, args);
	}

}
/* No MySQL
CREATE DATABASE Test;
USE Test;

CREATE TABLE Users(
	id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	username VARCHAR(30) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(50),
    role VARCHAR(50)
);
 */

/* No Postman POST http://localhost:8080/api/users (body - raw/json)
{
    "username":"Jaques",
    "email": "jaques@teste.com",
    "role": "Desenvolvedor",
    "password": "root"
}
{
    "username":"Ayumi",
    "email": "ayumi@teste.com",
    "role": "Estagiaria",
    "password": "root"
}

Para consultar um id GET http://localhost:8080/api/users/1
Para consultar todos GET http://localhost:8080/api/users/

Para deletar DELETE http://localhost:8080/api/users/1
 */
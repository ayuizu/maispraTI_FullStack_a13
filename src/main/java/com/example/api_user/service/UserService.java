package com.example.api_user.service;

import com.example.api_user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.api_user.dto.UserDTO;
import com.example.api_user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

//Toda logica da aplicação

@Service
// Anotação @Service:
// - Indica que esta classe é um **serviço** Spring, que contém a lógica de negócios.
// - Serviços são componentes do Spring usados para encapsular a lógica central da aplicação e podem ser reutilizados e injetados em outras partes.
// - A anotação `@Service` faz com que esta classe seja registrada automaticamente como um bean gerenciado pelo Spring.

public class UserService {

    @Autowired
    private UserRepository userRepository;

    //Recuperar usuarios
    public List<UserDTO> getAllUsers() {
        return userRepository
                .findAll() //Pega todos os usuarios do banco de dados
                .stream()
                .map(this::convertToDTO) //Converter cada um deles em DTO
                .collect(Collectors.toList()); //Coletar dados e transformar em lista
    }

    //Encontrar User por ID
    public UserDTO getUserById(int id) {
        //Optional: pode ser nulo
        Optional<User> user = userRepository.findById(id);
        return user.map(this::convertToDTO).orElse(null);
    }

    //Encontrar User por username
    public UserDTO getUserByUsername(String username) {
        //Optional: pode ser nulo
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(this::convertToDTO).orElse(null);
    }

    //Converter para DTO
    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    //Criar User
    public UserDTO createUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        user.setPassword(userDTO.getPassword()); //só enquanto não tem JWT

        user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));

        userRepository.save(user);

        return convertToDTO(user);
    }

    //Atualizar Usuario
    public UserDTO updateUser(int id, UserDTO userDTO) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            user.setRole(userDTO.getRole());

            user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));

            userRepository.save(user);

            return convertToDTO(user);
        }
        return null;
    }

    //Deletar usuario
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

}
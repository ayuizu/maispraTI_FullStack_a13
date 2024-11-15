package com.example.api_user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.api_user.service.UserService;
import com.example.api_user.dto.UserDTO;

import java.util.List;

@RestController
// Anotação @RestController:
// - Combina @Controller e @ResponseBody.
// - Indica que esta classe é um controlador no Spring MVC, que lida com requisições HTTP.
// - Todos os métodos retornam objetos diretamente, que serão serializados para JSON ou XML, dependendo do cabeçalho da requisição.
// - Essencialmente, o Spring converte automaticamente o retorno dos métodos em dados JSON ou XML (geralmente JSON em APIs REST).

@RequestMapping("/api/users")
// Anotação @RequestMapping("/api/users"):
// - Define um caminho base para todas as requisições que esta classe vai manipular.
// - Neste caso, o caminho base é "/api/users", o que significa que todas as URLs que acessam este controlador devem começar com "/api/users".
// - Esse caminho pode ser refinado em cada metodo.

public class UserController {
    // Anotação @Autowired:
    // - Spring gerencia a criação e injeção dessa instância de UserService automaticamente.
    // - O @Autowired indica ao Spring que essa variável (userService) será injetada automaticamente, sem necessidade de instanciá-la manualmente.
    // - O UserService aqui é o responsável por implementar a lógica de negócios, e o controlador só repassa as requisições para ele.

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    // Anotação @GetMapping:
    // - Mapeia requisições HTTP GET para este metodo.
    // - O caminho associado a este metodo é o definido por @RequestMapping na classe, ou seja, "/api/users".
    // - A operação GET é usada geralmente para obter dados sem modificá-los.

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id) {
        UserDTO userDTO = userService.getUserById(id);
        return userDTO !=null ? ResponseEntity.ok(userDTO) : ResponseEntity.notFound().build();
    }


    // Anotação @PostMapping:
    // - Mapeia requisições HTTP POST para este metodo.
    // - O POST é utilizado para criar novos recursos, neste caso, um novo usuário.
    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    // Anotação @PutMapping("/{id}"):
    // - Mapeia requisições HTTP PUT para este método.
    // - O PUT é utilizado para atualizar recursos existentes, neste caso, atualizar um usuário pelo ID.

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable int id, @RequestBody UserDTO userDTO) {
        UserDTO updateUser = userService.updateUser(id, userDTO);

        return updateUser !=null ? ResponseEntity.ok(updateUser) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);

        return ResponseEntity.notFound().build();
    }
}

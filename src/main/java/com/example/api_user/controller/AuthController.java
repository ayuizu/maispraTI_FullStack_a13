package com.example.api_user.controller;

import com.example.api_user.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


//AuthController: autenticar usuário e gerar token Jwt
//Chama o JwtService

@RestController
@RequestMapping("/auth")
public class AuthController {
    // O AuthenticationManager é o responsável por gerenciar o processo de autenticação.
    private final AuthenticationManager authenticationManager;
    // O JwtTokenProvider é responsável por gerar tokens JWT para os usuários autenticados.
    private final JwtTokenProvider jwtTokenProvider;
    // UserDetailsService é uma interface do Spring Security que fornece a funcionalidade para carregar detalhes de usuários.
    private final UserDetailsService userDetailsService;


    // Construtor que recebe as dependências como parâmetros. Essas dependências são injetadas pelo Spring.
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    // Anotação @PostMapping("/login"):
    // - Mapeia uma requisição HTTP POST para a URL "/auth/login".
    // - O metodo `login` será chamado quando o cliente enviar uma requisição POST para esta URL.
    public String login(@RequestParam String username, @RequestParam String password) {
        try{
            // O AuthenticationManager realiza a autenticação baseada no nome de usuário e senha.
            // A autenticação é feita criando um UsernamePasswordAuthenticationToken com as credenciais fornecidas (username e password).
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            // Se a autenticação for bem-sucedida, o objeto `authentication` conterá as informações do usuário autenticado.
            // O metodo `getPrincipal()` retorna o objeto principal da autenticação, que no caso é um `UserDetails` (detalhes do usuário autenticado).
            UserDetails user = (UserDetails) authentication.getPrincipal();

            // O JwtTokenProvider gera um token JWT usando as informações do usuário autenticado.
            return jwtTokenProvider.generateToken(user);

        }catch(AuthenticationException error){
            // Se houver uma exceção de autenticação, significa que as credenciais são inválidas.
            // A exceção será capturada e uma RuntimeException será lançada com a mensagem "Invalid Credentials".
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }

}

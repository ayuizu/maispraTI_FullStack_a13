package com.example.api_user.security;

import com.example.api_user.service.CustomUserDetailsService;
import com.example.api_user.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Filtro de autenticação JWT para verificar tokens em cada requisição.
    private final JwtAuthenticationFilter jwtAuthFilter;
    // Serviço personalizado que carrega os detalhes do usuário a partir do banco de dados.
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, CustomUserDetailsService customUserDetailsService) {
        //Parameter 0 of constructor in com.example.api_user.security.SecurityConfig required
        //a bean of type 'com.example.api_user.security.JwtAuthenticationFilter' that could not be found.
        this.jwtAuthFilter = jwtAuthFilter;
        this.customUserDetailsService = customUserDetailsService;
    }

    // Anotação @Bean:
    // - Define que este metodo cria um **bean** gerenciado pelo Spring.
    // - Um **bean** é um objeto criado, configurado e gerenciado pelo Spring, controlando seu ciclo de vida e disponibilizando-o para ser injetado em outras partes da aplicação.
    // - Métodos anotados com @Bean geralmente retornam instâncias de objetos que o Spring deve gerenciar.
    @Bean
    //Gerenciador de autenticação
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //Encriptar senha
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Definir estruturas
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    // Metodo que configura a cadeia de filtros de segurança.
    // O SecurityFilterChain define a sequência de filtros de segurança a serem aplicados em todas as requisições HTTP.
    // Cada requisição HTTP passa por uma cadeia de filtros que determinam se a requisição deve ser autenticada ou autorizada.

    //Indicar o filterChain (a requisicao em si)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // **CSRF (Cross-Site Request Forgery)**:
                .csrf(csrf -> csrf.disable()) //para testar
                .authorizeHttpRequests(auth -> auth
                        // Permite todas as requisições para o caminho "/auth/**" (rota de autenticação).
                        .requestMatchers("/auth/**").permitAll()
                        // Requere autenticação para todas as requisições que começam com "/api/**".
                        // **authenticated()** significa que apenas usuários autenticados poderão acessar as rotas "/api/**".
                        .requestMatchers("/api/**").authenticated()
                        // Qualquer outra requisição precisa estar autenticada.
                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    // Metodo que configura o JwtAuthenticationConverter.
// O JwtAuthenticationConverter é responsável por converter o token JWT em uma autenticação Spring Security.
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        // Converte as claims de um JWT em autoridades (permissões) do Spring Security.
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        // Define o nome da claim onde os papéis (roles) dos usuários são armazenados no JWT.
        // Neste caso, a claim "role" contém os papéis do usuário (por exemplo, ROLE_USER ou ROLE_ADMIN).
        grantedAuthoritiesConverter.setAuthoritiesClaimName("role");

        // Define o prefixo para as autoridades. Por padrão, todas as autoridades terão o prefixo "ROLE_".
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        // Cria um JwtAuthenticationConverter e configura o JwtGrantedAuthoritiesConverter.
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        // Retorna o JwtAuthenticationConverter configurado.
        return jwtAuthenticationConverter;
    }
}
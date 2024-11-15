package com.example.api_user.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.api_user.service.CustomUserDetailsService;

import java.io.IOException;

//Filtra requisições verificando se tem token

// Anotação @Configuration:
// - Indica que esta classe faz parte da configuração do Spring. Isso registra a classe como um bean gerenciado pelo Spring.
@Configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    //private final UserDetails userDetailsService;
    //private final UserService userDetailsService;
    //private final CustomUserDetailsService userDetailsService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, CustomUserDetailsService userDetailsService){
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Extrair cabeçalho
        String authHeader = request.getHeader("Authorization");

        //Verificar se está vazio cabeçaçho de autenticação (primeiro login) ou se NÃO começa com Bearer (portador) (não tinha negação)
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            //Não vai aplicar o filtro. Só dá return e sai da função
            filterChain.doFilter(request,response);
            return;
        }

        //Se tiver cabeçalho, armazena jwt
        String jwt = authHeader.substring(7);
        //Extrai username
        String username = jwtTokenProvider.extractUsername(jwt);

        // Inicializa o objeto UserDetails como null.
        UserDetails userDetails = null;
        //Verificar se username não é nulo e se objeto da autenticação atual é nulo (sem auth configurado) -- sessão expirou, user ainda não autenticou
        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
            // Carrega os detalhes do usuário a partir do nome de usuário extraído do token.
            userDetails = userDetailsService.loadUserByUsername(username);
        }

        //Verifica se o token é válido
        // Se for válido, criamos um UsernamePasswordAuthenticationToken.
        UsernamePasswordAuthenticationToken authenticationToken = null;
        if (jwtTokenProvider.isTokenValid(jwt, userDetails)) {
            authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            // Configura os detalhes da autenticação (IP, informações da requisição, etc.).
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        }

        // Define o objeto de autenticação no SecurityContext do Spring Security.
        // Isso autentica o usuário para o contexto da requisição atual.
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // Continua o processamento da requisição, passando para o próximo filtro na cadeia de filtros.
        filterChain.doFilter(request,response);

    }

}

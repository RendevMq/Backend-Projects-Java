package com.example.configuration.Filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

//Nuestro filtro que validadra si nuestro token es valido

public class JwtTokenValidator extends OncePerRequestFilter {

    private JwtUtils jwtUtils;

    //INYECTAMOS POR CONSTRUCTOR
    public JwtTokenValidator(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (jwtToken != null){
            //un token viene como... Bearer ajkndwanwdawdjnllamkwd , solo nos interesa el token
            jwtToken = jwtToken.substring(7);

            DecodedJWT decodedJWT =  jwtUtils.validateToken(jwtToken); //SI EL TOKEN FUESE INVALIDO NO PASA DE ESTA LINEA


            String username = jwtUtils.extractUsername(decodedJWT);
            String stringAuthorities = jwtUtils.getSpecificClaim(decodedJWT,"authorities").asString();

            //AHROA TENEMOS QUE SETEAR EL usuario y los permisos en el SECURITY CONTEXT HOLDER
            Collection<? extends GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(stringAuthorities);

            SecurityContext context = SecurityContextHolder.getContext(); //extraemos el contexto de springsecurity
            Authentication authentication = new UsernamePasswordAuthenticationToken(username,null,authorities);
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context); //seteamos el contexto, y le damos acceso al usuario
        }

        filterChain.doFilter(request,response);

    }
}


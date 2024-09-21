package com.example.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtUtils {

    @Value("${security.jwt.key.private}")
    private String privateKey;

    @Value("${security.jwt.user.generator}")
    private String userGenerator;

    // Método para crear un token sin roles
    public String createToken(Authentication authentication) {
        Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

        String username = authentication.getPrincipal().toString();

        // Generamos el token sin roles
        return JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1800000))  // Expira en 30 minutos
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algorithm); // Firma el token
    }

    // Método para decodificar el JWT (nuevo)
    public DecodedJWT decodeToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.userGenerator)
                    .build();
            return verifier.verify(token);  // Devuelve el JWT decodificado
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Token inválido");
        }
    }

    // Validación del token y devolver DecodedJWT
    public DecodedJWT validateToken(String token) {
        return decodeToken(token);  // Utilizamos el método de decodificación aquí
    }

    // Extraemos el usuario dentro del token
    public String extractUsername(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject();
    }

    // Método para devolver todos los claims
    public Map<String, Claim> returnAllClaims(DecodedJWT decodedJWT) {
        return decodedJWT.getClaims();
    }

    // Método para obtener la autenticación basada en el JWT
    public UsernamePasswordAuthenticationToken getAuthentication(DecodedJWT decodedJWT) {
        String username = decodedJWT.getSubject();  // Extraemos el subject (email o nombre de usuario)

        // Creamos un UsernamePasswordAuthenticationToken sin roles (empty list of authorities)
        return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
    }
}

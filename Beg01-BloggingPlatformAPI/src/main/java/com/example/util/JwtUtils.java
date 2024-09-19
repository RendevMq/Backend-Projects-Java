package com.example.util;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/*    https://jwt.io/libraries?language=Java
      https://github.com/auth0/java-jwt
*/

@Component
public class JwtUtils {

    @Value("${security.jwt.key.private}")
    private String privateKey;

    //usuario que va agenerar los tokens
    @Value("${security.jwt.user.generator}")
    private String userGenerator;

    //Para extraer al usuario y las autorizaciones
    public String createToken(Authentication authentication) {
        Algorithm algorithm = Algorithm.HMAC256(this.privateKey); //encriptamos

        String username = authentication.getPrincipal().toString(); //obtenemos el usuario autenticado

        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(",")); // POR EJEMPLO "READ,WRITE"

        //Generamos el token, codificamos el token
        String jwtToken = JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(username)
                .withClaim("authorities" , authorities)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1800000))
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algorithm); //Firma
        return  jwtToken;
    }

    //Validacion del token, obtendremos el JWT decodificado
    public DecodedJWT validateToken(String token) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.userGenerator)
                    .build();

            DecodedJWT decodedJWT =  verifier.verify(token);

            return  decodedJWT; //devolvemos el token verificado
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Token invalid, not  gaa");
        }
    }

    // extrameos el usuario dentro del token
    public String extractUsername(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject().toString(); //obtengo el usuario
    }

    //metodo para exytraer el claim
    public Claim getSpecificClaim(DecodedJWT decodedJWT , String claimName){
        return decodedJWT.getClaim(claimName);
    }

    //metodo para devovler todos los claims
    public Map<String, Claim> returnAllClaims(DecodedJWT decodedJWT){
        return decodedJWT.getClaims(); //devuelve un mapa con  el payload
    }
}

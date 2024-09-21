package com.example.persistence.entity.authEntities;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")  // Usamos MongoDB
public class UserEntity {

    @Id
    private String id;  // ID generado por MongoDB

    private String email;  // Email único del usuario

    private String password;  // Contraseña hasheada

    private String name;  // Nombre del usuario

    // Atributos relacionados con la seguridad de la cuenta
    private boolean isEnable;  // Si la cuenta está habilitada
    private boolean accountNoExpired;  // Si la cuenta no ha expirado
    private boolean accountNoLocked;  // Si la cuenta no está bloqueada
    private boolean credentialNoExpired;  // Si las credenciales no han expirado
}

package com.example.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mitag")
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<BlogPostEntity> blogPosts;

    // Getters, setters, constructores
}


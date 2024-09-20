package com.example.presentation.controller;

import com.example.presentation.dto.BlogPostDTO;
import com.example.service.interfaces.IBlogPostService;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class BlogRestController {

    @Autowired
    private IBlogPostService blogPostService;

    // 1. Crear una nueva publicación de blog
    @PostMapping
    public ResponseEntity<BlogPostDTO> createPost(@RequestBody BlogPostDTO blogPostDTO) {
        BlogPostDTO createdPost = blogPostService.save(blogPostDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    // 2. Obtener todas las publicaciones del blog
    // Ademas, para obtener posts por título, contenido, nombre de la categoría o nombres de tags segun el termino
    @GetMapping
    public ResponseEntity<List<BlogPostDTO>> getAllPosts(@RequestParam(value = "term", required = false) String term) {
        List<BlogPostDTO> posts;
        if (term != null && !term.isEmpty()) {
            //posts = blogPostService.searchByTitleOrContent(term);
            posts = blogPostService.searchByTerm(term);
        } else {
            posts = blogPostService.findAll();
        }
        return ResponseEntity.ok(posts);
    }

    // 3. Obtener una sola publicación de blog por ID
    @GetMapping("/{id}")
    public ResponseEntity<BlogPostDTO> getPostById(@PathVariable Long id) {
        Optional<BlogPostDTO> post = blogPostService.findById(id);
        return post.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // 4. Actualizar una publicación de blog existente
    @PutMapping("/{id}")
    public ResponseEntity<BlogPostDTO> updatePost(@PathVariable Long id, @RequestBody BlogPostDTO blogPostDTO) {
        try {
            BlogPostDTO updatedPost = blogPostService.updatePost(id, blogPostDTO);
            return ResponseEntity.ok(updatedPost);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    // 5. Eliminar una publicación de blog existente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        try {
            blogPostService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

}


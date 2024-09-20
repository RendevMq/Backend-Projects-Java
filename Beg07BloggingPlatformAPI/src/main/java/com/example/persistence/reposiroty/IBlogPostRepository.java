package com.example.persistence.reposiroty;

import com.example.persistence.entity.BlogPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IBlogPostRepository extends JpaRepository<BlogPostEntity, Long> {

    // Métodoo para encontrar todos los posts de un usuario
    List<BlogPostEntity> findByUserId(Long userId);

    // Métodoo para buscar posts por categoría
    List<BlogPostEntity> findByCategoryId(Long categoryId);

    // Métodoo para buscar posts por una lista de tags
    List<BlogPostEntity> findByTags_NameIn(List<String> tags);

    // Métodoo para buscar posts por título o contenido (para filtrar)
    List<BlogPostEntity> findByTitleContainingOrContentContaining(String title, String content);

    // MétodoO para obtener posts por título, contenido, nombre de la categoría o nombres de tags
    // "Category_NameContaining"
    //List<BlogPostEntity> findByTitleContainingOrContentContainingOrCategory_NameContainingOrTags_NameIn(String title, String content, String categoryName, List<String> tags);

    // MétodoO para obtener posts por título, contenido, nombre de la categoría o nombres de tags
    // "Category_NameEquals"
    List<BlogPostEntity> findByTitleContainingOrContentContainingOrCategory_NameEqualsOrTags_NameIn(String title, String content, String categoryName, List<String> tags);

}

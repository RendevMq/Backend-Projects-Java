package com.example.service.interfaces;

import com.example.presentation.dto.BlogPostDTO;

import java.util.List;
import java.util.Optional;

public interface  IBlogPostService {
    BlogPostDTO save(BlogPostDTO blogPostDTO);

    List<BlogPostDTO> findAll();

    Optional<BlogPostDTO> findById(Long id);

    BlogPostDTO updatePost(Long postId, BlogPostDTO blogPostDTO);  // Sin User como parámetro

    void deleteById(Long postId);  // Sin User como parámetro

    List<BlogPostDTO> findByCategoryId(Long categoryId);

    List<BlogPostDTO> findByTags(List<String> tags);

    List<BlogPostDTO> searchByTitleOrContent(String term);

    List<BlogPostDTO> findByUserId(Long userId);
}

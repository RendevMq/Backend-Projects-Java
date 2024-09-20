package com.example.service.implementation;

import com.example.persistence.entity.BlogPostEntity;
import com.example.persistence.entity.CategoryEntity;
import com.example.persistence.entity.TagEntity;
import com.example.persistence.entity.authEntities.RoleEnum;
import com.example.persistence.entity.authEntities.UserEntity;
import com.example.persistence.reposiroty.IBlogPostRepository;
import com.example.persistence.reposiroty.ICategoryRepository;
import com.example.persistence.reposiroty.ITagRepository;
import com.example.persistence.reposiroty.authRepositories.UserRepository;
import com.example.presentation.dto.BlogPostDTO;
import com.example.service.interfaces.IBlogPostService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BlogPostServiceImpl implements IBlogPostService {

    @Autowired
    private IBlogPostRepository blogPostRepository;

    @Autowired
    private ICategoryRepository categoryRepository;

    @Autowired
    private ITagRepository tagRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity getAuthenticatedUser() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // MétodoO auxiliar para verificar si el usuario tiene un rol específico
    private boolean hasRole(UserEntity user, RoleEnum roleEnum) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getRoleEnum().equals(roleEnum)); // Comparar con RoleEnum
    }

    @Override
    public BlogPostDTO save(BlogPostDTO blogPostDTO) {
        BlogPostEntity blogPost = new BlogPostEntity();
        blogPost.setTitle(blogPostDTO.getTitle());
        blogPost.setContent(blogPostDTO.getContent());
        blogPost.setCreatedAt(LocalDateTime.now());
        blogPost.setUpdatedAt(LocalDateTime.now());

        // Asociar la categoría
        CategoryEntity category = categoryRepository.findById(blogPostDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        blogPost.setCategory(category);

        // Obtener el usuario autenticado desde el contexto de seguridad (JWT)
        UserEntity user = getAuthenticatedUser();
        blogPost.setUser(user);

        // Asociar las etiquetas
        blogPost.setTags(blogPostDTO.getTags().stream()
                .map(tagName -> tagRepository.findByName(tagName)
                        .orElseGet(() -> {
                            TagEntity tag = new TagEntity(); // Crear una instancia vacía
                            tag.setName(tagName); // Usar el setter para asignar el nombre
                            return tagRepository.save(tag);
                        }))
                .collect(Collectors.toSet()));

        BlogPostEntity savedPost = blogPostRepository.save(blogPost);
        blogPostDTO.setId(savedPost.getId());
        blogPostDTO.setCreatedAt(savedPost.getCreatedAt());
        blogPostDTO.setUpdatedAt(savedPost.getUpdatedAt());
        blogPostDTO.setUserId(user.getId()); // Asignar el ID del usuario al DTO para devolverlo en la respuesta
        return blogPostDTO;
    }


    @Override
    @Transactional
    public BlogPostDTO updatePost(Long postId, BlogPostDTO blogPostDTO) {
        BlogPostEntity blogPost = blogPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post no encontrado"));

        // Obtener el usuario autenticado
        UserEntity currentUser = getAuthenticatedUser();

        // Validar que el usuario puede actualizar este post
        if (!hasRole(currentUser, RoleEnum.ADMIN) && !blogPost.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("No tienes permisos para eliminar este post");
        }


        blogPost.setTitle(blogPostDTO.getTitle());
        blogPost.setContent(blogPostDTO.getContent());
        blogPost.setUpdatedAt(LocalDateTime.now());

        return mapToDTO(blogPostRepository.save(blogPost));
    }

    @Override
    @Transactional
    public void deleteById(Long postId) {
        BlogPostEntity blogPost = blogPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post no encontrado"));

        // Obtener el usuario autenticado
        UserEntity currentUser = getAuthenticatedUser();

        // Validar que el usuario puede eliminar este post
        if (!hasRole(currentUser, RoleEnum.ADMIN) && !blogPost.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("No tienes permisos para eliminar este post");
        }


        blogPostRepository.delete(blogPost);
    }
    @Override
    public List<BlogPostDTO> findAll() {
        return blogPostRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BlogPostDTO> findById(Long id) {
        return blogPostRepository.findById(id)
                .map(this::mapToDTO);
    }

    @Override
    public List<BlogPostDTO> findByCategoryId(Long categoryId) {
        return blogPostRepository.findByCategoryId(categoryId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BlogPostDTO> findByTags(List<String> tags) {
        return blogPostRepository.findByTags_NameIn(tags).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BlogPostDTO> searchByTitleOrContent(String term) {
        return blogPostRepository.findByTitleContainingOrContentContaining(term, term).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BlogPostDTO> searchByTerm(String term) {
        return blogPostRepository.findByTitleContainingOrContentContainingOrCategory_NameEqualsOrTags_NameIn(term, term, term, List.of(term)).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /*
    @Override
    public List<BlogPostDTO> searchByTitleContentCategoryOrTags(String term, String categoryName, List<String> tags) {
        return blogPostRepository.findByTitleContainingOrContentContainingOrCategory_NameContainingOrTags_NameIn(term, term, categoryName, tags).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }*/


    @Override
    public List<BlogPostDTO> findByUserId(Long userId) {
        return blogPostRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // MétodoO auxiliar para mapear BlogPost a BlogPostDTO
    private BlogPostDTO mapToDTO(BlogPostEntity blogPost) {
        BlogPostDTO blogPostDTO = new BlogPostDTO();
        blogPostDTO.setId(blogPost.getId());
        blogPostDTO.setTitle(blogPost.getTitle());
        blogPostDTO.setContent(blogPost.getContent());
        blogPostDTO.setCategoryId(blogPost.getCategory().getId());
        blogPostDTO.setTags(blogPost.getTags().stream()
                .map(TagEntity::getName)
                .collect(Collectors.toSet()));
        blogPostDTO.setCreatedAt(blogPost.getCreatedAt());
        blogPostDTO.setUpdatedAt(blogPost.getUpdatedAt());
        return blogPostDTO;
    }


    // Otros métodos no cambian
}


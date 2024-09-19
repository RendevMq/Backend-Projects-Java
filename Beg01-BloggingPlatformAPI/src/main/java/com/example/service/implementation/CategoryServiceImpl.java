package com.example.service.implementation;

import com.example.persistence.entity.CategoryEntity;
import com.example.persistence.reposiroty.ICategoryRepository;
import com.example.presentation.dto.CategoryDTO;
import com.example.service.interfaces.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    public CategoryDTO save(CategoryDTO categoryDTO) {
        CategoryEntity category = new CategoryEntity();
        category.setName(categoryDTO.getName());
        CategoryEntity savedCategory = categoryRepository.save(category);
        categoryDTO.setId(savedCategory.getId());
        return categoryDTO;
    }

    @Override
    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryDTO(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CategoryDTO> findById(Long id) {
        return categoryRepository.findById(id)
                .map(category -> new CategoryDTO(category.getId(), category.getName()));
    }

    @Override
    public Optional<CategoryDTO> findByName(String name) {
        return categoryRepository.findByName(name)
                .map(category -> new CategoryDTO(category.getId(), category.getName()));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}


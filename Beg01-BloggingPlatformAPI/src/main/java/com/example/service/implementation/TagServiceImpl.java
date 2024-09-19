package com.example.service.implementation;

import com.example.persistence.entity.TagEntity;
import com.example.persistence.reposiroty.ITagRepository;
import com.example.presentation.dto.TagDTO;
import com.example.service.interfaces.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements ITagService {

    @Autowired
    private ITagRepository tagRepository;

    @Override
    public TagDTO save(TagDTO tagDTO) {
        TagEntity tag = new TagEntity();
        tag.setName(tagDTO.getName());
        TagEntity savedTag = tagRepository.save(tag);
        tagDTO.setId(savedTag.getId());
        return tagDTO;
    }

    @Override
    public List<TagDTO> findAll() {
        return tagRepository.findAll().stream()
                .map(tag -> new TagDTO(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TagDTO> findById(Long id) {
        return tagRepository.findById(id)
                .map(tag -> new TagDTO(tag.getId(), tag.getName()));
    }

    @Override
    public Optional<TagDTO> findByName(String name) {
        return tagRepository.findByName(name)
                .map(tag -> new TagDTO(tag.getId(), tag.getName()));
    }

    @Override
    public void deleteById(Long id) {
        tagRepository.deleteById(id);
    }
}


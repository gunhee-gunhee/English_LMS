package com.english.lms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.english.lms.entity.TextEntity;
import com.english.lms.repository.TextRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TextService {
    private final TextRepository textRepository;

    public List<TextEntity> findAll() {
        return textRepository.findAll();
    }

    public TextEntity addText(String name) {
        TextEntity entity = new TextEntity();
        entity.setName(name);
        return textRepository.save(entity);
    }

    public void deleteText(Integer textNum) {
        textRepository.deleteById(textNum);
    }
}

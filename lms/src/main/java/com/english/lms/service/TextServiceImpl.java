package com.english.lms.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.english.lms.entity.TextEntity;
import com.english.lms.repository.TextRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TextServiceImpl implements TextService {

    private final TextRepository textRepository;

    @Override
    public List<TextEntity> findAll() {
        return textRepository.findAll();
    }

    @Override
    public TextEntity addText(String name) {
        TextEntity entity = new TextEntity();
        entity.setName(name);
        return textRepository.save(entity);
    }

    @Override
    public void deleteText(Integer textNum) {
        textRepository.deleteById(textNum);
    }
}

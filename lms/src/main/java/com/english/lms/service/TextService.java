package com.english.lms.service;

import java.util.List;
import com.english.lms.entity.TextEntity;

public interface TextService {
    List<TextEntity> findAll();
    TextEntity addText(String name);
    void deleteText(Integer textNum);
}

package com.english.lms.service;

import java.util.List;
import java.util.Optional;

import com.english.lms.entity.MessageWordEntity;

public interface MessageWordService {
    MessageWordEntity register(String word, String wordJp);
    void delete(Integer id);
    List<MessageWordEntity> findAll();
    Optional<MessageWordEntity> findById(Integer id);
}
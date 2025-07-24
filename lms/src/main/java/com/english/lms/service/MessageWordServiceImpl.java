package com.english.lms.service;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.english.lms.entity.MessageWordEntity;
import com.english.lms.repository.MessageWordRepository;

import jakarta.transaction.Transactional;

@Service
public class MessageWordServiceImpl implements MessageWordService {

    private final MessageWordRepository repository;

    public MessageWordServiceImpl(MessageWordRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public MessageWordEntity register(String word, String wordJp) {
        MessageWordEntity mw = new MessageWordEntity();
        mw.setWord(word);
        mw.setWordJp(wordJp);
        return repository.save(mw);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<MessageWordEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<MessageWordEntity> findById(Integer id) {
        return repository.findById(id);
    }
}

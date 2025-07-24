package com.english.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.english.lms.entity.MessageWordEntity;

public interface MessageWordRepository extends JpaRepository<MessageWordEntity, Integer> {
}

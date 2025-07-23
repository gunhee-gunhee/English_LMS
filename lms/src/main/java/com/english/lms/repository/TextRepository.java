package com.english.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.english.lms.entity.TextEntity;

public interface TextRepository extends JpaRepository<TextEntity, Integer> {

}

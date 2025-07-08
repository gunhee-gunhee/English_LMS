package com.english.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.english.lms.entity.ZoomAccountEntity;

public interface ZoomAccountRepository extends JpaRepository<ZoomAccountEntity, Integer> {

	List<ZoomAccountEntity> findByLinkedFalse();
}

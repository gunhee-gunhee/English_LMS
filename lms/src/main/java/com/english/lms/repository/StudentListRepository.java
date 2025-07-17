package com.english.lms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.english.lms.dto.StudentDTO;
import com.english.lms.entity.StudentEntity;

public interface StudentListRepository extends JpaRepository<StudentEntity, Integer>, StudentRepositoryCustom  {

	

	
	
}

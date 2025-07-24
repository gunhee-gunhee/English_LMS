package com.english.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.english.lms.entity.StudentEntity;

public interface StudentListRepository extends JpaRepository<StudentEntity, Integer>, StudentRepositoryCustom  {

	

	

	
	
}

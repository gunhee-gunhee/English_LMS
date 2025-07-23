package com.english.lms.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.english.lms.dto.StudentDTO;

//// ここにカスタムメソッドを定義
public interface StudentRepositoryCustom {
	
	//学生紹介リスト
	Page<StudentDTO> findAllStudentsPageWithTeacher(Pageable pageable);
	
	//学生の授業登録リスト
	List<StudentDTO> findActiveStuden(String studentSort, String studentDir);
}

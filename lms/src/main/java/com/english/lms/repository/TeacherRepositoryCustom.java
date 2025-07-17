package com.english.lms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.english.lms.dto.StudentDTO;
import com.english.lms.dto.TeacherDTO;

////ここにカスタムメソッドを定義
public interface TeacherRepositoryCustom {

	// ページネーション
	Page<TeacherDTO> findAllStudentsPage(Pageable pageable);
}

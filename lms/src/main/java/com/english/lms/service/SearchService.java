package com.english.lms.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.english.lms.dto.StudentDTO;
import com.english.lms.repository.StudentListRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final StudentListRepository studentListRepository;

    // 학생 검색 (검색어 있으면 검색, 없으면 전체)
    public Page<StudentDTO> searchStudentsPageWithTeacher(String keyword, Pageable pageable) {
        if (keyword != null && !keyword.isBlank()) {
            return studentListRepository.searchStudentsPageWithTeacher(keyword, pageable);
        } else {
            return studentListRepository.findAllStudentsPageWithTeacher(pageable);
        }
    }

    // (추후 확장: 강사/수업 등도 이 안에 추가)
}

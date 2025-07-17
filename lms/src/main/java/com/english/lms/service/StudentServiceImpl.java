package com.english.lms.service;

import com.english.lms.dto.StudentDTO;
import com.english.lms.entity.ClassEntity;
import com.english.lms.entity.PointEntity;
import com.english.lms.entity.StudentEntity;
import com.english.lms.entity.TeacherEntity;
import com.english.lms.enums.Role;
import com.english.lms.repository.ClassRepository;
import com.english.lms.repository.PointRepository;
import com.english.lms.repository.StudentRepository;
import com.english.lms.repository.TeacherRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClassRepository classRepository;
    private final TeacherRepository teacherRepository;
    private final PointRepository pointRepository;   // ★ 추가


    @Override
    public void registerStudent(StudentDTO dto) {
        StudentEntity student = new StudentEntity();
        student.setStudentId(dto.getId());
        student.setPassword(passwordEncoder.encode(dto.getPassword()));
        student.setNickname(dto.getNickname());
        student.setNicknameJp(dto.getNicknameJp());
        student.setAge(dto.getAge());
        student.setEnglishLevel(dto.getEnglishLevel());
        student.setEnglishPurpose(dto.getEnglishPurpose());
        student.setSignupPath(dto.getSignupPath());
        student.setCompany(dto.getCompany());



        // --- point セッチング ---

        student.setPoint(0);
        student.setNullity(false);
        student.setRole(Role.STUDENT);

        // --- joinDate, use, role 등은 @PrePersistで自動処理---
   
        studentRepository.save(student);

        // ★ 회원가입 포인트 지급 (2포인트, type=free, expiresAt=null) 추가
        PointEntity point = PointEntity.builder()
                .studentNum(student.getStudentNum())     // save 후에 값 할당됨
                .pointAmount(2)
                .type("free")
                .createdAt(LocalDate.now())
                .expiresAt(null)
                .absentDate(null)
                .build();
        pointRepository.save(point);
    }

    @Override
    public boolean existsById(String id) {
        // StudentRepository에서 studentId로 중복 체크
        return studentRepository.findByStudentId(id).isPresent();
    }
}

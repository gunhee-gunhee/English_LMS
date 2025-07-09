package com.english.lms.service;

import com.english.lms.dto.StudentDTO;
import com.english.lms.entity.StudentEntity;
import com.english.lms.enums.Role;
import com.english.lms.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

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

        // --- point 기본값 세팅 (꼭 필요) ---
        student.setPoint(0);
        student.setNullity(0);
        student.setRole(Role.STUDENT);

        // --- joinDate, use, role 등은 @PrePersist에서 자동 처리하면 좋음 ---

        studentRepository.save(student);
    }

    @Override
    public boolean existsById(String id) {
        // StudentRepository에서 studentId로 중복 체크
        return studentRepository.findByStudentId(id).isPresent();
    }
}

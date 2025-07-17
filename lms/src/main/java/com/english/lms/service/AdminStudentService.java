package com.english.lms.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.english.lms.dto.StudentDTO;
import com.english.lms.entity.TeacherEntity;
import com.english.lms.enums.Role;
import com.english.lms.repository.StudentListRepository;
import com.english.lms.repository.StudentRepository;
import com.english.lms.repository.TeacherRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

/**
 * 管理者用：学生詳細サービス
 */
@Service
@RequiredArgsConstructor
public class AdminStudentService {

    private final StudentListRepository studentListRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
//
//    AdminStudentService(StudentListRepository studentListRepository) {
//        this.studentListRepository = studentListRepository;
//    }

    /**
     * DBに登録されている「年齢」リストを取得
     */
    public List<String> getAgeOptions() {
        return List.of(
            "10歳未満",   
            "10代",      
            "20代",      
            "30代",      
            "40代",      
            "50代",      
            "60代以上"   
        );
    }


    /**
     * DBに登録されている「英語レベル」リストを取得
     */
    public List<Integer> getEnglishLevelOptions() {
        return java.util.stream.IntStream.rangeClosed(1, 10)
                .boxed()
                .toList();
    }

    /**
     * DBに登録されている「会社」リストを取得
     */
    public List<String> getCompanyOptions() {
        return studentRepository.findDistinctCompanies();
    }

    /**
     * 学生を削除
     */
    public void deleteStudent(Integer studentNum) {
        studentRepository.deleteById(studentNum);
    }
    
    public StudentDTO getStudent(Integer studentNum) {
        return studentRepository.findById(studentNum)
            .map(entity -> StudentDTO.builder()
                .studentNum(entity.getStudentNum())
                .id(entity.getStudentId())  // <- getStudentId()
                .password(entity.getPassword())
                .nickname(entity.getNickname())
                .nicknameJp(entity.getNicknameJp())
                .age(entity.getAge())
                .englishLevel(entity.getEnglishLevel())
                .englishPurpose(entity.getEnglishPurpose())
                .signupPath(entity.getSignupPath())
                // LocalDateTime → String 변환 (예: yyyy-MM-dd)
                .joinDate(entity.getJoinDate() != null ? entity.getJoinDate().toString() : null)
                .nullity(entity.getNullity())
                .point(entity.getPoint())
                // Role enum → String
                .role(entity.getRole() != null ? entity.getRole().name() : null)
                .company(entity.getCompany())
                .build())
            .orElse(null);
    }


    public void updateStudent(Integer studentNum, StudentDTO dto) {
        var optional = studentRepository.findById(studentNum);
        if (optional.isPresent()) {
            var entity = optional.get();
            entity.setPassword(dto.getPassword());
            entity.setNickname(dto.getNickname());
            entity.setAge(dto.getAge());
            entity.setEnglishLevel(dto.getEnglishLevel());
            if (dto.getRole() != null) {
            	entity.setRole(Role.valueOf(dto.getRole().toUpperCase()));
            entity.setCompany(dto.getCompany());
            entity.setNullity(dto.getNullity());
            studentRepository.save(entity);
            }
        }
    }

    public String getTeacherNickname(Integer teacherNum) {
        return teacherRepository.findByTeacher(teacherNum)
                .map(TeacherEntity::getNickname) // 엔티티 이름에 맞게 수정
                .orElse(null);
    }
    
	// ページネーション
	public Page<StudentDTO> getStudentPageWithTeacher(Pageable pageable) {
        
		return studentListRepository.findAllStudentsPageWithTeacher(pageable);
		
	}
}
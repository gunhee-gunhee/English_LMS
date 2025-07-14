package com.english.lms.service;

import com.english.lms.dto.StudentDTO;
import com.english.lms.entity.ClassEntity;
import com.english.lms.entity.StudentEntity;
import com.english.lms.entity.TeacherEntity;
import com.english.lms.enums.Role;
import com.english.lms.repository.ClassRepository;
import com.english.lms.repository.StudentRepository;
import com.english.lms.repository.TeacherRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;

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
        student.setNullity(false);
        student.setRole(Role.STUDENT);

        // --- joinDate, use, role 등은 @PrePersist에서 자동 처리하면 좋음 ---

        studentRepository.save(student);
    }

    @Override
    public boolean existsById(String id) {
        // StudentRepository에서 studentId로 중복 체크
        return studentRepository.findByStudentId(id).isPresent();
    }

    
//    //student-listのStudentDTO生成
//	@Override
//	public List<StudentDTO> getAllStudentWithTeacher() {
//		
//		//StudentEntity
//		List<StudentEntity> allStudents = studentRepository.findAll();
//		
//		//StudentDTOList 追加
//		List<StudentDTO> dtoList = new ArrayList<>();
//		
//		//StudentDTO List 生成
//		for(StudentEntity student : allStudents) {
//			
//			//StudentNum
//			Integer studentNum = student.getStudentNum();
//			
//			// studentNum に対応する ClassEntity を取得する
//			ClassEntity classEntity = classRepository.findFirstByStudentNum(studentNum);
//			
//			//teacher nickNameを取得
//			// teacherNickNameはString型で受け取る必要がある！
//			String teacherNickname = "ニックネームなし";
//			
//			if(classEntity != null ) {
//				
//				//classEntityてteacherNumを取得する。
//				Integer teacherNum = classEntity.getTeacherNum();
//				
//				
//				Optional<TeacherEntity> teacherOpt = teacherRepository.findById(teacherNum);
//				if (teacherOpt.isPresent()) {
//				    teacherNickname = teacherOpt.get().getNickname();
//				}
//			}
//			
//			
//			
//			//StudentDTO
//			StudentDTO dto = StudentDTO.builder()
//					.id(student.getStudentId())
//					.nickname(student.getNickname())
//					.company(student.getCompany())
//					.age(student.getAge())
//					.englishLevel(student.getEnglishLevel())
//					.point(student.getPoint())
//					.nullity(student.getNullity())
//					.role(student.getRole() != null ? student.getRole().name() : null)
//					.teacherNickname(teacherNickname)
//					.build();
//					
//			//DTOListに追加
//			dtoList.add(dto);
//					
//
//
//		 }
//		return dtoList;
//	}
	
	// ページネーション
	@Override
	public Page<StudentDTO> getStudentPageWithTeacher(Pageable pageable) {
        //Pageableは、現在のページ番号や1ページあたりに表示するデータの件数など、ページングに関する情報を持つオブジェクト
		//StudentEntityは、データベースの1行（レコード）を表すオブジェクト
		//学生リストを取得する。
		Page<StudentEntity> studentPage = studentRepository.findAllStudentsPage(pageable);
		
		//studentEntity -> StudentDTO (+teacher 情報）
        List<StudentDTO> dtoList = studentPage.getContent().stream().map(student -> {
         
          //studentNumで　classEntity取得
        	Integer StudentNum = student.getStudentNum();
        	ClassEntity classEntity = classRepository.findByStudentNumQuery(StudentNum);
        	
        	// デフォルト値：ニックネームなし
        	String teacherNickname = "ニックネームなし";
        	
        	if(classEntity != null) {
        		Integer teacherNum = classEntity.getTeacherNum();
        		Optional<TeacherEntity> teacherOpt = teacherRepository.findById(teacherNum);
        		if(teacherOpt.isPresent()) {
        			teacherNickname = teacherOpt.get().getNickname();
        		}
        	}
        	
        	//studentDTO 生成
        	//StudentDTO
        	return StudentDTO.builder()
                    .id(student.getStudentId())
                    .nickname(student.getNickname())
                    .company(student.getCompany())
                    .age(student.getAge())
                    .englishLevel(student.getEnglishLevel())
                    .point(student.getPoint())
                    .nullity(student.getNullity())
                    .role(student.getRole() != null ? student.getRole().name() : null)
                    .teacherNickname(teacherNickname)
                    .build();

        }).toList(); 
        	
		
		
		//new PageImpl(List<T> content, Pageable pageable, long total)
        //content : List<StudentDTO> dtoList \
        //pageable : // 現在のページング設定（ページ番号、1ページあたりの件数、並び順など）
        //total : // 全体のデータ件数（ページネーションの計算に使用）
		return new PageImpl<>(dtoList, pageable, studentPage.getTotalElements());
	}
    
    
}

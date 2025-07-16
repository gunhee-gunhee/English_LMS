package com.english.lms.service;

import com.english.lms.dto.StudentDTO;
import com.english.lms.entity.ClassEntity;
import com.english.lms.entity.StudentEntity;
import com.english.lms.entity.TeacherEntity;
import com.english.lms.enums.Role;
import com.english.lms.repository.ClassRepository;
import com.english.lms.repository.StudentRepository;
import com.english.lms.repository.TeacherRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
    
    @PersistenceContext
    private EntityManager entityManager;

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
	
	// ページネーション
	@Override
	public Page<StudentDTO> getStudentPageWithTeacher(Pageable pageable) {
        //Pageableは、現在のページ番号や1ページあたりに表示するデータの件数など、ページングに関する情報を持つオブジェクト
		//StudentEntityは、データベースの1行（レコード）を表すオブジェクト
		//学生リストを取得する。
		int limit = pageable.getPageSize();
		int offset = (int) pageable.getOffset();
//		Page<Object[]> page = studentRepository.findAllStudentsPage(limit, offset);
		
		String sql = "SELECT s.id, s.nickname, s.age, s.english_level, s.company, s.nullity, s.point, s.role, " +
                "COALESCE(t.nickname, 'ニックネームなし') AS teacher_nickname " +
                "FROM lms_student s " +
                "LEFT JOIN lms_class c ON s.student_num = c.student_num " +
                "LEFT JOIN lms_teacher t ON c.teacher_num = t.teacher_num " +
                "ORDER BY s.id LIMIT " + limit + " OFFSET " + offset;
		
		 List<Object[]> rows = entityManager.createNativeQuery(sql).getResultList();
		
		 
		  long total = ((Number) entityManager
	                .createNativeQuery("SELECT COUNT(*) FROM lms_student")
	                .getSingleResult())
	                .longValue();
		//Object[] row = 1行分のカラムデータ
		// row は Object[] 型：StudentEntity の各フィールドが順番に格納されていて、最後の要素は teacher_nickname
		  List<StudentDTO> dtoList = rows.stream().map(row ->
        
        	// 例：Object[] のフィールド順に従ってマッピング（順番はクエリのカラム順と一致）
        	// SELECT のカラム順 = Object[] の配列順
        	 
              StudentDTO.builder()
                      .id((String) row[0])
                      .nickname((String) row[1])
                      .age((String) row[2])
                      .englishLevel((Integer) row[3])
                      .company((String) row[4])
                      .nullity((Boolean) row[5])
                      .point((Integer) row[6])
                      .role(row[7] != null ? row[7].toString() : null)
                      .teacherNickname((String) row[row.length - 1])
                      .build()
      ).toList();

       
        	
		
		
		//new PageImpl(List<T> content, Pageable pageable, long total)
        //pageable : // 現在のページング設定（ページ番号、1ページあたりの件数、並び順など）
        //total : // 全体のデータ件数（ページネーションの計算に使用）
		  return new PageImpl<>(dtoList, pageable, total);
	}
    
    
}

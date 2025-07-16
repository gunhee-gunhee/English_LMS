package com.english.lms.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.english.lms.dto.StudentDTO;
import com.english.lms.entity.TeacherEntity;
import com.english.lms.enums.Role;
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
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

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
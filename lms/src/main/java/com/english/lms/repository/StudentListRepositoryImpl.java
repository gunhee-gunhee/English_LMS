package com.english.lms.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.english.lms.dto.StudentDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class StudentListRepositoryImpl implements StudentRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
	
	@Override
	// ページネーション
		public Page<StudentDTO> findAllStudentsPageWithTeacher(Pageable pageable) {
	        //Pageableは、現在のページ番号や1ページあたりに表示するデータの件数など、ページングに関する情報を持つオブジェクト
			//学生リストを取得する。
			int limit = pageable.getPageSize();
			int offset = (int) pageable.getOffset();
			
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

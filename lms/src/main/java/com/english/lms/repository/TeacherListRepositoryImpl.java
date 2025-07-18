package com.english.lms.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.english.lms.dto.TeacherDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class TeacherListRepositoryImpl implements TeacherRepositoryCustom {

	  @PersistenceContext
	    private EntityManager entityManager;
	
	@Override
	public Page<TeacherDTO> findAllStudentsPage(Pageable pageable) {
		
		//Pageableは、現在のページ番号や1ページあたりに表示するデータの件数など、ページングに関する情報を持つオブジェクト
		//講師リストを取得する。
		int limit = pageable.getPageSize();
		int offset = (int) pageable.getOffset();
		
		String sql = "SELECT t.id, t.nickname, SUBSTRING_INDEX(z.zoom_id, '@', 1) AS zoomId, t.nullity, t.role, t.teacher_num "+
					 "FROM lms_teacher t " + 
					 "LEFT JOIN lms_zoom_account z ON t.zoom_num = z.zoom_num " +
					 "ORDER BY t.nickname LIMIT " + limit + " OFFSET " + offset;
		
		 List<Object[]> rows = entityManager.createNativeQuery(sql).getResultList();
		
		long total = ((Number) entityManager
				.createNativeQuery("SELECT COUNT(*) FROM lms_teacher")
				.getSingleResult())
				.longValue();
		
		//Object[] row = 1行分のカラムデータ
		List<TeacherDTO> dtoList = rows.stream().map(row -> 
		 
		// 例：Object[] のフィールド順に従ってマッピング（順番はクエリのカラム順と一致）
    	// SELECT のカラム順 = Object[] の配列順
		
		TeacherDTO.builder()
			.id((String) row[0])
			.nickname((String) row[1])
			.zoomId((String) row[2])
			.nullity((Boolean) row[3])
			.role(row[4] != null ? row[4].toString() : null)
			.teacherNum((Integer) row[5])
			.build()
		
		).toList();
		
		
		
		//new PageImpl(List<T> content, Pageable pageable, long total)
        //pageable : // 現在のページング設定（ページ番号、1ページあたりの件数、並び順など）
        //total : // 全体のデータ件数（ページネーションの計算に使用）
		return  new PageImpl<>(dtoList, pageable, total);
	}

}

package com.english.lms.repository;

import java.util.ArrayList;
import java.util.Arrays;
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



	//許可されたソートカラムのみを列挙したホワイトリスト（SQLインジェクション対策）
	private static final List<String> SORT_COLUMNS = Arrays.asList(
			"id", "nickname", "age", "english_level"
			);
	
	//学生の授業登録リスト
	/**
	 * @param String studentSort
	 * @param String studentDir
	 * @return List<StudentDTO>
	 */
	@Override
	public List<StudentDTO> findActiveStuden(String studentSort, String studentDir) {
		
		//許可されたカラムかどうかをホワイトリストで確認し、違う場合は id を使用する
		if (!SORT_COLUMNS.contains(studentSort)) studentSort = "id";
		
		String dir = "asc".equalsIgnoreCase(studentDir) ? "ASC" : "DESC";
		
		String sql = "SELECT student_num, id, nickname, age, english_level, nullity " +
					 "FROM lms_student WHERE nullity = 0 " +
				     "ORDER BY " + studentSort+ " " + dir;
		
		// ネイティブSQLを実行し、結果を Object[] の List として受け取る
		List<Object[]> result = entityManager.createNativeQuery(sql).getResultList();
		
		List<StudentDTO> dtoList = result.stream().map(row ->
			
			StudentDTO.builder()
				.studentNum(row[0] != null ? ((Number) row[0]).intValue() : null)
				.id((String) row[1])
				.nickname((String) row[2])
				.age((String) row[3])
				.englishLevel(row[4] !=null ? ((Number) row[4]).intValue() : null)
				.nullity(row[5] != null ? Boolean.valueOf(row[5].toString()) : null)
				.build()
				).toList();
				
		
		return dtoList;
	}

	//学生紹介のリスト
	/**
	 * @param Pageable pageable
	 * @return Page<StudentDTO>
	 * */
    @Override
    public Page<StudentDTO> findAllStudentsPageWithTeacher(Pageable pageable) {
        int limit = pageable.getPageSize();
        int offset = (int) pageable.getOffset();

        String sql = "SELECT s.id, s.nickname, s.nickname_jp, s.age, s.english_level, s.company, s.nullity, s.point, s.role, s.student_num, " +
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

        List<StudentDTO> dtoList = rows.stream().map(row ->
                StudentDTO.builder()
                        .id((String) row[0])
                        .nickname((String) row[1])
                        .nicknameJp((String) row[2])
                        .age((String) row[3])
                        .englishLevel((Integer) row[4])
                        .company((String) row[5])
                        .nullity((Boolean) row[6])
                        .point((Integer) row[7])
                        .role(row[8] != null ? row[8].toString() : null)
                        .studentNum((Integer) row[9])
                        .teacherNickname((String) row[10])
                        .build()
        ).toList();

        return new PageImpl<>(dtoList, pageable, total);
    }

    //学生紹介ページの検索メソッド
    @Override
    public Page<StudentDTO> searchStudentsPageWithTeacher(String keyword, Pageable pageable) {
        int limit = pageable.getPageSize();
        int offset = (int) pageable.getOffset();

        String where = "";
        boolean isEnableKeyword = "有効".equals(keyword);
        boolean isDisableKeyword = "無効".equals(keyword);

        // 기본 검색 조건
        if (keyword != null && !keyword.isBlank()) {
            where = "WHERE (s.id LIKE :kw OR s.nickname LIKE :kw OR s.nickname_jp LIKE :kw OR s.company LIKE :kw";
            if (isEnableKeyword) {
                where += " OR s.nullity = false";
            } else if (isDisableKeyword) {
                where += " OR s.nullity = true";
            }
            where += ")";
        }

        String sql = "SELECT s.id, s.nickname, s.nickname_jp, s.age, s.english_level, s.company, s.nullity, s.point, s.role, s.student_num, " +
                "COALESCE(t.nickname, 'ニックネームなし') AS teacher_nickname " +
                "FROM lms_student s " +
                "LEFT JOIN lms_class c ON s.student_num = c.student_num " +
                "LEFT JOIN lms_teacher t ON c.teacher_num = t.teacher_num " +
                where +
                " ORDER BY s.id LIMIT :limit OFFSET :offset";

        var query = entityManager.createNativeQuery(sql);
        if (keyword != null && !keyword.isBlank()) {
            query.setParameter("kw", "%" + keyword + "%");
        }
        query.setParameter("limit", limit);
        query.setParameter("offset", offset);

        List<Object[]> rows = query.getResultList();

        String countSql = "SELECT COUNT(*) FROM lms_student s ";
        if (!where.isEmpty()) countSql += where;
        var countQuery = entityManager.createNativeQuery(countSql);
        if (keyword != null && !keyword.isBlank()) {
            countQuery.setParameter("kw", "%" + keyword + "%");
        }
        long total = ((Number) countQuery.getSingleResult()).longValue();

        List<StudentDTO> dtoList = rows.stream().map(row ->
                StudentDTO.builder()
                        .id((String) row[0])
                        .nickname((String) row[1])
                        .nicknameJp((String) row[2])
                        .age((String) row[3])
                        .englishLevel((Integer) row[4])
                        .company((String) row[5])
                        .nullity((Boolean) row[6])
                        .point((Integer) row[7])
                        .role(row[8] != null ? row[8].toString() : null)
                        .studentNum((Integer) row[9])
                        .teacherNickname((String) row[10])
                        .build()
        ).toList();

        return new PageImpl<>(dtoList, pageable, total);
    }

}

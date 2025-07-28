package com.english.lms.repository;

import com.english.lms.dto.ClassDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.sql.Time;
import java.util.List;
import java.util.stream.Collectors;

public class ClassRepositoryImpl implements ClassRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    // (1) 월별 전체 조회
    @Override
    public Page<ClassDTO> findAllClassesWithDetailsAndMonth(String classMonth, Pageable pageable) {
        int limit = pageable.getPageSize();
        int offset = (int) pageable.getOffset();

        String sql = "SELECT c.class_num, c.student_num, c.teacher_num, c.week_days, c.text_num, c.start_date, " +
                "c.start_time, c.end_time, c.class_month, " +
                "s.id AS student_id, s.nickname AS student_nickname, s.nickname_jp, s.english_level, s.age, " +
                "t.id AS teacher_id, t.nickname AS teacher_nickname, " +
                "za.zoom_id AS zoom_id, " +
                "tx.name AS text_name " +
                "FROM lms_class c " +
                "LEFT JOIN lms_student s ON c.student_num = s.student_num " +
                "LEFT JOIN lms_teacher t ON c.teacher_num = t.teacher_num " +
                "LEFT JOIN lms_text tx ON c.text_num = tx.text_num " +
                "LEFT JOIN lms_zoom_account za ON t.zoom_num = za.zoom_num " +
                "WHERE (:classMonth IS NULL OR c.class_month = :classMonth) " +
                "ORDER BY c.class_num LIMIT :limit OFFSET :offset";

        List<Object[]> rows = entityManager.createNativeQuery(sql)
                .setParameter("classMonth", classMonth)
                .setParameter("limit", limit)
                .setParameter("offset", offset)
                .getResultList();

        long total = ((Number) entityManager
                .createNativeQuery("SELECT COUNT(*) FROM lms_class c WHERE (:classMonth IS NULL OR c.class_month = :classMonth)")
                .setParameter("classMonth", classMonth)
                .getSingleResult())
                .longValue();

        List<ClassDTO> dtoList = rows.stream().map(row ->
                ClassDTO.builder()
                        .classNum((Integer) row[0])
                        .studentNum((Integer) row[1])
                        .teacherNum((Integer) row[2])
                        .weekDays((String) row[3])
                        .textNum((Integer) row[4])
                        .startDate(row[5] != null ? ((java.sql.Date) row[5]).toLocalDate() : null)
                        .startTime(row[6] != null ? ((Time) row[6]).toLocalTime() : null)
                        .endTime(row[7] != null ? ((Time) row[7]).toLocalTime() : null)
                        .classMonth((String) row[8])
                        .studentId((String) row[9])
                        .studentNickname((String) row[10])
                        .studentNicknameJp((String) row[11])
                        .studentEnglishLevel(row[12] != null ? ((Number) row[12]).intValue() : null)
                        .studentAge(row[13] != null ? row[13].toString() : null)
                        .teacherId((String) row[14])
                        .teacherNickname((String) row[15])
                        .zoomId((String) row[16])
                        .textName((String) row[17])
                        .build()
        ).collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, total);
    }

    // (2) 월별 + 검색
    @Override
    public Page<ClassDTO> searchClassesWithDetailsAndMonth(String q, String classMonth, Pageable pageable) {
        int limit = pageable.getPageSize();
        int offset = (int) pageable.getOffset();

        String sql = "SELECT c.class_num, c.student_num, c.teacher_num, c.week_days, c.text_num, c.start_date, " +
                "c.start_time, c.end_time, c.class_month, " +
                "s.id AS student_id, s.nickname AS student_nickname, s.nickname_jp, s.english_level, s.age, " +
                "t.id AS teacher_id, t.nickname AS teacher_nickname, " +
                "za.zoom_id AS zoom_id, " +
                "tx.name AS text_name " +
                "FROM lms_class c " +
                "LEFT JOIN lms_student s ON c.student_num = s.student_num " +
                "LEFT JOIN lms_teacher t ON c.teacher_num = t.teacher_num " +
                "LEFT JOIN lms_text tx ON c.text_num = tx.text_num " +
                "LEFT JOIN lms_zoom_account za ON t.zoom_num = za.zoom_num " +
                "WHERE (:classMonth IS NULL OR c.class_month = :classMonth) " +
                "AND (s.id LIKE :kw OR s.nickname LIKE :kw OR s.nickname_jp LIKE :kw OR t.nickname LIKE :kw OR tx.name LIKE :kw OR c.week_days LIKE :kw) " +
                "ORDER BY c.class_num LIMIT :limit OFFSET :offset";

        List<Object[]> rows = entityManager.createNativeQuery(sql)
                .setParameter("classMonth", classMonth)
                .setParameter("kw", "%" + q + "%")
                .setParameter("limit", limit)
                .setParameter("offset", offset)
                .getResultList();

        String countSql = "SELECT COUNT(*) FROM lms_class c " +
                "LEFT JOIN lms_student s ON c.student_num = s.student_num " +
                "LEFT JOIN lms_teacher t ON c.teacher_num = t.teacher_num " +
                "LEFT JOIN lms_text tx ON c.text_num = tx.text_num " +
                "LEFT JOIN lms_zoom_account za ON t.zoom_num = za.zoom_num " +
                "WHERE (:classMonth IS NULL OR c.class_month = :classMonth) " +
                "AND (s.id LIKE :kw OR s.nickname LIKE :kw OR s.nickname_jp LIKE :kw OR t.nickname LIKE :kw OR tx.name LIKE :kw OR c.week_days LIKE :kw)";

        long total = ((Number) entityManager.createNativeQuery(countSql)
                .setParameter("classMonth", classMonth)
                .setParameter("kw", "%" + q + "%")
                .getSingleResult()).longValue();

        List<ClassDTO> dtoList = rows.stream().map(row ->
                ClassDTO.builder()
                        .classNum((Integer) row[0])
                        .studentNum((Integer) row[1])
                        .teacherNum((Integer) row[2])
                        .weekDays((String) row[3])
                        .textNum((Integer) row[4])
                        .startDate(row[5] != null ? ((java.sql.Date) row[5]).toLocalDate() : null)
                        .startTime(row[6] != null ? ((Time) row[6]).toLocalTime() : null)
                        .endTime(row[7] != null ? ((Time) row[7]).toLocalTime() : null)
                        .classMonth((String) row[8])
                        .studentId((String) row[9])
                        .studentNickname((String) row[10])
                        .studentNicknameJp((String) row[11])
                        .studentEnglishLevel(row[12] != null ? ((Number) row[12]).intValue() : null)
                        .studentAge(row[13] != null ? row[13].toString() : null)
                        .teacherId((String) row[14])
                        .teacherNickname((String) row[15])
                        .zoomId((String) row[16])
                        .textName((String) row[17])
                        .build()
        ).collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, total);
    }

    // (3) 전체 페이징(월 제한 없음)
    @Override
    public Page<ClassDTO> findAllClassesWithDetails(Pageable pageable) {
        int limit = pageable.getPageSize();
        int offset = (int) pageable.getOffset();

        String sql = "SELECT c.class_num, c.student_num, c.teacher_num, c.week_days, c.text_num, c.start_date, " +
                "c.start_time, c.end_time, c.class_month, " +
                "s.id AS student_id, s.nickname AS student_nickname, s.nickname_jp, s.english_level, s.age, " +
                "t.id AS teacher_id, t.nickname AS teacher_nickname, " +
                "za.zoom_id AS zoom_id, " +
                "tx.name AS text_name " +
                "FROM lms_class c " +
                "LEFT JOIN lms_student s ON c.student_num = s.student_num " +
                "LEFT JOIN lms_teacher t ON c.teacher_num = t.teacher_num " +
                "LEFT JOIN lms_text tx ON c.text_num = tx.text_num " +
                "LEFT JOIN lms_zoom_account za ON t.zoom_num = za.zoom_num " +
                "ORDER BY c.class_num LIMIT :limit OFFSET :offset";

        List<Object[]> rows = entityManager.createNativeQuery(sql)
                .setParameter("limit", limit)
                .setParameter("offset", offset)
                .getResultList();

        long total = ((Number) entityManager
                .createNativeQuery("SELECT COUNT(*) FROM lms_class")
                .getSingleResult())
                .longValue();

        List<ClassDTO> dtoList = rows.stream().map(row ->
                ClassDTO.builder()
                        .classNum((Integer) row[0])
                        .studentNum((Integer) row[1])
                        .teacherNum((Integer) row[2])
                        .weekDays((String) row[3])
                        .textNum((Integer) row[4])
                        .startDate(row[5] != null ? ((java.sql.Date) row[5]).toLocalDate() : null)
                        .startTime(row[6] != null ? ((Time) row[6]).toLocalTime() : null)
                        .endTime(row[7] != null ? ((Time) row[7]).toLocalTime() : null)
                        .classMonth((String) row[8])
                        .studentId((String) row[9])
                        .studentNickname((String) row[10])
                        .studentNicknameJp((String) row[11])
                        .studentEnglishLevel(row[12] != null ? ((Number) row[12]).intValue() : null)
                        .studentAge(row[13] != null ? row[13].toString() : null)
                        .teacherId((String) row[14])
                        .teacherNickname((String) row[15])
                        .zoomId((String) row[16])
                        .textName((String) row[17])
                        .build()
        ).collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, total);
    }

    // (4) 전체+검색
    @Override
    public Page<ClassDTO> searchClassesWithDetails(String q, Pageable pageable) {
        int limit = pageable.getPageSize();
        int offset = (int) pageable.getOffset();

        String sql = "SELECT c.class_num, c.student_num, c.teacher_num, c.week_days, c.text_num, c.start_date, " +
                "c.start_time, c.end_time, c.class_month, " +
                "s.id AS student_id, s.nickname AS student_nickname, s.nickname_jp, s.english_level, s.age, " +
                "t.id AS teacher_id, t.nickname AS teacher_nickname, " +
                "za.zoom_id AS zoom_id, " +
                "tx.name AS text_name " +
                "FROM lms_class c " +
                "LEFT JOIN lms_student s ON c.student_num = s.student_num " +
                "LEFT JOIN lms_teacher t ON c.teacher_num = t.teacher_num " +
                "LEFT JOIN lms_text tx ON c.text_num = tx.text_num " +
                "LEFT JOIN lms_zoom_account za ON t.zoom_num = za.zoom_num " +
                "WHERE (s.id LIKE :kw OR s.nickname LIKE :kw OR s.nickname_jp LIKE :kw OR t.nickname LIKE :kw OR tx.name LIKE :kw OR c.week_days LIKE :kw) " +
                "ORDER BY c.class_num LIMIT :limit OFFSET :offset";

        List<Object[]> rows = entityManager.createNativeQuery(sql)
                .setParameter("kw", "%" + q + "%")
                .setParameter("limit", limit)
                .setParameter("offset", offset)
                .getResultList();

        String countSql = "SELECT COUNT(*) FROM lms_class c " +
                "LEFT JOIN lms_student s ON c.student_num = s.student_num " +
                "LEFT JOIN lms_teacher t ON c.teacher_num = t.teacher_num " +
                "LEFT JOIN lms_text tx ON c.text_num = tx.text_num " +
                "LEFT JOIN lms_zoom_account za ON t.zoom_num = za.zoom_num " +
                "WHERE (s.id LIKE :kw OR s.nickname LIKE :kw OR s.nickname_jp LIKE :kw OR t.nickname LIKE :kw OR tx.name LIKE :kw OR c.week_days LIKE :kw)";

        long total = ((Number) entityManager.createNativeQuery(countSql)
                .setParameter("kw", "%" + q + "%")
                .getSingleResult()).longValue();

        List<ClassDTO> dtoList = rows.stream().map(row ->
                ClassDTO.builder()
                        .classNum((Integer) row[0])
                        .studentNum((Integer) row[1])
                        .teacherNum((Integer) row[2])
                        .weekDays((String) row[3])
                        .textNum((Integer) row[4])
                        .startDate(row[5] != null ? ((java.sql.Date) row[5]).toLocalDate() : null)
                        .startTime(row[6] != null ? ((Time) row[6]).toLocalTime() : null)
                        .endTime(row[7] != null ? ((Time) row[7]).toLocalTime() : null)
                        .classMonth((String) row[8])
                        .studentId((String) row[9])
                        .studentNickname((String) row[10])
                        .studentNicknameJp((String) row[11])
                        .studentEnglishLevel(row[12] != null ? ((Number) row[12]).intValue() : null)
                        .studentAge(row[13] != null ? row[13].toString() : null)
                        .teacherId((String) row[14])
                        .teacherNickname((String) row[15])
                        .zoomId((String) row[16])
                        .textName((String) row[17])
                        .build()
        ).collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, total);
    }

    // (5) 상세
    @Override
    public ClassDTO findClassDetailByClassNum(Integer classNum) {
        String sql = "SELECT c.class_num, c.student_num, c.teacher_num, c.week_days, c.text_num, c.start_date, " +
                "c.start_time, c.end_time, c.class_month, " +
                "s.id AS student_id, s.nickname AS student_nickname, s.nickname_jp, s.english_level, s.age, " +
                "t.id AS teacher_id, t.nickname AS teacher_nickname, " +
                "za.zoom_id AS zoom_id, " +
                "tx.name AS text_name " +
                "FROM lms_class c " +
                "LEFT JOIN lms_student s ON c.student_num = s.student_num " +
                "LEFT JOIN lms_teacher t ON c.teacher_num = t.teacher_num " +
                "LEFT JOIN lms_text tx ON c.text_num = tx.text_num " +
                "LEFT JOIN lms_zoom_account za ON t.zoom_num = za.zoom_num " +
                "WHERE c.class_num = :classNum";

        Object[] row = (Object[]) entityManager.createNativeQuery(sql)
                .setParameter("classNum", classNum)
                .getSingleResult();

        return ClassDTO.builder()
                .classNum((Integer) row[0])
                .studentNum((Integer) row[1])
                .teacherNum((Integer) row[2])
                .weekDays((String) row[3])
                .textNum((Integer) row[4])
                .startDate(row[5] != null ? ((java.sql.Date) row[5]).toLocalDate() : null)
                .startTime(row[6] != null ? ((Time) row[6]).toLocalTime() : null)
                .endTime(row[7] != null ? ((Time) row[7]).toLocalTime() : null)
                .classMonth((String) row[8])
                .studentId((String) row[9])
                .studentNickname((String) row[10])
                .studentNicknameJp((String) row[11])
                .studentEnglishLevel(row[12] != null ? ((Number) row[12]).intValue() : null)
                .studentAge(row[13] != null ? row[13].toString() : null)
                .teacherId((String) row[14])
                .teacherNickname((String) row[15])
                .zoomId((String) row[16])
                .textName((String) row[17])
                .build();
    }

    // (6) 년도 목록
    @Override
    public List<String> findDistinctYearsFromClassMonth() {
        String sql = "SELECT DISTINCT LEFT(class_month, 4) AS year FROM lms_class ORDER BY year";
        List<?> result = entityManager.createNativeQuery(sql).getResultList();
        return result.stream().map(Object::toString).collect(Collectors.toList());
    }
}

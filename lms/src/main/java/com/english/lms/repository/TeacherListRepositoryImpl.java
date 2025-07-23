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
    public Page<TeacherDTO> findAllTeachersPage(Pageable pageable) {
        int limit = pageable.getPageSize();
        int offset = (int) pageable.getOffset();

        String sql = "SELECT t.id, t.nickname, SUBSTRING_INDEX(z.zoom_id, '@', 1) AS zoomId, t.nullity, t.role, t.teacher_num " +
                "FROM lms_teacher t " +
                "LEFT JOIN lms_zoom_account z ON t.zoom_num = z.zoom_num " +
                "ORDER BY t.nickname LIMIT " + limit + " OFFSET " + offset;

        List<Object[]> rows = entityManager.createNativeQuery(sql).getResultList();

        long total = ((Number) entityManager
                .createNativeQuery("SELECT COUNT(*) FROM lms_teacher")
                .getSingleResult())
                .longValue();

        List<TeacherDTO> dtoList = rows.stream().map(row ->
                TeacherDTO.builder()
                        .id((String) row[0])
                        .nickname((String) row[1])
                        .zoomId((String) row[2])
                        .nullity((Boolean) row[3])
                        .role(row[4] != null ? row[4].toString() : null)
                        .teacherNum((Integer) row[5])
                        .build()
        ).toList();

        return new PageImpl<>(dtoList, pageable, total);
    }

    @Override
    public Page<TeacherDTO> searchTeachersPage(String keyword, Pageable pageable) {
        int limit = pageable.getPageSize();
        int offset = (int) pageable.getOffset();

        String where = "";
        boolean isEnableKeyword = "有効".equals(keyword);
        boolean isDisableKeyword = "無効".equals(keyword);

        if (keyword != null && !keyword.isBlank()) {
            where = "WHERE (t.id LIKE :kw OR t.nickname LIKE :kw OR z.zoom_id LIKE :kw";
            if (isEnableKeyword) {
                where += " OR t.nullity = false";
            } else if (isDisableKeyword) {
                where += " OR t.nullity = true";
            }
            where += ")";
        }

        String sql = "SELECT t.id, t.nickname, SUBSTRING_INDEX(z.zoom_id, '@', 1) AS zoomId, t.nullity, t.role, t.teacher_num " +
                "FROM lms_teacher t " +
                "LEFT JOIN lms_zoom_account z ON t.zoom_num = z.zoom_num " +
                where +
                " ORDER BY t.nickname LIMIT :limit OFFSET :offset";

        var query = entityManager.createNativeQuery(sql);
        if (keyword != null && !keyword.isBlank()) {
            query.setParameter("kw", "%" + keyword + "%");
        }
        query.setParameter("limit", limit);
        query.setParameter("offset", offset);

        List<Object[]> rows = query.getResultList();

        String countSql = "SELECT COUNT(*) FROM lms_teacher t LEFT JOIN lms_zoom_account z ON t.zoom_num = z.zoom_num ";
        if (!where.isEmpty()) countSql += where;
        var countQuery = entityManager.createNativeQuery(countSql);
        if (keyword != null && !keyword.isBlank()) {
            countQuery.setParameter("kw", "%" + keyword + "%");
        }
        long total = ((Number) countQuery.getSingleResult()).longValue();

        List<TeacherDTO> dtoList = rows.stream().map(row ->
                TeacherDTO.builder()
                        .id((String) row[0])
                        .nickname((String) row[1])
                        .zoomId((String) row[2])
                        .nullity((Boolean) row[3])
                        .role(row[4] != null ? row[4].toString() : null)
                        .teacherNum((Integer) row[5])
                        .build()
        ).toList();

        return new PageImpl<>(dtoList, pageable, total);
    }
}

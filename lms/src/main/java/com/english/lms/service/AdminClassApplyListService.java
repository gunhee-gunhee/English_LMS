package com.english.lms.service;

import com.english.lms.dto.ClassApplyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminClassApplyListService {

    private final NamedParameterJdbcTemplate jdbc;

    public Page<ClassApplyDto> findApplyPage(int year, int month, String q, Pageable pageable) {
        String keyword = (q == null) ? "" : q.trim();

        // 정렬
        Sort sort = pageable.getSort().isUnsorted()
                ? Sort.by(Sort.Direction.DESC, "applyDate")
                : pageable.getSort();

        String orderBy = sort.stream()
                .map(o -> mapSortKey(o.getProperty()) + " " + (o.isAscending() ? "ASC" : "DESC"))
                .collect(Collectors.joining(", "));
        if (orderBy.isBlank()) {
            orderBy = "a.apply_date DESC";
        }

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("year", year)
                .addValue("month", month)
                .addValue("q", keyword)
                .addValue("limit", pageable.getPageSize())
                .addValue("offset", (int) pageable.getOffset());

        // 공통 WHERE
        String where = ""
                + " WHERE YEAR(a.start_date) = :year"
                + "   AND MONTH(a.start_date) = :month"
                + "   AND ( :q = ''"
                + "         OR s.nickname LIKE CONCAT('%', :q, '%')"
                + "         OR a.student_id LIKE CONCAT('%', :q, '%')"
                + "         OR a.course LIKE CONCAT('%', :q, '%')"
                + "       )";

        // 총 개수
        String countSql = ""
                + "SELECT COUNT(*)"
                + "  FROM lms_class_apply a"
                + "  LEFT JOIN lms_student s ON s.id = a.student_id"
                + where;

        int total = 0;
        try {
            total = Optional.ofNullable(jdbc.queryForObject(countSql, params, Integer.class)).orElse(0);
        } catch (EmptyResultDataAccessException ignore) {}

        if (total == 0) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        // 목록 조회
        String listSql = ""
                + "SELECT"
                + "    a.class_apply_num  AS applyNum,"
                + "    COALESCE(s.nickname, '') AS studentNickname,"
                + "    a.student_id       AS studentId,"
                + "    a.start_date       AS startDate,"
                + "    a.course           AS course,"
                + "    a.first_choice     AS firstChoice,"
                + "    a.second_choice    AS secondChoice,"
                + "    a.third_choice     AS thirdChoice,"
                + "    a.apply_date       AS applyDate"
                + "  FROM lms_class_apply a"
                + "  LEFT JOIN lms_student s ON s.id = a.student_id"
                + where
                + "  ORDER BY " + orderBy
                + "  LIMIT :limit OFFSET :offset";

        var rows = jdbc.query(listSql, params,
                new BeanPropertyRowMapper<>(ClassApplyDto.class));

        return new PageImpl<>(rows, pageable, total);
    }

    /**
     * 단건 조회 (상세 페이지)
     */
    public ClassApplyDto findById(Long applyNum) {
        String sql = ""
                + "SELECT"
                + "    a.class_apply_num  AS applyNum,"
                + "    COALESCE(s.nickname, '') AS studentNickname,"
                + "    a.student_id       AS studentId,"
                + "    a.start_date       AS startDate,"
                + "    a.course           AS course,"
                + "    a.first_choice     AS firstChoice,"
                + "    a.second_choice    AS secondChoice,"
                + "    a.third_choice     AS thirdChoice,"
                + "    a.apply_date       AS applyDate"
                + "  FROM lms_class_apply a"
                + "  LEFT JOIN lms_student s ON s.id = a.student_id"
                + " WHERE a.class_apply_num = :applyNum";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("applyNum", applyNum);

        List<ClassApplyDto> list = jdbc.query(sql, params, new BeanPropertyRowMapper<>(ClassApplyDto.class));
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 화면에서 넘어온 정렬 키 → 실제 DB 컬럼 매핑
     */
    private String mapSortKey(String key) {
        if (key == null) return "a.apply_date";
        switch (key) {
            case "studentNickname": return "s.nickname";
            case "studentId":       return "a.student_id";
            case "startDate":       return "a.start_date";
            case "course":          return "a.course";
            case "firstChoice":     return "a.first_choice";
            case "secondChoice":    return "a.second_choice";
            case "thirdChoice":     return "a.third_choice";
            case "applyDate":       return "a.apply_date";
            default:                return "a.apply_date";
        }
    }
}

package com.english.lms.repository;

import com.english.lms.dto.TeacherRequestDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TeacherRequestRepositoryImpl implements TeacherRequestRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    /* ---------- 유틸 ---------- */
    private Integer getInt(Object o) {
        if (o == null) return null;
        if (o instanceof Number) return ((Number) o).intValue();
        return null;
    }
    private String getStr(Object o) { return o == null ? null : o.toString(); }
    private LocalDate getLocalDate(Object o) {
        if (o == null) return null;
        if (o instanceof java.sql.Date) return ((java.sql.Date) o).toLocalDate();
        if (o instanceof java.util.Date) return new java.sql.Date(((java.util.Date) o).getTime()).toLocalDate();
        return null;
    }
    private LocalDateTime getLocalDateTime(Object o) {
        if (o == null) return null;
        if (o instanceof Timestamp) return ((Timestamp) o).toLocalDateTime();
        if (o instanceof java.util.Date) return new Timestamp(((java.util.Date) o).getTime()).toLocalDateTime();
        return null;
    }
    private void setTimeToHourMinute(Time time, java.util.function.IntConsumer setHour, java.util.function.IntConsumer setMinute) {
        if (time != null) {
            setHour.accept(time.toLocalTime().getHour());
            setMinute.accept(time.toLocalTime().getMinute());
        }
    }

    /* ---------- 공통 SELECT / FROM (목록/검색 공용) ---------- */
    // s.id 또는 숫자 캐스팅 매칭 (tr.student_num 이 문자일 수 있어 두 경우 모두 대응)
    private static final String BASE_FROM_JOIN =
            " FROM lms_teacher_request tr " +
            " LEFT JOIN lms_student s " +
            "   ON (s.id = tr.student_num) " +
            "   OR ( (tr.student_num REGEXP '^[0-9]+$') AND s.student_num = CAST(tr.student_num AS UNSIGNED) ) ";

    // NOTE:
    // - start_month 컬럼이 DB에 없으므로 MONTH(tr.request_date)로 계산하여 별칭 제공
    // - responsibility 컬럼이 DB에 없으므로 NULL AS responsibility 로 안전 매핑
    // - teacher_a_week/b_week/c_week 컬럼이 DB에 없으므로 모두 NULL 별칭으로 매핑
    private static final String BASE_SELECT =
            " SELECT " +
            "   tr.request_num, tr.student_num, tr.week_days, tr.start_time, tr.end_time, " +
            "   MONTH(tr.request_date) AS start_month, NULL AS responsibility, " +
            "   tr.teacher_a_name, tr.teacher_b_name, tr.teacher_c_name, " +
            "   NULL AS teacher_a_week, NULL AS teacher_b_week, NULL AS teacher_c_week, " +
            "   tr.teacher_a_time, tr.teacher_b_time, tr.teacher_c_time, " +
            "   tr.request_date, " +
            "   s.nickname AS student_nickname ";

    /* ---------- 행 매핑 ---------- */
    private TeacherRequestDTO mapRowToDTO(Object[] r) {
        TeacherRequestDTO dto = new TeacherRequestDTO();
        int i = 0;

        dto.setRequestNum(getInt(r[i++]));                        // 0: request_num
        dto.setStudentNum(getStr(r[i++]));                        // 1: student_num
        dto.setWeekDays(getStr(r[i++]));                          // 2: week_days

        // 3: start_time, 4: end_time -> 시/분 분리 저장
        Time startTime = (Time) r[i++];
        Time endTime   = (Time) r[i++];
        setTimeToHourMinute(startTime, dto::setStartHour, dto::setStartMinute);
        setTimeToHourMinute(endTime,   dto::setEndHour,   dto::setEndMinute);

        // 5: start_month (계산값)
        dto.setStartMonth(getInt(r[i++]));

        // 6: responsibility -> 컬럼 없음: 항상 null
        Integer resp = getInt(r[i++]);
        dto.setResponsibility(resp != null ? (resp != 0) : null);

        // 7~9: teacher_a/b/c_name
        dto.setTeacherAName(getStr(r[i++]));
        dto.setTeacherBName(getStr(r[i++]));
        dto.setTeacherCName(getStr(r[i++]));

        // 10~12: teacher_a/b/c_week -> 컬럼 없음: null
        dto.setTeacherAWeek(getStr(r[i++]));
        dto.setTeacherBWeek(getStr(r[i++]));
        dto.setTeacherCWeek(getStr(r[i++]));

        // 13~15: teacher_a/b/c_time (TIME) -> "HH:mm"
        Time ta = (Time) r[i++];
        Time tb = (Time) r[i++];
        Time tc = (Time) r[i++];
        dto.setTeacherATime(ta != null ? ta.toLocalTime().toString() : null);
        dto.setTeacherBTime(tb != null ? tb.toLocalTime().toString() : null);
        dto.setTeacherCTime(tc != null ? tc.toLocalTime().toString() : null);

        // 16: request_date
        dto.setRequestDate(getLocalDateTime(r[i++]));

        // 17: student_nickname
        dto.setStudentNickname(getStr(r[i++]));

        // 화면 보조 필드
        dto.setStatus(null);
        dto.setClassDate(null);

        return dto;
    }

    /* ---------- 목록 (기간) ---------- */
    @Override
    public Page<TeacherRequestDTO> findTeacherRequestsByRange(LocalDate from, LocalDate to, Pageable pageable) {
        int limit = pageable.getPageSize();
        int offset = (int) pageable.getOffset();

        String where =
                " WHERE DATE(tr.request_date) BETWEEN :from AND :to ";

        String sql = BASE_SELECT + BASE_FROM_JOIN + where +
                " ORDER BY tr.request_date DESC, tr.request_num DESC " +
                " LIMIT :limit OFFSET :offset ";

        @SuppressWarnings("unchecked")
        List<Object[]> rows = em.createNativeQuery(sql)
                .setParameter("from", from)
                .setParameter("to", to)
                .setParameter("limit", limit)
                .setParameter("offset", offset)
                .getResultList();

        String countSql = "SELECT COUNT(*) " + BASE_FROM_JOIN + where;

        long total = ((Number) em.createNativeQuery(countSql)
                .setParameter("from", from)
                .setParameter("to", to)
                .getSingleResult()).longValue();

        List<TeacherRequestDTO> list = rows.stream().map(this::mapRowToDTO).collect(Collectors.toList());
        return new PageImpl<>(list, pageable, total);
    }

    /* ---------- 검색 (기간 + 키워드) ---------- */
    @Override
    public Page<TeacherRequestDTO> searchTeacherRequestsByRange(LocalDate from, LocalDate to, String keyword, Pageable pageable) {
        int limit = pageable.getPageSize();
        int offset = (int) pageable.getOffset();

        String where =
                " WHERE DATE(tr.request_date) BETWEEN :from AND :to " +
                "   AND ( " +
                "         tr.student_num LIKE :kw " +
                "      OR s.id LIKE :kw " +
                "      OR s.nickname LIKE :kw " +
                "      OR tr.teacher_a_name LIKE :kw " +
                "      OR tr.teacher_b_name LIKE :kw " +
                "      OR tr.teacher_c_name LIKE :kw " +
                "      OR tr.week_days LIKE :kw " +
                "     ) ";

        String sql = BASE_SELECT + BASE_FROM_JOIN + where +
                " ORDER BY tr.request_date DESC, tr.request_num DESC " +
                " LIMIT :limit OFFSET :offset ";

        @SuppressWarnings("unchecked")
        List<Object[]> rows = em.createNativeQuery(sql)
                .setParameter("from", from)
                .setParameter("to", to)
                .setParameter("kw", "%" + keyword + "%")
                .setParameter("limit", limit)
                .setParameter("offset", offset)
                .getResultList();

        String countSql = "SELECT COUNT(*) " + BASE_FROM_JOIN + where;

        long total = ((Number) em.createNativeQuery(countSql)
                .setParameter("from", from)
                .setParameter("to", to)
                .setParameter("kw", "%" + keyword + "%")
                .getSingleResult()).longValue();

        List<TeacherRequestDTO> list = rows.stream().map(this::mapRowToDTO).collect(Collectors.toList());
        return new PageImpl<>(list, pageable, total);
    }

    /* ---------- 상세 ---------- */
    @Override
    public TeacherRequestDTO findDetailByRequestNum(Integer requestNum) {
        String where = " WHERE tr.request_num = :reqNum ";

        String sql = BASE_SELECT + BASE_FROM_JOIN + where + " LIMIT 1";

        Object[] r = (Object[]) em.createNativeQuery(sql)
                .setParameter("reqNum", requestNum)
                .getSingleResult();

        return mapRowToDTO(r);
    }
}

package com.english.lms.repository;

import com.english.lms.dto.DayClassDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class DayClassRepositoryImpl implements DayClassRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private Integer getIntValue(Object obj) {
        if (obj == null) return null;
        if (obj instanceof Number) return ((Number) obj).intValue();
        if (obj instanceof Boolean) return (Boolean) obj ? 1 : 0;
        return null;
    }

    // 날짜 범위 전체 조회 (정규)
    @Override
    public Page<DayClassDTO> findAllRegularDayClassesWithDetailsByRange(LocalDate from, LocalDate to, Pageable pageable) {
        int limit = pageable.getPageSize();
        int offset = (int) pageable.getOffset();

        String sql =
            "SELECT d.day_class_num, d.student_num, d.teacher_num, d.class_num, d.class_type, d.class_name, d.class_date, d.start_time, d.end_time, d.zoom_link, d.zoom_meeting_id, d.attendance, d.absent, d.comment, d.progress, " +
            "s.id AS student_id, s.nickname AS student_nickname, s.nickname_jp, " +
            "t.id AS teacher_id, t.nickname AS teacher_nickname " +
            "FROM lms_day_class d " +
            "LEFT JOIN lms_student s ON d.student_num = s.student_num " +
            "LEFT JOIN lms_teacher t ON d.teacher_num = t.teacher_num " +
            "WHERE d.class_type = 'regular' AND d.class_date BETWEEN :from AND :to " +
            "ORDER BY d.class_date ASC, d.start_time ASC LIMIT :limit OFFSET :offset";

        List<Object[]> rows = entityManager.createNativeQuery(sql)
            .setParameter("from", from)
            .setParameter("to", to)
            .setParameter("limit", limit)
            .setParameter("offset", offset)
            .getResultList();

        long total = ((Number) entityManager
            .createNativeQuery("SELECT COUNT(*) FROM lms_day_class d WHERE d.class_type = 'regular' AND d.class_date BETWEEN :from AND :to")
            .setParameter("from", from)
            .setParameter("to", to)
            .getSingleResult()).longValue();

        List<DayClassDTO> dtoList = rows.stream().map(row -> {
            DayClassDTO dto = new DayClassDTO();
            dto.setDayClassNum((Integer) row[0]);
            dto.setStudentNum((Integer) row[1]);
            dto.setTeacherNum((Integer) row[2]);
            dto.setClassNum((Integer) row[3]);
            dto.setClassType((String) row[4]);
            dto.setClassName((String) row[5]);
            dto.setClassDate(row[6] != null ? ((java.sql.Date) row[6]).toLocalDate() : null);
            dto.setStartTime(row[7] != null ? ((Time) row[7]).toLocalTime() : null);
            dto.setEndTime(row[8] != null ? ((Time) row[8]).toLocalTime() : null);
            dto.setZoomLink((String) row[9]);
            dto.setZoomMeetingId((String) row[10]);
            dto.setAttendance(getIntValue(row[11]));
            dto.setAbsent(getIntValue(row[12]));
            dto.setComment((String) row[13]);
            dto.setProgress((String) row[14]);
            dto.setStudentId((String) row[15]);
            dto.setStudentNickname((String) row[16]);
            dto.setStudentNicknameJp((String) row[17]);
            dto.setTeacherId((String) row[18]);
            dto.setTeacherNickname((String) row[19]);
            return dto;
        }).collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, total);
    }

    // 날짜 범위 + 검색 (정규)
    @Override
    public Page<DayClassDTO> searchRegularDayClassesWithDetailsByRange(LocalDate from, LocalDate to, String keyword, Pageable pageable) {
        int limit = pageable.getPageSize();
        int offset = (int) pageable.getOffset();

        String sql =
            "SELECT d.day_class_num, d.student_num, d.teacher_num, d.class_num, d.class_type, d.class_name, d.class_date, d.start_time, d.end_time, d.zoom_link, d.zoom_meeting_id, d.attendance, d.absent, d.comment, d.progress, " +
            "s.id AS student_id, s.nickname AS student_nickname, s.nickname_jp, " +
            "t.id AS teacher_id, t.nickname AS teacher_nickname " +
            "FROM lms_day_class d " +
            "LEFT JOIN lms_student s ON d.student_num = s.student_num " +
            "LEFT JOIN lms_teacher t ON d.teacher_num = t.teacher_num " +
            "WHERE d.class_type = 'regular' AND d.class_date BETWEEN :from AND :to " +
            "AND (s.id LIKE :kw OR s.nickname LIKE :kw OR s.nickname_jp LIKE :kw OR t.nickname LIKE :kw OR d.class_name LIKE :kw) " +
            "ORDER BY d.class_date ASC, d.start_time ASC LIMIT :limit OFFSET :offset";

        List<Object[]> rows = entityManager.createNativeQuery(sql)
            .setParameter("from", from)
            .setParameter("to", to)
            .setParameter("kw", "%" + keyword + "%")
            .setParameter("limit", limit)
            .setParameter("offset", offset)
            .getResultList();

        String countSql = "SELECT COUNT(*) FROM lms_day_class d "
                + "LEFT JOIN lms_student s ON d.student_num = s.student_num "
                + "LEFT JOIN lms_teacher t ON d.teacher_num = t.teacher_num "
                + "WHERE d.class_type = 'regular' AND d.class_date BETWEEN :from AND :to "
                + "AND (s.id LIKE :kw OR s.nickname LIKE :kw OR s.nickname_jp LIKE :kw OR t.nickname LIKE :kw OR d.class_name LIKE :kw)";

        long total = ((Number) entityManager.createNativeQuery(countSql)
            .setParameter("from", from)
            .setParameter("to", to)
            .setParameter("kw", "%" + keyword + "%")
            .getSingleResult()).longValue();

        List<DayClassDTO> dtoList = rows.stream().map(row -> {
            DayClassDTO dto = new DayClassDTO();
            dto.setDayClassNum((Integer) row[0]);
            dto.setStudentNum((Integer) row[1]);
            dto.setTeacherNum((Integer) row[2]);
            dto.setClassNum((Integer) row[3]);
            dto.setClassType((String) row[4]);
            dto.setClassName((String) row[5]);
            dto.setClassDate(row[6] != null ? ((java.sql.Date) row[6]).toLocalDate() : null);
            dto.setStartTime(row[7] != null ? ((Time) row[7]).toLocalTime() : null);
            dto.setEndTime(row[8] != null ? ((Time) row[8]).toLocalTime() : null);
            dto.setZoomLink((String) row[9]);
            dto.setZoomMeetingId((String) row[10]);
            dto.setAttendance(getIntValue(row[11]));
            dto.setAbsent(getIntValue(row[12]));
            dto.setComment((String) row[13]);
            dto.setProgress((String) row[14]);
            dto.setStudentId((String) row[15]);
            dto.setStudentNickname((String) row[16]);
            dto.setStudentNicknameJp((String) row[17]);
            dto.setTeacherId((String) row[18]);
            dto.setTeacherNickname((String) row[19]);
            return dto;
        }).collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, total);
    }

    // 날짜 범위 전체조회 (비정규)
    @Override
    public Page<DayClassDTO> findAllIrregularDayClassesWithDetailsByRange(LocalDate from, LocalDate to, Pageable pageable) {
        int limit = pageable.getPageSize();
        int offset = (int) pageable.getOffset();

        String sql =
            "SELECT d.day_class_num, d.student_num, d.teacher_num, d.class_num, d.class_type, d.class_name, d.class_date, d.start_time, d.end_time, d.zoom_link, d.zoom_meeting_id, d.attendance, d.absent, d.comment, d.progress, " +
            "s.id AS student_id, s.nickname AS student_nickname, s.nickname_jp, " +
            "t.id AS teacher_id, t.nickname AS teacher_nickname " +
            "FROM lms_day_class d " +
            "LEFT JOIN lms_student s ON d.student_num = s.student_num " +
            "LEFT JOIN lms_teacher t ON d.teacher_num = t.teacher_num " +
            "WHERE d.class_type != 'regular' AND d.class_date BETWEEN :from AND :to " +
            "ORDER BY d.class_date ASC, d.start_time ASC LIMIT :limit OFFSET :offset";

        List<Object[]> rows = entityManager.createNativeQuery(sql)
            .setParameter("from", from)
            .setParameter("to", to)
            .setParameter("limit", limit)
            .setParameter("offset", offset)
            .getResultList();

        long total = ((Number) entityManager
            .createNativeQuery("SELECT COUNT(*) FROM lms_day_class d WHERE d.class_type != 'regular' AND d.class_date BETWEEN :from AND :to")
            .setParameter("from", from)
            .setParameter("to", to)
            .getSingleResult()).longValue();

        List<DayClassDTO> dtoList = rows.stream().map(row -> {
            DayClassDTO dto = new DayClassDTO();
            dto.setDayClassNum((Integer) row[0]);
            dto.setStudentNum((Integer) row[1]);
            dto.setTeacherNum((Integer) row[2]);
            dto.setClassNum((Integer) row[3]);
            dto.setClassType((String) row[4]);
            dto.setClassName((String) row[5]);
            dto.setClassDate(row[6] != null ? ((java.sql.Date) row[6]).toLocalDate() : null);
            dto.setStartTime(row[7] != null ? ((Time) row[7]).toLocalTime() : null);
            dto.setEndTime(row[8] != null ? ((Time) row[8]).toLocalTime() : null);
            dto.setZoomLink((String) row[9]);
            dto.setZoomMeetingId((String) row[10]);
            dto.setAttendance(getIntValue(row[11]));
            dto.setAbsent(getIntValue(row[12]));
            dto.setComment((String) row[13]);
            dto.setProgress((String) row[14]);
            dto.setStudentId((String) row[15]);
            dto.setStudentNickname((String) row[16]);
            dto.setStudentNicknameJp((String) row[17]);
            dto.setTeacherId((String) row[18]);
            dto.setTeacherNickname((String) row[19]);
            return dto;
        }).collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, total);
    }

    // 날짜 범위 + 검색 (비정규)
    @Override
    public Page<DayClassDTO> searchIrregularDayClassesWithDetailsByRange(LocalDate from, LocalDate to, String keyword, Pageable pageable) {
        int limit = pageable.getPageSize();
        int offset = (int) pageable.getOffset();

        String sql =
            "SELECT d.day_class_num, d.student_num, d.teacher_num, d.class_num, d.class_type, d.class_name, d.class_date, d.start_time, d.end_time, d.zoom_link, d.zoom_meeting_id, d.attendance, d.absent, d.comment, d.progress, " +
            "s.id AS student_id, s.nickname AS student_nickname, s.nickname_jp, " +
            "t.id AS teacher_id, t.nickname AS teacher_nickname " +
            "FROM lms_day_class d " +
            "LEFT JOIN lms_student s ON d.student_num = s.student_num " +
            "LEFT JOIN lms_teacher t ON d.teacher_num = t.teacher_num " +
            "WHERE d.class_type != 'regular' AND d.class_date BETWEEN :from AND :to " +
            "AND (s.id LIKE :kw OR s.nickname LIKE :kw OR s.nickname_jp LIKE :kw OR t.nickname LIKE :kw OR d.class_name LIKE :kw) " +
            "ORDER BY d.class_date ASC, d.start_time ASC LIMIT :limit OFFSET :offset";

        List<Object[]> rows = entityManager.createNativeQuery(sql)
            .setParameter("from", from)
            .setParameter("to", to)
            .setParameter("kw", "%" + keyword + "%")
            .setParameter("limit", limit)
            .setParameter("offset", offset)
            .getResultList();

        String countSql = "SELECT COUNT(*) FROM lms_day_class d "
                + "LEFT JOIN lms_student s ON d.student_num = s.student_num "
                + "LEFT JOIN lms_teacher t ON d.teacher_num = t.teacher_num "
                + "WHERE d.class_type != 'regular' AND d.class_date BETWEEN :from AND :to "
                + "AND (s.id LIKE :kw OR s.nickname LIKE :kw OR s.nickname_jp LIKE :kw OR t.nickname LIKE :kw OR d.class_name LIKE :kw)";

        long total = ((Number) entityManager.createNativeQuery(countSql)
            .setParameter("from", from)
            .setParameter("to", to)
            .setParameter("kw", "%" + keyword + "%")
            .getSingleResult()).longValue();

        List<DayClassDTO> dtoList = rows.stream().map(row -> {
            DayClassDTO dto = new DayClassDTO();
            dto.setDayClassNum((Integer) row[0]);
            dto.setStudentNum((Integer) row[1]);
            dto.setTeacherNum((Integer) row[2]);
            dto.setClassNum((Integer) row[3]);
            dto.setClassType((String) row[4]);
            dto.setClassName((String) row[5]);
            dto.setClassDate(row[6] != null ? ((java.sql.Date) row[6]).toLocalDate() : null);
            dto.setStartTime(row[7] != null ? ((Time) row[7]).toLocalTime() : null);
            dto.setEndTime(row[8] != null ? ((Time) row[8]).toLocalTime() : null);
            dto.setZoomLink((String) row[9]);
            dto.setZoomMeetingId((String) row[10]);
            dto.setAttendance(getIntValue(row[11]));
            dto.setAbsent(getIntValue(row[12]));
            dto.setComment((String) row[13]);
            dto.setProgress((String) row[14]);
            dto.setStudentId((String) row[15]);
            dto.setStudentNickname((String) row[16]);
            dto.setStudentNicknameJp((String) row[17]);
            dto.setTeacherId((String) row[18]);
            dto.setTeacherNickname((String) row[19]);
            return dto;
        }).collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, total);
    }

    // 단일 날짜 전체조회 (정규)
    @Override
    public Page<DayClassDTO> findAllRegularDayClassesWithDetailsByDaily(LocalDate classDate, Pageable pageable) {
        return findAllRegularDayClassesWithDetailsByRange(classDate, classDate, pageable);
    }

    // 단일 날짜+검색 (정규)
    @Override
    public Page<DayClassDTO> searchRegularDayClassesWithDetailsByDaily(LocalDate classDate, String keyword, Pageable pageable) {
        return searchRegularDayClassesWithDetailsByRange(classDate, classDate, keyword, pageable);
    }
}

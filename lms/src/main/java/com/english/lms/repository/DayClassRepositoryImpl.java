package com.english.lms.repository;

import com.english.lms.dto.DayClassDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

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

    /** ---------------- [공통] 날짜 범위 조회 (type) ---------------- */
    public Page<DayClassDTO> findAllDayClassesWithDetailsByRange(LocalDate from, LocalDate to, String type, Pageable pageable) {
        int limit = pageable.getPageSize();
        int offset = (int) pageable.getOffset();

        String typeCondition = ("regular".equals(type)) ? "d.class_type = 'regular'" : "d.class_type != 'regular'";

        String sql =
                "SELECT d.day_class_num, d.student_num, d.teacher_num, d.class_num, d.class_type, d.class_name, d.class_date, d.start_time, d.end_time, d.zoom_link, d.zoom_meeting_id, d.attendance, d.absent, d.comment, d.progress, " +
                "s.id AS student_id, s.nickname AS student_nickname, s.nickname_jp, " +
                "t.id AS teacher_id, t.nickname AS teacher_nickname " +
                "FROM lms_day_class d " +
                "LEFT JOIN lms_student s ON d.student_num = s.student_num " +
                "LEFT JOIN lms_teacher t ON d.teacher_num = t.teacher_num " +
                "WHERE " + typeCondition + " AND d.class_date BETWEEN :from AND :to " +
                "ORDER BY d.class_date ASC, d.start_time ASC LIMIT :limit OFFSET :offset";

        List<Object[]> rows = entityManager.createNativeQuery(sql)
                .setParameter("from", from)
                .setParameter("to", to)
                .setParameter("limit", limit)
                .setParameter("offset", offset)
                .getResultList();

        String countSql = "SELECT COUNT(*) FROM lms_day_class d WHERE " + typeCondition + " AND d.class_date BETWEEN :from AND :to";

        long total = ((Number) entityManager.createNativeQuery(countSql)
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

    /** [공통] 날짜 범위 + 검색 (type) */
    public Page<DayClassDTO> searchDayClassesWithDetailsByRange(LocalDate from, LocalDate to, String keyword, String type, Pageable pageable) {
        int limit = pageable.getPageSize();
        int offset = (int) pageable.getOffset();

        String typeCondition = ("regular".equals(type)) ? "d.class_type = 'regular'" : "d.class_type != 'regular'";

        String sql =
                "SELECT d.day_class_num, d.student_num, d.teacher_num, d.class_num, d.class_type, d.class_name, d.class_date, d.start_time, d.end_time, d.zoom_link, d.zoom_meeting_id, d.attendance, d.absent, d.comment, d.progress, " +
                "s.id AS student_id, s.nickname AS student_nickname, s.nickname_jp, " +
                "t.id AS teacher_id, t.nickname AS teacher_nickname " +
                "FROM lms_day_class d " +
                "LEFT JOIN lms_student s ON d.student_num = s.student_num " +
                "LEFT JOIN lms_teacher t ON d.teacher_num = t.teacher_num " +
                "WHERE " + typeCondition + " AND d.class_date BETWEEN :from AND :to " +
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
                + "WHERE " + typeCondition + " AND d.class_date BETWEEN :from AND :to "
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

    // [공통] 단일 날짜 전체/검색 (type)
    public Page<DayClassDTO> findAllDayClassesWithDetailsByDaily(LocalDate classDate, String type, Pageable pageable) {
        return findAllDayClassesWithDetailsByRange(classDate, classDate, type, pageable);
    }

    public Page<DayClassDTO> searchDayClassesWithDetailsByDaily(LocalDate classDate, String keyword, String type, Pageable pageable) {
        return searchDayClassesWithDetailsByRange(classDate, classDate, keyword, type, pageable);
    }

    /** [공통] 단일건 상세조회 (type) */
    public DayClassDTO findDayClassDetailByDayClassNum(Integer dayClassNum, String type) {
        String typeCondition = ("regular".equals(type)) ? "d.class_type = 'regular'" : "d.class_type != 'regular'";
        String sql =
                "SELECT d.day_class_num, d.student_num, d.teacher_num, d.class_num, d.class_type, d.class_name, d.class_date, d.start_time, d.end_time, d.zoom_link, d.zoom_meeting_id, d.attendance, d.absent, d.comment, d.progress, " +
                "s.id AS student_id, s.nickname AS student_nickname, s.nickname_jp, " +
                "t.id AS teacher_id, t.nickname AS teacher_nickname, " +
                "s.english_level, s.age " +
                "FROM lms_day_class d " +
                "LEFT JOIN lms_student s ON d.student_num = s.student_num " +
                "LEFT JOIN lms_teacher t ON d.teacher_num = t.teacher_num " +
                "WHERE d.day_class_num = :dayClassNum AND " + typeCondition;

        Object[] row = (Object[]) entityManager.createNativeQuery(sql)
                .setParameter("dayClassNum", dayClassNum)
                .getSingleResult();

        if (row == null) return null;
        DayClassDTO dto = new DayClassDTO();
        dto.setDayClassNum((Integer) row[0]);
        dto.setStudentNum((Integer) row[1]);
        dto.setTeacherNum((Integer) row[2]);
        dto.setClassNum((Integer) row[3]);
        dto.setClassType((String) row[4]);
        dto.setClassName((String) row[5]);
        dto.setClassDate(row[6] != null ? ((java.sql.Date) row[6]).toLocalDate() : null);
        dto.setStartTime(row[7] != null ? ((java.sql.Time) row[7]).toLocalTime() : null);
        dto.setEndTime(row[8] != null ? ((java.sql.Time) row[8]).toLocalTime() : null);
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
        dto.setStudentEnglishLevel(row[20] != null ? row[20].toString() : null);
        dto.setStudentAge(row[21] != null ? row[21].toString() : null);
        return dto;
    }

    /** [공통] 단일건 수정 (type) */
    @Transactional
    public void updateDayClassByDayClassNum(DayClassDTO dto, String type) {
        String typeCondition = ("regular".equals(type)) ? "class_type = 'regular'" : "class_type != 'regular'";
        String sql =
                "UPDATE lms_day_class SET " +
                "teacher_num = :teacherNum, " +
                "class_name = :className, " +
                "class_date = :classDate, " +
                "start_time = :startTime, " +
                "end_time = :endTime, " +
                "zoom_link = :zoomLink " +
                "WHERE day_class_num = :dayClassNum AND " + typeCondition;

        entityManager.createNativeQuery(sql)
                .setParameter("teacherNum", dto.getTeacherNum())
                .setParameter("className", dto.getClassName())
                .setParameter("classDate", dto.getClassDate())
                .setParameter("startTime", dto.getStartTime())
                .setParameter("endTime", dto.getEndTime())
                .setParameter("zoomLink", dto.getZoomLink())
                .setParameter("dayClassNum", dto.getDayClassNum())
                .executeUpdate();
    }

    /** [공통] 단일건 삭제 (type) */
    @Transactional
    public void deleteDayClassByDayClassNum(Integer dayClassNum, String type) {
        String typeCondition = ("regular".equals(type)) ? "class_type = 'regular'" : "class_type != 'regular'";
        String sql = "DELETE FROM lms_day_class WHERE day_class_num = :dayClassNum AND " + typeCondition;
        entityManager.createNativeQuery(sql)
                .setParameter("dayClassNum", dayClassNum)
                .executeUpdate();
    }

    /** --------------------------------------------------------
     *  이하 기존 메서드(정규/비정규)는 공통 메서드로 위임
     * -------------------------------------------------------- */

    // 기존 정규/비정규 메서드는 아래처럼 위임
    @Override
    public Page<DayClassDTO> findAllRegularDayClassesWithDetailsByRange(LocalDate from, LocalDate to, Pageable pageable) {
        return findAllDayClassesWithDetailsByRange(from, to, "regular", pageable);
    }
    @Override
    public Page<DayClassDTO> findAllIrregularDayClassesWithDetailsByRange(LocalDate from, LocalDate to, Pageable pageable) {
        return findAllDayClassesWithDetailsByRange(from, to, "irregular", pageable);
    }
    @Override
    public Page<DayClassDTO> searchRegularDayClassesWithDetailsByRange(LocalDate from, LocalDate to, String keyword, Pageable pageable) {
        return searchDayClassesWithDetailsByRange(from, to, keyword, "regular", pageable);
    }
    @Override
    public Page<DayClassDTO> searchIrregularDayClassesWithDetailsByRange(LocalDate from, LocalDate to, String keyword, Pageable pageable) {
        return searchDayClassesWithDetailsByRange(from, to, keyword, "irregular", pageable);
    }
    @Override
    public DayClassDTO findRegularDayClassDetailByDayClassNum(Integer dayClassNum) {
        return findDayClassDetailByDayClassNum(dayClassNum, "regular");
    }
    @Override
    public DayClassDTO findIrregularDayClassDetailByDayClassNum(Integer dayClassNum) {
        return findDayClassDetailByDayClassNum(dayClassNum, "irregular");
    }
    @Override
    @Transactional
    public void updateRegularDayClassByDayClassNum(DayClassDTO dto) {
        updateDayClassByDayClassNum(dto, "regular");
    }
    @Override
    @Transactional
    public void updateIrregularDayClassByDayClassNum(DayClassDTO dto) {
        updateDayClassByDayClassNum(dto, "irregular");
    }
    @Override
    @Transactional
    public void deleteRegularDayClassByDayClassNum(Integer dayClassNum) {
        deleteDayClassByDayClassNum(dayClassNum, "regular");
    }
    @Override
    @Transactional
    public void deleteIrregularDayClassByDayClassNum(Integer dayClassNum) {
        deleteDayClassByDayClassNum(dayClassNum, "irregular");
    }
}

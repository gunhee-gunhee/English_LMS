package com.english.lms.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.english.lms.dto.TeacherMyPageDTO;

@Repository
public class TeacherMyPageRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<TeacherMyPageDTO> getTeacherSchedule(int teacherNum, String date) {
        String sql = "SELECT dc.day_class_num, dc.start_time, dc.end_time, " +
                "s.nickname AS student_name, s.nickname_jp AS student_name_jp, " +
                "dc.class_type, dc.class_name, dc.absent, dc.attendance, " +
                "dc.zoom_link " +    // zoom_link 컬럼 SELECT
                "FROM lms_day_class dc " +
                "JOIN lms_class c ON dc.class_num = c.class_num " +
                "JOIN lms_student s ON c.student_num = s.student_num " +
                "WHERE c.teacher_num = ? AND dc.class_date = ? " +
                "ORDER BY dc.start_time";
        return jdbcTemplate.query(
                sql,
                new Object[]{teacherNum, date},
                new BeanPropertyRowMapper<>(TeacherMyPageDTO.class)
        );
    }
}


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
        String sql = """
            SELECT
                dc.day_class_num,
                dc.start_time,
                dc.end_time,
                COALESCE(s.student_num, s2.student_num) AS student_num,
                COALESCE(s.nickname, s2.nickname) AS student_name,
                COALESCE(s.nickname_jp, s2.nickname_jp) AS student_name_jp,
                dc.class_type,
                dc.class_name,
                dc.absent,
                dc.attendance,
                dc.zoom_link
            FROM lms_day_class dc
            LEFT JOIN lms_class c ON dc.class_num = c.class_num
            LEFT JOIN lms_student s ON c.student_num = s.student_num
            LEFT JOIN lms_student s2 ON dc.student_num = s2.student_num
            WHERE dc.teacher_num = ? AND dc.class_date = ?
            ORDER BY dc.start_time
        """;
        return jdbcTemplate.query(
            sql,
            new Object[]{teacherNum, date},
            new BeanPropertyRowMapper<>(TeacherMyPageDTO.class)
        );
    }


}


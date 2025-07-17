package com.english.lms.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.english.lms.dto.StudentDetailDTO;

@Repository
public class StudentDetailRepository {

    private final JdbcTemplate jdbcTemplate;

    public StudentDetailRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public StudentDetailDTO findStudentDetail(int studentNum) {
        String sql = "SELECT student_num, nickname, nickname_jp, age, english_level, english_purpose " +
                     "FROM lms_student WHERE student_num = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{studentNum}, studentDetailMapper);
    }

    private final RowMapper<StudentDetailDTO> studentDetailMapper = (rs, rowNum) -> {
        StudentDetailDTO dto = new StudentDetailDTO();
        dto.setStudentNum(rs.getInt("student_num"));
        dto.setNickname(rs.getString("nickname"));
        dto.setNicknameJp(rs.getString("nickname_jp"));
        dto.setAge(rs.getString("age"));
        dto.setEnglishLevel((Integer) rs.getObject("english_level")); // nullable 처리
        dto.setEnglishPurpose(rs.getString("english_purpose"));
        return dto;
    };
}

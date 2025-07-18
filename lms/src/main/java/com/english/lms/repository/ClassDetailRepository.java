package com.english.lms.repository;

import com.english.lms.dto.ClassDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ClassDetailRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ClassDetailDTO getClassDetail(int dayClassNum) {
    	 String sql = """
    		        SELECT 
    		            dc.day_class_num AS dayClassNum,
    		            dc.class_date AS classDate,
    		            DAYNAME(dc.class_date) AS weekDay,
    		            COALESCE(s.nickname, '') AS studentName,
    		            COALESCE(s.nickname_jp, '') AS studentNameJp,
    		            dc.class_type AS classType,
    		            c.text_num AS textNum,
    		            dc.comment,
    		            dc.progress,
    		            t.name
    		        FROM lms_day_class dc
    		        LEFT JOIN lms_class c ON dc.class_num = c.class_num
    		        LEFT JOIN lms_student s ON 
    		            (CASE 
    		                WHEN dc.class_num IS NOT NULL THEN c.student_num 
    		                ELSE dc.student_num
    		            END) = s.student_num
    		        LEFT JOIN lms_text t ON c.text_num = t.text_num
    		        WHERE dc.day_class_num = ?
    		    """;
    		    return jdbcTemplate.queryForObject(
    		        sql,
    		        new Object[]{dayClassNum},
    		        new BeanPropertyRowMapper<>(ClassDetailDTO.class)
    		    );
    		}

    public int updateCommentProgress(int dayClassNum, String comment, String progress) {
        String sql = "UPDATE lms_day_class SET comment=?, progress=? WHERE day_class_num=?";
        return jdbcTemplate.update(sql, comment, progress, dayClassNum);
    }
}

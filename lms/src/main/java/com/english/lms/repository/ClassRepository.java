
package com.english.lms.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.english.lms.entity.ClassEntity;

public interface ClassRepository extends JpaRepository<ClassEntity, Integer> {
    
	List<ClassEntity> findByStudentNum(Integer studentNum);
    
    @Query("SELECT c.teacherNum FROM ClassEntity c WHERE c.studentNum = :studentNum")
    Integer findTeacherNumByStudentNum(@Param("studentNum") Integer studentNum);

    //該当学生のclassEntity取得
    @Query(
    		value= "SELECT * FROM lms_class WHERE student_num = :studentNum lIMIT 1",
    		nativeQuery = true 
    		)
	ClassEntity findByStudentNumQuery(@Param("studentNum") Integer studentNum);


}
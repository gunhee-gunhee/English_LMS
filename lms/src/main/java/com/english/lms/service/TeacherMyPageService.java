package com.english.lms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.english.lms.dto.TeacherMyPageDTO;
import com.english.lms.repository.TeacherMyPageRepository;
import com.english.lms.repository.TeacherRepository;

@Service
public class TeacherMyPageService {
    @Autowired
    private TeacherMyPageRepository scheduleRepository;

    @Autowired
    private TeacherRepository teacherRepository; // teacherId → PK 

    // teacherId → teacherNum
    public int getTeacherNumById(String teacherId) {
        return teacherRepository.findByTeacherId(teacherId)
                .orElseThrow(() -> new RuntimeException("講師無し"))
                .getTeacherNum();
    }

    public List<TeacherMyPageDTO> getSchedule(int teacherNum, String date) {
        return scheduleRepository.getTeacherSchedule(teacherNum, date);
    }
}


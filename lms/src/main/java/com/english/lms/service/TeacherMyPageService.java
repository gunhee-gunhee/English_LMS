package com.english.lms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.english.lms.dto.TeacherMyPageDTO;
import com.english.lms.repository.TeacherMyPageRepository;

@Service
public class TeacherMyPageService {

    @Autowired
    private TeacherMyPageRepository scheduleRepository;

    public List<TeacherMyPageDTO> getSchedule(int teacherNum, String date) {
        return scheduleRepository.getTeacherSchedule(teacherNum, date);
    }
}

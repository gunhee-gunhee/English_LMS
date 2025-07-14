package com.english.lms.service;

import com.english.lms.entity.DayClassEntity;
import com.english.lms.repository.DayClassRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentMyPageService {

    private final DayClassRepository dayClassRepository;

    // 欠席/欠席取消
    @Transactional
    public void setAbsent(Integer dayClassNum, boolean absent) {
        DayClassEntity entity = dayClassRepository.findById(dayClassNum)
            .orElseThrow(() -> new IllegalArgumentException("授業が存在しません。"));
        entity.setAbsent(absent ? "1" : "0"); // "1"=欠席, "0"=欠席取消
        dayClassRepository.save(entity);
    }

    // 出席 처리 (参加ボタン)
    @Transactional
    public void setAttendance(Integer dayClassNum, boolean attendance) {
        DayClassEntity entity = dayClassRepository.findById(dayClassNum)
            .orElseThrow(() -> new IllegalArgumentException("授業が存在しません。"));
        entity.setAttendance(attendance); // attendance가 Boolean 타입이면 true/false, int면 1/0
        dayClassRepository.save(entity);
    }
}

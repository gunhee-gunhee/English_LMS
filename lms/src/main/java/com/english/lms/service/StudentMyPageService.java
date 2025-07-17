package com.english.lms.service;

import com.english.lms.entity.DayClassEntity;
import com.english.lms.entity.PointEntity;
import com.english.lms.repository.DayClassRepository;
import com.english.lms.repository.PointRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class StudentMyPageService {

    private final DayClassRepository dayClassRepository;
    private final PointRepository pointRepository;

    // 결석/결석취소
    @Transactional
    public void setAbsent(Integer dayClassNum, boolean absent) {
        DayClassEntity entity = dayClassRepository.findById(dayClassNum)
            .orElseThrow(() -> new IllegalArgumentException("授業が存在しません。"));

        entity.setAbsent(absent ? "1" : "0");
        dayClassRepository.save(entity);

        Integer studentNum = entity.getStudentNum();
        LocalDate classDate = entity.getClassDate(); // 수업날짜

        if (absent) {
            // 결석 포인트 지급: absent_date에 해당 row를 찾아 +1, 없으면 새로 insert
            PointEntity point = pointRepository.findByStudentNumAndAbsentDate(studentNum, classDate)
                    .orElse(PointEntity.builder()
                            .studentNum(studentNum)
                            .absentDate(classDate)
                            .pointAmount(0)
                            .build());

            // +1 포인트 적립
            point.setPointAmount(point.getPointAmount() + 1);
            point.setCreatedAt(LocalDate.now());
            point.setExpiresAt(LocalDate.now().plusDays(30));
            point.setType("additional");   // 추가된 부분

            pointRepository.save(point);

        } else {
            // 결석취소: absent_date에 해당하는 row에서 -1
            pointRepository.findByStudentNumAndAbsentDate(studentNum, classDate)
                .ifPresent(point -> {
                    point.setPointAmount(point.getPointAmount() - 1);
                    point.setType("additional");   // 결석취소도 type을 'additional'로 유지
                    pointRepository.save(point);
                });
        }
    }

    // 출석 처리 (변경 없음)
    @Transactional
    public void setAttendance(Integer dayClassNum, boolean attendance) {
        DayClassEntity entity = dayClassRepository.findById(dayClassNum)
            .orElseThrow(() -> new IllegalArgumentException("授業が存在しません。"));
        entity.setAttendance(attendance);
        dayClassRepository.save(entity);
    }
}

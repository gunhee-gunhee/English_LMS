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

    // 결석/결석취소 (return값으로 성공/실패 구분)
    @Transactional
    public boolean setAbsent(Integer dayClassNum, boolean absent) {
        DayClassEntity entity = dayClassRepository.findById(dayClassNum)
            .orElseThrow(() -> new IllegalArgumentException("授業が存在しません。"));

        // 수정: String -> Integer
        entity.setAbsent(absent ? 1 : 0);
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
                            .type("additional")
                            .build());

            point.setPointAmount(point.getPointAmount() + 1);
            point.setCreatedAt(LocalDate.now());
            point.setExpiresAt(LocalDate.now().plusDays(30));
            point.setType("additional");

            pointRepository.save(point);
            return true;

        } else {
            // 결석취소: absent_date에 해당하는 row에서 -1, 없으면 이미 사용된 것임
            var opt = pointRepository.findByStudentNumAndAbsentDate(studentNum, classDate);
            if (opt.isEmpty()) {
                return false; // 이미 사용됨
            }
            PointEntity point = opt.get();
            if (point.getPointAmount() <= 0) {
                return false; // 이미 사용됨
            }
            point.setPointAmount(point.getPointAmount() - 1);
            if (point.getPointAmount() <= 0) {
                pointRepository.delete(point);
            } else {
                pointRepository.save(point);
            }
            return true;
        }
    }

    // 출석 처리 (변경: boolean -> Integer)
    @Transactional
    public void setAttendance(Integer dayClassNum, boolean attendance) {
        DayClassEntity entity = dayClassRepository.findById(dayClassNum)
            .orElseThrow(() -> new IllegalArgumentException("授業が存在しません。"));
        // 수정: boolean -> Integer
        entity.setAttendance(attendance ? 1 : 0);
        dayClassRepository.save(entity);
    }
}

package com.english.lms.service;

import com.english.lms.dto.ClassApplyDto;
import com.english.lms.entity.ClassApplyEntity;
import com.english.lms.repository.ClassApplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassApplyService {

    private final ClassApplyRepository classApplyRepository;

    /**
     * 신청 처리
     *
     * @param dto        사용자가 입력한 신청 내용
     * @param studentId  로그인한 사용자 ID
     */
    public void apply(ClassApplyDto dto) {
        ClassApplyEntity entity = ClassApplyEntity.builder()
                .studentId(dto.getStudentId()) // 여기로 변경
                .startDate(dto.getStartDate())
                .course(dto.getCourse())
                .firstChoice(dto.getFirstChoice())
                .secondChoice(dto.getSecondChoice())
                .thirdChoice(dto.getThirdChoice())
                .build();

        classApplyRepository.save(entity);
    }
}

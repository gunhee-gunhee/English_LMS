package com.english.lms.repository;

import com.english.lms.dto.DayClassDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;

public interface DayClassRepositoryCustom {
    // === 공통 메서드 (type 파라미터 추가) ===
    Page<DayClassDTO> findAllDayClassesWithDetailsByRange(LocalDate from, LocalDate to, String type, Pageable pageable);
    Page<DayClassDTO> searchDayClassesWithDetailsByRange(LocalDate from, LocalDate to, String keyword, String type, Pageable pageable);

    Page<DayClassDTO> findAllDayClassesWithDetailsByDaily(LocalDate classDate, String type, Pageable pageable);
    Page<DayClassDTO> searchDayClassesWithDetailsByDaily(LocalDate classDate, String keyword, String type, Pageable pageable);

    DayClassDTO findDayClassDetailByDayClassNum(Integer dayClassNum, String type);

    void updateDayClassByDayClassNum(DayClassDTO dto, String type);
    void deleteDayClassByDayClassNum(Integer dayClassNum, String type);

    // === 기존 정규/비정규 메서드도 아래처럼 래핑(default 메서드, 또는 구현체에서 바로 호출) 가능 ===

    // ----- [정규수업 메서드] -----
    default Page<DayClassDTO> findAllRegularDayClassesWithDetailsByRange(LocalDate from, LocalDate to, Pageable pageable) {
        return findAllDayClassesWithDetailsByRange(from, to, "regular", pageable);
    }
    default Page<DayClassDTO> searchRegularDayClassesWithDetailsByRange(LocalDate from, LocalDate to, String keyword, Pageable pageable) {
        return searchDayClassesWithDetailsByRange(from, to, keyword, "regular", pageable);
    }
    default Page<DayClassDTO> findAllRegularDayClassesWithDetailsByDaily(LocalDate classDate, Pageable pageable) {
        return findAllDayClassesWithDetailsByDaily(classDate, "regular", pageable);
    }
    default Page<DayClassDTO> searchRegularDayClassesWithDetailsByDaily(LocalDate classDate, String keyword, Pageable pageable) {
        return searchDayClassesWithDetailsByDaily(classDate, keyword, "regular", pageable);
    }
    default DayClassDTO findRegularDayClassDetailByDayClassNum(Integer dayClassNum) {
        return findDayClassDetailByDayClassNum(dayClassNum, "regular");
    }
    default void updateRegularDayClassByDayClassNum(DayClassDTO dto) {
        updateDayClassByDayClassNum(dto, "regular");
    }
    default void deleteRegularDayClassByDayClassNum(Integer dayClassNum) {
        deleteDayClassByDayClassNum(dayClassNum, "regular");
    }

    // ----- [비정규수업 메서드] -----
    default Page<DayClassDTO> findAllIrregularDayClassesWithDetailsByRange(LocalDate from, LocalDate to, Pageable pageable) {
        return findAllDayClassesWithDetailsByRange(from, to, "irregular", pageable);
    }
    default Page<DayClassDTO> searchIrregularDayClassesWithDetailsByRange(LocalDate from, LocalDate to, String keyword, Pageable pageable) {
        return searchDayClassesWithDetailsByRange(from, to, keyword, "irregular", pageable);
    }
    default Page<DayClassDTO> findAllIrregularDayClassesWithDetailsByDaily(LocalDate classDate, Pageable pageable) {
        return findAllDayClassesWithDetailsByDaily(classDate, "irregular", pageable);
    }
    default Page<DayClassDTO> searchIrregularDayClassesWithDetailsByDaily(LocalDate classDate, String keyword, Pageable pageable) {
        return searchDayClassesWithDetailsByDaily(classDate, keyword, "irregular", pageable);
    }
    default DayClassDTO findIrregularDayClassDetailByDayClassNum(Integer dayClassNum) {
        return findDayClassDetailByDayClassNum(dayClassNum, "irregular");
    }
    default void updateIrregularDayClassByDayClassNum(DayClassDTO dto) {
        updateDayClassByDayClassNum(dto, "irregular");
    }
    default void deleteIrregularDayClassByDayClassNum(Integer dayClassNum) {
        deleteDayClassByDayClassNum(dayClassNum, "irregular");
    }
}

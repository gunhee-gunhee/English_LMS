package com.english.lms.api;

import com.english.lms.entity.DayOffEntity;
import com.english.lms.repository.DayOffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class DayOffApiController {

    private final DayOffRepository dayOffRepository;

    @GetMapping("/api/dayoff")
    public List<Map<String, String>> getDayOffList(
            @RequestParam(value = "start", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(value = "end", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        // 기본값: 올해 전체
        if (start == null) start = LocalDate.now().withDayOfYear(1);
        if (end == null) end = LocalDate.now().withDayOfYear(LocalDate.now().lengthOfYear());

        List<DayOffEntity> list = dayOffRepository.findByOffDateBetween(
                java.sql.Date.valueOf(start),
                java.sql.Date.valueOf(end)
        );

        List<Map<String, String>> result = new ArrayList<>();
        for (DayOffEntity e : list) {
            Map<String, String> map = new HashMap<>();
            map.put("date", e.getOffDate().toLocalDate().toString()); // yyyy-MM-dd
            map.put("name", e.getName());
            result.add(map);
        }
        return result;
    }
}

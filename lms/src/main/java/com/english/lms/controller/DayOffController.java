package com.english.lms.controller;

import com.english.lms.entity.DayOffEntity;
import com.english.lms.repository.DayOffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/dayoff")
@RequiredArgsConstructor
public class DayOffController {

    private final DayOffRepository dayOffRepository;

    @GetMapping("/list")
    public String list(
            @RequestParam(value = "year", defaultValue = "#{T(java.time.LocalDate).now().year}") int year,
            @RequestParam(value = "editId", required = false) Integer editId,
            Model model) {
        List<DayOffEntity> dayOffList = dayOffRepository.findByYear(year);
        model.addAttribute("year", year);
        model.addAttribute("dayOffList", dayOffList);
        model.addAttribute("editId", editId);
        return "admin/dayoff"; // Thymeleaf 템플릿 이름
    }

    @PostMapping("/add")
    public String add(
            @RequestParam(value = "year") int year,
            @RequestParam(value = "month") int month,
            @RequestParam(value = "day") int day,
            @RequestParam(value = "name") String name) {
        LocalDate localDate = LocalDate.of(year, month, day);
        Date sqlDate = Date.valueOf(localDate);
        DayOffEntity dayOff = DayOffEntity.builder()
                .offDate(sqlDate)
                .name(name)
                .build();
        dayOffRepository.save(dayOff);
        return "redirect:/admin/dayoff/list?year=" + year;
    }

    @PostMapping("/update")
    public String update(
            @RequestParam(value = "dayOffNum") Integer dayOffNum,
            @RequestParam(value = "month") int month,
            @RequestParam(value = "day") int day,
            @RequestParam(value = "name") String name) {
        DayOffEntity dayOff = dayOffRepository.findById(dayOffNum).orElseThrow();
        LocalDate origin = dayOff.getOffDate().toLocalDate();
        LocalDate updatedDate = LocalDate.of(origin.getYear(), month, day);
        dayOff.setOffDate(Date.valueOf(updatedDate));
        dayOff.setName(name);
        dayOffRepository.save(dayOff);
        int year = updatedDate.getYear();
        return "redirect:/admin/dayoff/list?year=" + year;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        DayOffEntity dayOff = dayOffRepository.findById(id).orElseThrow();
        int year = dayOff.getOffDate().toLocalDate().getYear();
        dayOffRepository.deleteById(id);
        return "redirect:/admin/dayoff/list?year=" + year;
    }

    @PostMapping("/importLastYear")
    public String importLastYear(@RequestParam(value = "year") int year) {
        List<DayOffEntity> lastYearList = dayOffRepository.findAllByYear(year - 1);
        for (DayOffEntity old : lastYearList) {
            LocalDate newDate = old.getOffDate().toLocalDate().plusYears(1);
            DayOffEntity copy = DayOffEntity.builder()
                    .offDate(Date.valueOf(newDate))
                    .name(old.getName())
                    .build();
            dayOffRepository.save(copy);
        }
        return "redirect:/admin/dayoff/list?year=" + year;
    }
}

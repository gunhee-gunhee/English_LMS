package com.english.lms.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.english.lms.dto.TeacherDTO;
import com.english.lms.dto.TeacherScheduleDTO;
import com.english.lms.entity.TeacherEntity;
import com.english.lms.entity.TeacherScheduleEntity;
import com.english.lms.enums.Role;
import com.english.lms.repository.TeacherRepository;
import com.english.lms.repository.TeacherScheduleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherScheduleRepository teacherScheduleRepository;
    private final PasswordEncoder passwordEncoder;

    // ==========================
    //      DTO変換メソッド
    // ==========================
    /**
     * エンティティからDTOへの変換（スケジュールもまとめて）
     */
    public TeacherDTO toDTO(TeacherEntity entity, List<TeacherScheduleEntity> schedules) {
        // スケジュールを曜日＋時間帯でグループ化してDTOリストへ変換
        List<TeacherScheduleDTO> scheduleDTOs = mapSchedulesToDTOs(schedules);

        return TeacherDTO.builder()
                .id(entity.getTeacherId())
                .nickname(entity.getNickname())
                .join_date(entity.getJoinDate())
                .nullity(entity.getNullity())
                .schedules(scheduleDTOs)
                .build();
    }

    // ==========================
    //      更新処理
    // ==========================
    @Override
    @Transactional
    public void updateTeacher(Integer teacherNum, TeacherDTO teacherDTO) {
        // 既存の講師情報を取得
        TeacherEntity teacher = teacherRepository.findById(teacherNum)
                .orElseThrow(() -> new RuntimeException("강사가 존재하지 않습니다。"));

        // パスワードが指定されている場合のみ更新
        if (teacherDTO.getPassword() != null && !teacherDTO.getPassword().isEmpty()) {
            teacher.setPassword(passwordEncoder.encode(teacherDTO.getPassword()));
        }
        teacher.setNickname(teacherDTO.getNickname());
        teacher.setNullity(teacherDTO.getNullity());
        teacherRepository.save(teacher);

        // 既存スケジュールをすべて削除
        teacherScheduleRepository.deleteByTeacherNum(teacherNum);
        
        System.out.println("=== schedules ===");
        for (TeacherScheduleDTO s : teacherDTO.getSchedules()) {
            System.out.println(s);
        }
        
        for (TeacherScheduleDTO schedule : teacherDTO.getSchedules()) {
            // 값이 하나라도 null이면 무시
            if (schedule.getStartHour() == null || schedule.getStartMinute() == null
             || schedule.getEndHour() == null || schedule.getEndMinute() == null
             || schedule.getWeekdays() == null || schedule.getWeekdays().isEmpty()) {
                System.out.println("[무시] 빈 스케줄 row: " + schedule);
                continue;
            }

            LocalTime startTime = LocalTime.of(schedule.getStartHour(), schedule.getStartMinute());
            LocalTime endTime = LocalTime.of(schedule.getEndHour(), schedule.getEndMinute());

            for (String weekday : new HashSet<>(schedule.getWeekdays())) {
                LocalTime time = startTime;
                while (!time.isAfter(endTime)) {
                    TeacherScheduleEntity scheduleEntity = TeacherScheduleEntity.builder()
                            .teacherNum(teacherNum)
                            .weekDay(weekday)
                            .timeSlot(time)
                            .isAvailable(1)
                            .build();

                    teacherScheduleRepository.save(scheduleEntity);
                    time = time.plusMinutes(10);
                }
            }
        }
    }

    // ==========================
    //      削除処理
    // ==========================
    @Override
    @Transactional
    public void deleteTeacher(Integer teacherNum) {
        // 先にスケジュールを削除
        teacherScheduleRepository.deleteByTeacherNum(teacherNum);
        // 講師本体を削除
        teacherRepository.deleteById(teacherNum);
    }

    // ==========================
    //      ID重複チェック
    // ==========================
    @Override
    public boolean existsById(String id) {
        // IDの重複を確認
        return teacherRepository.findByTeacherId(id).isPresent();
    }

    // ==========================
    //      新規登録処理
    // ==========================
    @Override
    @Transactional // 講師とスケジュールの登録が両方成功したら保存完了
    public void registerTeacher(TeacherDTO teacherDTO) {
        // 基本情報を保存
        TeacherEntity teacher = TeacherEntity.builder()
                .teacherId(teacherDTO.getId())
                .password(passwordEncoder.encode(teacherDTO.getPassword()))
                .nickname(teacherDTO.getNickname())
                .joinDate(LocalDateTime.now())
                .nullity(false)
                .role(Role.TEACHER)
                .build();

        // 講師本体登録
        TeacherEntity entity = teacherRepository.save(teacher);
        Integer teacherNum = entity.getTeacherNum();

        // スケジュール保存
        for (TeacherScheduleDTO schedule : teacherDTO.getSchedules()) {
            // 時間（時と分）を結合してLocalTimeを作成
            LocalTime startTime = LocalTime.of(schedule.getStartHour(), schedule.getStartMinute());
            LocalTime endTime = LocalTime.of(schedule.getEndHour(), schedule.getEndMinute());

            // 曜日ごとにタイムスロットを保存
            for (String weekday : schedule.getWeekdays()) {
                // 時間初期化
                LocalTime time = startTime;

                while (!time.isAfter(endTime)) {
                    TeacherScheduleEntity scheduleEntity = TeacherScheduleEntity.builder()
                            .teacherNum(teacherNum)
                            .weekDay(weekday)
                            .timeSlot(time)
                            .isAvailable(1)
                            .build();

                    teacherScheduleRepository.save(scheduleEntity);

                    time = time.plusMinutes(10);
                }
            }
        }
    }

    // ==========================
    //    スケジュール→DTO変換
    // ==========================
    /**
     * 同じ時間帯が複数の曜日で重複する場合は1つのDTOのweekdayリストにまとめて返す
     */
    public List<TeacherScheduleDTO> mapSchedulesToDTOs(List<TeacherScheduleEntity> schedules) {
        // 1. 曜日ごとに時間スロットをまとめる
        Map<String, List<LocalTime>> weekdayMap = new LinkedHashMap<>();
        for (TeacherScheduleEntity entity : schedules) {
            weekdayMap.computeIfAbsent(entity.getWeekDay(), k -> new ArrayList<>()).add(entity.getTimeSlot());
        }

        // 2. 同じ時間帯（開始～終了）が同じ曜日をまとめる
        Map<String, List<String>> timeRangeToWeekdays = new LinkedHashMap<>();
        Map<String, LocalTime[]> timeRangeToTimes = new LinkedHashMap<>(); // 時間範囲パース用

        for (Map.Entry<String, List<LocalTime>> entry : weekdayMap.entrySet()) {
            List<LocalTime> times = entry.getValue().stream().sorted().collect(Collectors.toList());
            if (times.isEmpty()) continue;
            LocalTime start = times.get(0);
            LocalTime end = times.get(times.size() - 1);
            // 開始～終了時刻をkeyに
            String timeKey = String.format("%02d:%02d-%02d:%02d",
                    start.getHour(), start.getMinute(),
                    end.getHour(), end.getMinute());
            timeRangeToWeekdays.computeIfAbsent(timeKey, k -> new ArrayList<>()).add(entry.getKey());
            timeRangeToTimes.put(timeKey, new LocalTime[] { start, end });
        }

        // 3. DTOリスト作成
        List<TeacherScheduleDTO> result = new ArrayList<>();
        for (String key : timeRangeToWeekdays.keySet()) {
            List<String> weekdays = timeRangeToWeekdays.get(key);
            LocalTime[] times = timeRangeToTimes.get(key);
            TeacherScheduleDTO dto = new TeacherScheduleDTO();
            dto.setWeekdays(weekdays);
            dto.setStartHour(times[0].getHour());
            dto.setStartMinute(times[0].getMinute());
            dto.setEndHour(times[1].getHour());
            dto.setEndMinute(times[1].getMinute());
            result.add(dto);
        }
        return result;
    }
}

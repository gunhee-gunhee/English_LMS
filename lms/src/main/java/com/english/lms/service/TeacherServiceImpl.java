package com.english.lms.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.english.lms.dto.TeacherDTO;
import com.english.lms.dto.TeacherScheduleDTO;
import com.english.lms.entity.TeacherEntity;
import com.english.lms.entity.TeacherScheduleEntity;
import com.english.lms.entity.ZoomAccountEntity;
import com.english.lms.enums.Role;
import com.english.lms.repository.TeacherListRepository;
import com.english.lms.repository.TeacherRepository;
import com.english.lms.repository.TeacherScheduleRepository;
import com.english.lms.repository.ZoomAccountRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherScheduleRepository teacherScheduleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ZoomAccountRepository zoomAccountRepository;
    private final TeacherListRepository teacherListRepository;
    
    // ==========================
    //      DTO変換メソッド
    // ==========================
    /**
     * エンティティからDTOへの変換（スケジュールもまとめて）
     */
    public TeacherDTO toDTO(TeacherEntity entity, List<TeacherScheduleEntity> schedules) {
        List<TeacherScheduleDTO> scheduleDTOs = mapSchedulesToDTOs(schedules);

        // ZoomId lookup by zoomNum
        String zoomId = null;
        if (entity.getZoomNum() != null) {
            Optional<ZoomAccountEntity> zoomOpt = zoomAccountRepository.findById(entity.getZoomNum());
            if (zoomOpt.isPresent()) {
                zoomId = zoomOpt.get().getZoomId();
            }
        }

        return TeacherDTO.builder()
                .teacherNum(entity.getTeacherNum())
                .id(entity.getTeacherId())
                .nickname(entity.getNickname())
                .join_date(entity.getJoinDate())
                .nullity(entity.getNullity())
                .zoomId(zoomId != null ? zoomId.trim() : null)  
                .schedules(scheduleDTOs)
                .build();
    }

    // ==========================
    //      更新処理
    // ==========================
    @Override
    @Transactional
    public void updateTeacher(Integer teacherNum, TeacherDTO teacherDTO) {
        TeacherEntity teacher = teacherRepository.findById(teacherNum)
                .orElseThrow(() -> {
                    log.error("[講師更新] 該当する講師が存在しません (講師番号: {})", teacherNum);
                    return new RuntimeException("該当する講師が存在しません。");
                });
        log.info("[講師更新] 講師情報を更新します (講師番号: {})", teacherNum);

        // パスワード
        if (teacherDTO.getPassword() != null && !teacherDTO.getPassword().isEmpty()) {
            if (!teacherDTO.getPassword().equals(teacherDTO.getPasswordCheck())) {
                log.warn("[講師更新] パスワードが一致しません (ID: {})", teacher.getTeacherId());
                throw new IllegalArgumentException("パスワードが一致しません。");
            }
            log.info("[講師更新] パスワードを更新します (ID: {})", teacher.getTeacherId());
            teacher.setPassword(passwordEncoder.encode(teacherDTO.getPassword()));
        }

        teacher.setNickname(teacherDTO.getNickname());
        teacher.setNullity(teacherDTO.getNullity());

        // Zoomアカウント連携
        if (teacherDTO.getZoomId() != null && !teacherDTO.getZoomId().isEmpty()) {
            ZoomAccountEntity newZoom = zoomAccountRepository.findByZoomId(teacherDTO.getZoomId())
                    .orElseThrow(() -> {
                        log.error("[講師更新] Zoomアカウントが存在しません: {}", teacherDTO.getZoomId());
                        return new RuntimeException("Zoomアカウントが存在しません。");
                    });
            // 기존 Zoom 해제
            if (teacher.getZoomNum() != null) {
                zoomAccountRepository.findById(teacher.getZoomNum())
                        .ifPresent(oldZoom -> {
                            oldZoom.setLinked(false);
                            zoomAccountRepository.save(oldZoom);
                            log.info("[講師更新] 以前のZoomアカウント連携解除: {}", oldZoom.getZoomId());
                        });
            }
            newZoom.setLinked(true);
            zoomAccountRepository.save(newZoom);
            teacher.setZoomNum(newZoom.getZoomNum());
            log.info("[講師更新] 新しいZoomアカウント連携: {}", newZoom.getZoomId());
        }

        teacherRepository.save(teacher);
        log.info("[講師更新] 講師情報の保存が完了しました (講師番号: {})", teacherNum);
        teacher.setNickname(teacherDTO.getNickname());
        teacher.setNullity(teacherDTO.getNullity());
        teacherRepository.save(teacher);

        // 既存スケジュールをすべて削除
        teacherScheduleRepository.deleteByTeacherNum(teacherNum);

        for (TeacherScheduleDTO schedule : teacherDTO.getSchedules()) {
            // 값이 하나라도 null이면 무시
            if (schedule.getStartHour() == null || schedule.getStartMinute() == null
                    || schedule.getEndHour() == null || schedule.getEndMinute() == null
                    || schedule.getWeekdays() == null || schedule.getWeekdays().isEmpty()) {
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
    //      ID重複チェック
    // ==========================
    @Override
    public boolean existsById(String id) {
        return teacherRepository.findByTeacherId(id).isPresent();
    }

    // ==========================
    //      新規登録処理
    // ==========================
    @Override
    @Transactional
    public void registerTeacher(TeacherDTO teacherDTO) {
        // zoom_id로 ZoomAccountEntity를 찾음
        ZoomAccountEntity zoomEntity = zoomAccountRepository.findByZoomId(teacherDTO.getZoomId())
                .orElseThrow(() -> new RuntimeException("Zoomアカウントが存在しません。"));
        Integer zoomNum = zoomEntity.getZoomNum();

        // 基本情報保存
        TeacherEntity teacher = TeacherEntity.builder()
                .teacherId(teacherDTO.getId())
                .password(passwordEncoder.encode(teacherDTO.getPassword()))
                .nickname(teacherDTO.getNickname())
                .zoomNum(zoomNum)
                .joinDate(LocalDateTime.now())
                .nullity(false)
                .role(Role.TEACHER)
                .build();

        // Zoom アカウントの linkedをtrueに更新する。
        zoomEntity.setLinked(true);

        // 講師の情報を保存する。
        TeacherEntity entity = teacherRepository.save(teacher);
        Integer teacherNum = entity.getTeacherNum();

        // 授業のスケジュールを保存する。
        for (TeacherScheduleDTO schedule : teacherDTO.getSchedules()) {
            if (schedule.getStartHour() == null || schedule.getStartMinute() == null
                    || schedule.getEndHour() == null || schedule.getEndMinute() == null
                    || schedule.getWeekdays() == null || schedule.getWeekdays().isEmpty()) {
                continue;
            }
            LocalTime startTime = LocalTime.of(schedule.getStartHour(), schedule.getStartMinute());
            LocalTime endTime = LocalTime.of(schedule.getEndHour(), schedule.getEndMinute());
            for (String weekday : schedule.getWeekdays()) {
                LocalTime time = startTime;
                while (time.isBefore(endTime)) {
         
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
        Map<String, List<LocalTime>> weekdayMap = new LinkedHashMap<>();
        for (TeacherScheduleEntity entity : schedules) {
            weekdayMap.computeIfAbsent(entity.getWeekDay(), k -> new ArrayList<>()).add(entity.getTimeSlot());
        }

        Map<String, List<String>> timeRangeToWeekdays = new LinkedHashMap<>();
        Map<String, LocalTime[]> timeRangeToTimes = new LinkedHashMap<>();

        for (Map.Entry<String, List<LocalTime>> entry : weekdayMap.entrySet()) {
            List<LocalTime> times = entry.getValue().stream().sorted().collect(Collectors.toList());
            if (times.isEmpty()) continue;
            LocalTime start = times.get(0);
            LocalTime end = times.get(times.size() - 1);
            String timeKey = String.format("%02d:%02d-%02d:%02d",
                    start.getHour(), start.getMinute(),
                    end.getHour(), end.getMinute());
            timeRangeToWeekdays.computeIfAbsent(timeKey, k -> new ArrayList<>()).add(entry.getKey());
            timeRangeToTimes.put(timeKey, new LocalTime[]{start, end});
        }

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
    
    
    // ====================================================
    //    教師照会ページで使用される処理メソッド
    // ====================================================
	@Override
	public Page<TeacherDTO> getTeacherPage(Pageable pageable) {
		/**
		    Pageableは、現在のページ番号や1ページあたりに表示するデータの件数など、ページングに関する情報を持つオブジェクト
			TeacherEntityは、データベースの1行（レコード）を表すオブジェクト
		*/
		return teacherListRepository.findAllTeachersPage(pageable);
	}
	
}

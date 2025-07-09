package com.english.lms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.english.lms.dto.StudentDetailDto;
import com.english.lms.entity.ClassEntity;
import com.english.lms.entity.DayClassEntity;
import com.english.lms.entity.StudentEntity;
import com.english.lms.enums.Role;
import com.english.lms.repository.ClassRepository;
import com.english.lms.repository.DayClassRepository;
import com.english.lms.repository.StudentRepository;
import com.english.lms.repository.TeacherRepository;

import lombok.RequiredArgsConstructor;

/**
 * 管理者用：学生詳細サービス
 */
@Service
@RequiredArgsConstructor
public class AdminStudentService {
    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;
    private final DayClassRepository dayClassRepository;
    private final TeacherRepository teacherRepository; // 講師リポジトリ

    /**
     * 学生詳細情報＋受講情報を取得
     * @param studentNum 学生番号
     * @return StudentDetailDto
     */
    public StudentDetailDto getStudentDetail(Integer studentNum) {
        // 学生情報取得
        StudentEntity student = studentRepository.findById(studentNum)
                .orElseThrow(() -> new RuntimeException("学生情報がありません"));

        // 受講クラス一覧取得
        List<ClassEntity> classes = classRepository.findByStudentNum(studentNum);
        List<StudentDetailDto.ClassInfo> classInfos = new ArrayList<>();
        for (ClassEntity cls : classes) {
            // 曜日情報変換（int→リスト）
            List<String> weekDays = parseWeekDays(cls.getWeekDays());

            // クラスごとの日程情報（時間割）
            List<DayClassEntity> dayClasses = dayClassRepository.findByClassNum(cls.getClassNum());
            List<StudentDetailDto.DayClassInfo> schedules = new ArrayList<>();
            for (DayClassEntity dc : dayClasses) {
                schedules.add(StudentDetailDto.DayClassInfo.builder()
                    .dayClassNum(dc.getDayClassNum())
                    .classType(dc.getClassType())
                    .className(dc.getClassName())
                    .classDate(dc.getClassDate() != null ? dc.getClassDate().toString() : null)
                    .startTime(dc.getStartTime() != null ? dc.getStartTime().toString() : null)
                    .endTime(dc.getEndTime() != null ? dc.getEndTime().toString() : null)
                    .build());
            }

            classInfos.add(StudentDetailDto.ClassInfo.builder()
                .classNum(cls.getClassNum())
                .teacherNum(cls.getTeacherNum())
                .weekDays(weekDays)
                .schedules(schedules)
                .build()
            );
        }

        // 学生情報＋受講情報をDTOに変換して返却
        return StudentDetailDto.builder()
                .studentNum(student.getStudentNum())
                .id(student.getStudentId()) // エンティティのstudentId → DTOのid
                .password(student.getPassword())
                .nickname(student.getNickname())
                .nicknameJp(student.getNicknameJp())
                .age(student.getAge())
                .englishLevel(student.getEnglishLevel())
                .englishPurpose(student.getEnglishPurpose())
                .signupPath(student.getSignupPath())
                .joinDate(student.getJoinDate() != null ? student.getJoinDate().toString() : null)
                .nullity(student.getNullity())
                .point(student.getPoint())
                .role(student.getRole() != null ? student.getRole().name() : null) // Enum→String
                .company(student.getCompany())
                .classes(classInfos)
                .build();
    }

    /**
     * 年齢選択肢リストを返す
     */
    public List<String> getAgeOptions() {
        List<String> ages = new ArrayList<>();
        for (int i = 6; i <= 18; i++) ages.add(i + "歳");
        ages.add("大学生");
        ages.add("社会人");
        return ages;
    }

    /**
     * 英語レベル選択肢リストを返す
     */
    public List<String> getEnglishLevelOptions() {
        return List.of("1", "2", "3", "4", "5");
    }

    /**
     * 講師選択肢リストを返す
     */
    public List<String> getTeacherOptions() {
        // 実際にはteacherRepositoryから取得
        // return teacherRepository.findAll().stream().map(TeacherEntity::getName).toList();
        return List.of("田中先生", "佐藤先生", "Kim Teacher"); // 仮データ
    }

    /**
     * 曜日選択肢リストを返す
     */
    public List<String> getWeekDayOptions() {
        return List.of("日", "月", "火", "水", "木", "金", "土");
    }

    /**
     * 時間帯選択肢リストを返す
     */
    public List<String> getTimeOptions(Integer studentNum) {
        // 実際はDBから取得してもOK
        return List.of("09:00", "10:00", "11:00", "14:00", "15:00", "16:00");
    }

    /**
     * 学生情報を修正
     * @param studentNum 学生番号
     * @param dto フォームからの入力値
     */
    public void updateStudent(Integer studentNum, StudentDetailDto dto) {
        StudentEntity student = studentRepository.findById(studentNum)
                .orElseThrow(() -> new RuntimeException("学生情報がありません"));
        // 入力値で上書き（nullチェックは適宜追加）
        student.setStudentId(dto.getId());
        student.setPassword(dto.getPassword());
        student.setNickname(dto.getNickname());
        student.setNicknameJp(dto.getNicknameJp());
        student.setAge(dto.getAge());
        student.setEnglishLevel(dto.getEnglishLevel());
        student.setEnglishPurpose(dto.getEnglishPurpose());
        student.setSignupPath(dto.getSignupPath());
        student.setNullity(dto.getNullity());
        student.setPoint(dto.getPoint());
        // 役割(Role)はString→Enum変換して設定
        if (dto.getRole() != null) {
            try {
                student.setRole(Role.valueOf(dto.getRole()));
            } catch (Exception e) {
                // 変換失敗時の処理（必要ならログや例外処理）
            }
        }
        student.setCompany(dto.getCompany());
        studentRepository.save(student);
    }

    /**
     * 学生を削除
     * @param studentNum 学生番号
     */
    public void deleteStudent(Integer studentNum) {
        studentRepository.deleteById(studentNum);
    }

    /**
     * 曜日情報（int型）を文字列リストに変換
     * 例：0b0001010 → ["火", "木"]
     * @param weekDays
     * @return List<String>
     */
    private List<String> parseWeekDays(Integer weekDays) {
        String[] weekStr = {"日","月","火","水","木","金","土"};
        List<String> result = new ArrayList<>();
        if (weekDays == null) return result;
        for (int i = 0; i < 7; i++) {
            if ((weekDays & (1 << i)) != 0) result.add(weekStr[i]);
        }
        return result;
    }
}

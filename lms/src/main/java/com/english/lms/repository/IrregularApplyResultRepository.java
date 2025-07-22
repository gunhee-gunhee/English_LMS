package com.english.lms.repository;

import com.english.lms.entity.TextEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IrregularApplyResultRepository {
    private boolean success;
    private String message;
    private String reservedTime;
    private String teacherNickname;
}

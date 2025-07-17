package com.english.lms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "lms_point")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PointEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pointNum;   // point_num

    private Integer studentNum; // student_num

    private Integer pointAmount; // point_amount

    @Column(length = 20)
    private String type; // type

    private LocalDate createdAt; // created_at

    private LocalDate expiresAt; // expires_at

    private LocalDate absentDate; // absent_date
}

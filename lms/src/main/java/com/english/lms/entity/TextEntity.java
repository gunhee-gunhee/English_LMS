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


@Entity
@Table(name = "lms_text")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TextEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "text_num")
    private Integer textNum;

    @Column(name = "name")
    private String name;

    // 필요시 컬럼 추가
}

package com.english.lms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lms_system")
@Getter @Setter
@NoArgsConstructor
public class SystemSettingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer systemNum;

    @Column(name = "`key`", nullable = false, length = 50)
    private String key;

    @Column(name = "`value`", nullable = false)
    private String value;

}

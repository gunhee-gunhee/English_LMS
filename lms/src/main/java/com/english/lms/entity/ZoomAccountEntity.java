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
@Table(name = "lms_zoom_account")
@Getter
@Setter
@NoArgsConstructor
public class ZoomAccountEntity {

    // zoomAccount番号
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "zoom_num")
    private Integer zoomNum;

    // zoomAccountID
    @Column(name = "zoom_id", nullable = false, length = 40)
    private String zoomId;

    // アカウントの状態
    @Column(name = "status", nullable = true, length = 10)
    private String status;

    // 講師との連結
    @Column(name = "linked", nullable = false)
    private Boolean linked;
}

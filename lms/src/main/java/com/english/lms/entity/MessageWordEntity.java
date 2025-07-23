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
@Table(name = "lms_message_word")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MessageWordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_num")
    private Integer wordNum;

    @Column(name = "word", nullable = false, length = 100)
    private String word;

    @Column(name = "word_jp", nullable = false, length = 100)
    private String wordJp;

}

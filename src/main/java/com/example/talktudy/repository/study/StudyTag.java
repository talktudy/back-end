package com.example.talktudy.repository.study;

import com.example.talktudy.repository.tag.Tag;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "study_tag")
public class StudyTag { // study, tag 매핑 테이블
    @Id
    @Column(name = "study_tag_id") @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studyTagId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "study_id")
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "tag_id")
    private Tag tag;



} // end class

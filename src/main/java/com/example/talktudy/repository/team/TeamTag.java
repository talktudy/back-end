package com.example.talktudy.repository.team;

import com.example.talktudy.repository.study.Study;
import com.example.talktudy.repository.tag.Tag;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "team_tag")
public class TeamTag {
    @Id
    @Column(name = "team_tag_id") @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamTagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;
}

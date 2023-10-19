package com.example.talktudy.service.tag;

import com.example.talktudy.repository.study.Study;
import com.example.talktudy.repository.study.StudyTag;
import com.example.talktudy.repository.study.StudyTagRepository;
import com.example.talktudy.repository.tag.Tag;
import com.example.talktudy.repository.tag.TagRepository;
import com.example.talktudy.repository.team.Team;
import com.example.talktudy.repository.team.TeamTag;
import com.example.talktudy.repository.team.TeamTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final StudyTagRepository studyTagRepository;
    private final TeamTagRepository teamTagRepository;

    @Transactional
    public void createStudyTags(String[] tags, Study study) {
        List<StudyTag> studyTags = Arrays.stream(tags).map(
                tagName -> {
                    Tag newTag = tagRepository.findByName(tagName).orElseGet(
                            () -> tagRepository.save(
                                    Tag.builder().name(tagName).build()
                            )
                    );

                    return StudyTag.builder().study(study).tag(newTag).build();
                }
        ).collect(Collectors.toList());

        studyTagRepository.saveAll(studyTags);
    }

    @Transactional
    public void createTeamTags(String[] tags, Team team) {
        List<TeamTag> teamTags = Arrays.stream(tags).map(
                tagName -> {
                    Tag newTag = tagRepository.findByName(tagName).orElseGet(
                            () -> tagRepository.save(
                                    Tag.builder().name(tagName).build()
                            )
                    );

                    return TeamTag.builder().team(team).tag(newTag).build();
                }
        ).collect(Collectors.toList());

        teamTagRepository.saveAll(teamTags);
    }

} //  enc class

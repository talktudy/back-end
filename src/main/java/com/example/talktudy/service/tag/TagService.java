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
    public void createStudyTags(String[] tags, Study study, boolean isUpdate) {
        // 1. 수정이면 스터디 태그를 모두 한번에 삭제시키기
        if (isUpdate) studyTagRepository.deleteAllByStudy(study);

        // 2. 태그와 스터디 태그 등록
        List<StudyTag> studyTags = Arrays.stream(tags).map(
                tagName -> {
                    String lowerTagName = tagName.toLowerCase();

                    Tag newTag = tagRepository.findByName(lowerTagName).orElseGet(
                            () -> tagRepository.save(
                                    Tag.builder().name(lowerTagName).build()
                            )
                    );

                    return StudyTag.builder().study(study).tag(newTag).build();
                }
        ).collect(Collectors.toList());

        studyTagRepository.saveAll(studyTags);
    }

    @Transactional
    public void createTeamTags(String[] tags, Team team) {
        // 새로운 태그를 Insert,
        List<TeamTag> teamTags = Arrays.stream(tags).map(
                tagName -> {
                    String lowerTagName = tagName.toLowerCase();

                    Tag newTag = tagRepository.findByName(lowerTagName).orElseGet(
                            () -> tagRepository.save(
                                    Tag.builder().name(lowerTagName).build()
                            )
                    );

                    return TeamTag.builder().team(team).tag(newTag).build();
                }
        ).collect(Collectors.toList());

        teamTagRepository.saveAll(teamTags);
    }
} //  enc class

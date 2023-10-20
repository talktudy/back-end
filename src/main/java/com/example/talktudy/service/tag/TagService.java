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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final StudyTagRepository studyTagRepository;
    private final TeamTagRepository teamTagRepository;

    private String filterRegex(String tagName) {
        // 숫자, 알파벳 대소문자, 한글 문자만 허용하는 정규 표현식
        String regex = "^[a-zA-Z0-9가-힣!@#$%^&*()_+\\-=\\[\\]{};':\",.<>/?]+$";

        // 정규 표현식에 맞지 않는 문자 모두 제거
        String filteredString = tagName.replaceAll(regex, "");

        log.info("Original tagName: " + tagName);
        log.info("Filtered tagName: " + filteredString);

        return filteredString;
    }

    @Transactional
    public Set<StudyTag> createStudyTags(String[] tags, Study study, boolean isUpdate) {
        // 1. 수정이면 스터디 태그를 모두 한번에 삭제시키기
        if (isUpdate) studyTagRepository.deleteAllByStudy(study);

        // 2. 태그와 스터디 태그 등록
        List<StudyTag> studyTags = Arrays.stream(tags).map(
                tagName -> {
                    String lowerTagName = tagName.replaceAll("\\s", "").toLowerCase(); //filterRegex(tagName).toLowerCase();

                    Tag newTag = tagRepository.findByName(lowerTagName).orElseGet(
                            () -> tagRepository.save(
                                    Tag.builder().name(lowerTagName).build()
                            )
                    );

                    return StudyTag.builder().study(study).tag(newTag).build();
                }
        ).collect(Collectors.toList());

        studyTagRepository.saveAll(studyTags);

        return studyTags.stream().collect(Collectors.toSet());
    }

    @Transactional
    public Set<TeamTag> createTeamTags(String[] tags, Team team, boolean isUpdate) {
        // 1. 수정이면 팀 태그를 모두 한번에 삭제시키기
        if (isUpdate) teamTagRepository.deleteAllByTeam(team);

        // 2. 태그와 팀 태그 등록
        List<TeamTag> teamTags = Arrays.stream(tags).map(
                tagName -> {
                    String lowerTagName = tagName.replaceAll("\\s", "").toLowerCase(); // filterRegex(tagName).toLowerCase();

                    Tag newTag = tagRepository.findByName(lowerTagName).orElseGet(
                            () -> tagRepository.save(
                                    Tag.builder().name(lowerTagName).build()
                            )
                    );

                    return TeamTag.builder().team(team).tag(newTag).build();
                }
        ).collect(Collectors.toList());

        teamTagRepository.saveAll(teamTags);

        return teamTags.stream().collect(Collectors.toSet());
    }
} //  enc class

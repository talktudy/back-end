package com.example.talktudy.repository.team;

import com.example.talktudy.repository.common.Interests;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.talktudy.repository.study.QStudy.study;
import static com.example.talktudy.repository.team.QTeam.team;

@RequiredArgsConstructor
public class TeamRepositoryImpl implements CustomTeamRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Team> findAll(Pageable pageable, String orderCriteria, List<String> interests, String keyword, String type) {

        List<Team> teams = jpaQueryFactory
                .selectFrom(team)
                .where(
                        eqInterests(interests),
                        eqKeyword(keyword, type)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(sortTeams(orderCriteria))
                .fetch();

        return new PageImpl<>(teams, pageable, teams.size());
    }

    // 모집 분야 복수 검색
    private BooleanBuilder eqInterests(List<String> interests) {
        if (interests == null) return null;

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        for (String interest : interests) {
            booleanBuilder.or(team.interests.eq(Enum.valueOf(Interests.class, interest)));
        }

        return booleanBuilder;
    }

    // type에 맞는 키워드 검색
    private BooleanExpression eqKeyword(String keyword, String type) {
        if (keyword == null || type == null) return null;

        if ("title".equalsIgnoreCase(type)) {
            return team.title.contains(keyword);
        }
        else if ("tag".equalsIgnoreCase(type)) {
            return team.teamTags.any().tag.name.contains(keyword);
        }

        return null;
    }

    // 정렬
    private OrderSpecifier<?> sortTeams(String orderCriteria) {
        if ("views".equalsIgnoreCase(orderCriteria)) {
            return team.views.desc();
        }

        return team.teamId.desc(); // default
    }

} // end class

package com.example.talktudy.repository.study;

import com.example.talktudy.repository.common.Interests;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;

import static com.example.talktudy.repository.study.QStudy.study;

/*
* QueryDSL
*
* BooleanExpression : 1번 조회
* BooleanBuilder : 여러 가지 값을 같은 조건에 넣어야할 때 사용
*
* 1. eq : 동등 조건, equals를 검사한다.
* 2. ne : 같지 않음을 검사
* 3. lt,le,gt,ge : less than, less equal, greater than, greater equal
* 4. like, contains : 일치 또는 일부 일치 like("%value%") contains("substring")
* 5. in : list or subquery의 결과와의 일치 여부(반대는 notin)
* */

@RequiredArgsConstructor
public class StudyRepositoryImpl implements CustomStudyRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Study> findAll(Pageable pageable, String orderCriteria, String isOpen, List<String> interests, String keyword, String type) {

        List<Study> studies = jpaQueryFactory
                .selectFrom(study)
                .where(
                        eqOpen(isOpen),
                        eqInterests(interests),
                        eqKeyword(keyword, type)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(sortStudies(orderCriteria))
                .fetch();

        return new PageImpl<>(studies, pageable, studies.size());
    }

    // 모집 상태의 여부
    private BooleanExpression eqOpen(String isOpen) {
        if (isOpen == null) return null;

        boolean open = "true".equalsIgnoreCase(isOpen);

        return study.open.eq(open);
    }

    // 모집 분야 복수 검색
    private BooleanBuilder eqInterests(List<String> interests) {
        if (interests == null) return null;

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        for (String interest : interests) {
            booleanBuilder.or(study.interests.eq(Enum.valueOf(Interests.class, interest)));
        }

        return booleanBuilder;
    }

    // type에 맞는 키워드 검색
    private BooleanExpression eqKeyword(String keyword, String type) {
        if (keyword == null || type == null) return null;

        if ("title".equalsIgnoreCase(type)) {
            return study.title.contains(keyword);
        }
        else if ("tag".equalsIgnoreCase(type)) {
            return study.studyTags.any().tag.name.contains(keyword);
        }

        return null;
    }


    private OrderSpecifier<?> sortStudies(String orderCriteria) {
        if ("views".equalsIgnoreCase(orderCriteria)) {
            return study.views.desc();
        }
        else if ("maxCapacity".equalsIgnoreCase(orderCriteria)) {
            return study.maxCapacity.desc();
        }
        else if ("endDate".equalsIgnoreCase(orderCriteria)) {
            return study.endDate.desc();
        }

        return study.studyId.desc(); // default
    }

}

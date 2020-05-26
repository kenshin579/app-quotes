package kr.pe.advenoh.quote.repository.quote;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.pe.advenoh.quote.model.dto.QuoteResponseDto;
import kr.pe.advenoh.quote.model.entity.QFolderQuoteMapping;
import kr.pe.advenoh.quote.model.entity.QQuote;
import kr.pe.advenoh.quote.model.entity.QQuoteTagMapping;
import kr.pe.advenoh.quote.model.entity.QTag;
import kr.pe.advenoh.quote.model.entity.Quote;
import kr.pe.advenoh.quote.model.entity.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class QuoteRepositoryCustomImpl extends QuerydslRepositorySupport implements QuoteRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public QuoteRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        super(Quote.class);
        this.queryFactory = queryFactory;
    }

    /**
     * https://velog.io/@recordsbeat/QueryDSL%EB%A1%9C-%EA%B2%80%EC%83%89-%ED%8E%98%EC%9D%B4%EC%A7%95-%EC%A0%81%EC%9A%A9%ED%95%98%EA%B8%B0
     * https://adunhansa.tistory.com/225
     *
     * @param folderId
     * @param pageable
     * @return
     */
    @Override
    public Page<Quote> findAllByFolderId(Long folderId, Pageable pageable) {
        QQuote qQuote = QQuote.quote;
        QFolderQuoteMapping qFolderQuoteMapping = QFolderQuoteMapping.folderQuoteMapping;

//        SELECT
//                <include refid="liveSelectQuery"/>
//                ,(
//                select group_concat( distinct(deal_no) SEPARATOR ',' )
//        from tmon_media.media_deal_mapping mdm
//        where mdm.media_seqno = m.media_seqno
//        ) dealStr
//        FROM tmon_media.media_info m
//        inner join tmon_media.media_info_live ml
//        on ml.media_seqno = m.media_seqno
//        left join tmon_media.media_live_job j
//        on ml.live_seqno = j.live_seqno and j.use_yn = 'Y'
//        left join tmon_media.media_owner o
//        on o.owner_seqno = m.owner_seqno
//        JPQLQuery subQuery = from(qQuote)
//                .;

        JPQLQuery query = from(qQuote)
                .innerJoin(qFolderQuoteMapping)
                .on(qQuote.id.eq(qFolderQuoteMapping.quote.id))
                .where(qFolderQuoteMapping.folder.id.eq(folderId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        final List<Quote> quotes = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(quotes, pageable, query.fetchCount());
//        return null;
    }

    /**
     * todo : query 한번에 실행하도록 변경하기
     *
     * http://www.querydsl.com/static/querydsl/3.4.3/reference/html/ch03s02.html
     * https://adunhansa.tistory.com/225
     *
     * @param quoteId
     * @return
     */
    @Override
    public Optional<QuoteResponseDto> findAllByQuoteId(Long quoteId) {
        QQuote qQuote = QQuote.quote;
        QTag qTag = QTag.tag;
        QQuoteTagMapping qQuoteTagMapping = QQuoteTagMapping.quoteTagMapping;

        List<Tag> tags = queryFactory.select(Projections.constructor(Tag.class, qTag.tagName))
                .from(qTag)
                .innerJoin(qQuoteTagMapping).on(qTag.id.eq(qQuoteTagMapping.tag.id))
                .where(qQuoteTagMapping.quote.id.eq(quoteId))
                .fetch();

        log.info("tags : {}", tags);

        QuoteResponseDto result = queryFactory.select(Projections.constructor(QuoteResponseDto.class,
                qQuote.id, qQuote.quoteText, qQuote.author.name.as("authorName"), qQuote.useYn))
                .from(qQuote)
//                .leftJoin(qQuoteTagMapping).on(qQuote.id.eq(qQuoteTagMapping.quote.id))
                .where(qQuote.id.eq(quoteId))
                .fetchOne();

        result.setTags(tags.stream().map(Tag::getTagName).collect(Collectors.toList()));
        return Optional.ofNullable(result);
    }
}

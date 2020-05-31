package kr.pe.advenoh.quote.repository.quote;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.sql.SQLExpressions;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import kr.pe.advenoh.quote.model.dto.QuoteResponseDto;
import kr.pe.advenoh.quote.model.entity.*;
import kr.pe.advenoh.quote.model.enums.YN;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class QuoteRepositoryCustomImpl extends QuerydslRepositorySupport implements QuoteRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final SQLQueryFactory sqlQueryFactory;

    public QuoteRepositoryCustomImpl(JPAQueryFactory queryFactory, SQLQueryFactory sqlQueryFactory) {
        super(Quote.class);
        this.queryFactory = queryFactory;
        this.sqlQueryFactory = sqlQueryFactory;
    }

    /**
     * https://velog.io/@recordsbeat/QueryDSL%EB%A1%9C-%EA%B2%80%EC%83%89-%ED%8E%98%EC%9D%B4%EC%A7%95-%EC%A0%81%EC%9A%A9%ED%95%98%EA%B8%B0
     * https://adunhansa.tistory.com/225
     * https://github.com/querydsl/querydsl/blob/master/querydsl-examples/querydsl-example-sql-spring/src/main/java/com/querydsl/example/dao/ProductDaoImpl.java
     *
     * @param folderId
     * @param pageable
     * @return
     */
    @Override
    public Page<QuoteResponseDto> findAllByFolderId(Long folderId, Pageable pageable) {
        SQuotes sQuotes = SQuotes.quotes;
        SAuthors sAuthors = SAuthors.authors;
        STags sTags = STags.tags;
        SQuotesTags sQuotesTags = SQuotesTags.quotesTags;
        SFolders sFolders = SFolders.folders;
        SFoldersQuotes sFoldersQuotes = SFoldersQuotes.foldersQuotes;

        SQLQuery<String> subQuery = sqlQueryFactory.selectDistinct(SQLExpressions.groupConcat(sTags.tagName))
                .from(sTags)
                .where(sQuotes.quoteId.eq(sQuotesTags.quoteId));

        long totalElements = sqlQueryFactory.query()
                .from(sQuotes)
                .fetchCount();

//        select q.quote_id,
//                q.quote_text,
//                q.use_yn,
//                a.name,
//                f.folder_id
//        from quotes as q
//        inner join folders as f
//        inner join folders_quotes folders_quotes on folders_quotes.folder_id = f.folder_id
//        left join quotes_tags quotes_tags on q.quote_id = quotes_tags.quote_id
//        left join authors as a on q.author_id = a.author_id
//        where f.folder_id = 10
//        and folders_quotes.quote_id = q.quote_id;

        List<QuoteResponseDto> quoteResponseDtos = sqlQueryFactory.select(sQuotes.quoteId, sQuotes.quoteText, sQuotes.useYn, sAuthors.name, subQuery.as("tagList"))
                .from(sQuotes)
                .innerJoin(sFolders)
                .innerJoin(sFoldersQuotes).on(sFoldersQuotes.folderId.eq(sFolders.folderId))
                .leftJoin(sQuotesTags).on(sQuotes.quoteId.eq(sQuotesTags.quoteId))
                .leftJoin(sAuthors).on(sQuotes.authorId.eq(sAuthors.authorId))
                .where(sFolders.folderId.eq(folderId).and(sFoldersQuotes.quoteId.eq(sQuotes.quoteId)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch().stream()
                .map(x -> new QuoteResponseDto(x.get(sQuotes.quoteId), x.get(sQuotes.quoteText), x.get(sAuthors.name),
                        YN.valueOf(x.get(sQuotes.useYn)), Arrays.asList(x.get(x.size() - 1, String.class))))
                .collect(Collectors.toList());

        return new PageImpl<>(quoteResponseDtos, pageable, totalElements);
    }

    /**
     * todo : query 한번에 실행하도록 변경하기
     * <p>
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

        QuoteResponseDto result = queryFactory.select(Projections.fields(QuoteResponseDto.class,
                qQuote.id, qQuote.quoteText, qQuote.author.name.as("authorName"), qQuote.useYn))
                .from(qQuote)
//                .leftJoin(qQuoteTagMapping).on(qQuote.id.eq(qQuoteTagMapping.quote.id))
                .where(qQuote.id.eq(quoteId))
                .fetchOne();

        result.setTags(tags.stream().map(Tag::getTagName).collect(Collectors.toList()));
        return Optional.ofNullable(result);
    }
}

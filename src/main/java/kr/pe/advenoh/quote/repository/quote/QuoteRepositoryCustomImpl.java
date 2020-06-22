package kr.pe.advenoh.quote.repository.quote;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.pe.advenoh.quote.model.dto.QuoteResponseDto;
import kr.pe.advenoh.quote.model.entity.QAuthor;
import kr.pe.advenoh.quote.model.entity.QFolder;
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
     * https://github.com/querydsl/querydsl/blob/master/querydsl-examples/querydsl-example-sql-spring/src/main/java/com/querydsl/example/dao/ProductDaoImpl.java
     *
     * @param folderId
     * @param pageable
     * @return
     */
    @Override
    public Page<QuoteResponseDto> findAllByFolderId(Long folderId, Pageable pageable) {
        QQuote qQuote = QQuote.quote;
        QTag qTag = QTag.tag;
        QAuthor qAuthor = QAuthor.author;
        QFolder qFolder = QFolder.folder;
        QFolderQuoteMapping qFolderQuoteMapping = QFolderQuoteMapping.folderQuoteMapping;
        QQuoteTagMapping qQuoteTagMapping = QQuoteTagMapping.quoteTagMapping;

        QueryResults<QuoteResponseDto> queryResults = queryFactory.select(Projections.fields(QuoteResponseDto.class,
                qQuote.id.as("quoteId"), qQuote.quoteText, qQuote.author.name.as("authorName"), qQuote.useYn))
                .from(qQuote)
                .innerJoin(qFolder).on(qFolder.id.eq(folderId))
                .innerJoin(qFolderQuoteMapping).on(qFolderQuoteMapping.quote.id.eq(qQuote.id)
                        .and(qFolder.id.eq(qFolderQuoteMapping.folder.id)))
                .innerJoin(qAuthor).on(qAuthor.id.eq(qQuote.author.id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(new OrderSpecifier(Order.DESC, qQuote.id))
                .fetchResults();

        List<QuoteResponseDto> quoteResponseDtos = queryResults.getResults().stream()
                .map(it -> {
                    List<Tag> tags = queryFactory.select(Projections.constructor(Tag.class, qTag.tagName))
                            .from(qTag)
                            .innerJoin(qQuoteTagMapping).on(qTag.id.eq(qQuoteTagMapping.tag.id))
                            .where(qQuoteTagMapping.quote.id.eq(it.getQuoteId()))
                            .fetch();
                    it.setTags(tags.stream().map(Tag::getTagName).collect(Collectors.toList()));
                    return it;
                }).collect(Collectors.toList());
        return new PageImpl<>(quoteResponseDtos, pageable, queryResults.getTotal());
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
                qQuote.id.as("quoteId"), qQuote.quoteText, qQuote.author.name.as("authorName"), qQuote.useYn))
                .from(qQuote)
//                .leftJoin(qQuoteTagMapping).on(qQuote.id.eq(qQuoteTagMapping.quote.id))
                .where(qQuote.id.eq(quoteId))
                .fetchOne();

        if (result != null) {
            result.setTags(tags.stream().map(Tag::getTagName).collect(Collectors.toList()));
        }
        return Optional.ofNullable(result);
    }
}

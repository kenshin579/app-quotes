package kr.pe.advenoh.quote.repository;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.pe.advenoh.quote.model.entity.QFolderQuoteMapping;
import kr.pe.advenoh.quote.model.entity.QQuote;
import kr.pe.advenoh.quote.model.entity.Quote;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Slf4j
public class QuoteRepositoryCustomImpl extends QuerydslRepositorySupport implements QuoteRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public QuoteRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        super(Quote.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<Quote> findAllByFolderId(Long folderId, Pageable pageable) {
        QQuote qQuote = QQuote.quote;
        QFolderQuoteMapping qFolderQuoteMapping = QFolderQuoteMapping.folderQuoteMapping;

        JPQLQuery query = from(qQuote)
                .innerJoin(qFolderQuoteMapping)
                .on(qQuote.id.eq(qFolderQuoteMapping.quote.id))
                .where(qFolderQuoteMapping.folder.id.eq(folderId));

        final List<Quote> quotes = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(quotes, pageable, query.fetchCount());
    }
}

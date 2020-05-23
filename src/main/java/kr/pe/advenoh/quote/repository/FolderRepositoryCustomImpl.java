package kr.pe.advenoh.quote.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.pe.advenoh.quote.model.dto.FolderResponseDto;
import kr.pe.advenoh.quote.model.entity.Folder;
import kr.pe.advenoh.quote.model.entity.QFolder;
import kr.pe.advenoh.quote.model.entity.QFolderQuoteMapping;
import kr.pe.advenoh.quote.model.entity.QFolderUserMapping;
import kr.pe.advenoh.quote.model.entity.QUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Slf4j
public class FolderRepositoryCustomImpl extends QuerydslRepositorySupport implements FolderRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public FolderRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        super(Folder.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public List<FolderResponseDto> findAllByUsername(String username) {
        QFolder qFolder = QFolder.folder;
        QFolderUserMapping qFolderUserMapping = QFolderUserMapping.folderUserMapping;
        QFolderQuoteMapping qFolderQuoteMapping = QFolderQuoteMapping.folderQuoteMapping;
        QUser qUser = QUser.user;

        NumberPath<Long> aliasNumOfQuotes = Expressions.numberPath(Long.class, "numOfQuotes");

        List<FolderResponseDto> result = queryFactory
                .select(Projections.constructor(FolderResponseDto.class, qFolder.id, qFolder.folderName, qFolder.id.count().as(aliasNumOfQuotes)))
                .from(qFolder)
                .innerJoin(qFolderUserMapping).on(qFolder.id.eq(qFolderUserMapping.folder.id))
                .innerJoin(qUser).on(qFolderUserMapping.user.id.eq(qUser.id))
                .leftJoin(qFolderQuoteMapping).on(qFolder.id.eq(qFolderQuoteMapping.folder.id))
                .where(qUser.username.eq(username))
                .groupBy(qFolder.id)
                .fetch();
        return result;
    }
}

package kr.pe.advenoh.quote.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.pe.advenoh.quote.model.entity.Folder;
import kr.pe.advenoh.quote.model.entity.QFolder;
import kr.pe.advenoh.quote.model.entity.QFolderUserMapping;
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
    public List<Folder> findAllByUsername(String username) {
        QFolder folder = QFolder.folder;
        QFolderUserMapping folderUserMapping = QFolderUserMapping.folderUserMapping;

        //todo : 이거와 아래와의 차이점은 뭔가?
//        JPQLQuery jpqlQuery = from(folder);
//        return jpqlQuery.fetch();

        return queryFactory.select(folder)
                .from(folder)
                .innerJoin(folderUserMapping).on(folder.id.eq(folderUserMapping.id))
                .where(folderUserMapping.user.username.eq(username))
                .fetch();
    }
}

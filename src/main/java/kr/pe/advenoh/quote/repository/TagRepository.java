package kr.pe.advenoh.quote.repository;

import kr.pe.advenoh.quote.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByTagNameIn(List<String> tagNames);

    @Modifying
    @Query("DELETE FROM Tag t WHERE t.tagName IN :tagNames")
    Integer deleteAllByTagNamesIn(@Param("tagNames") List<String> tagNames);
}

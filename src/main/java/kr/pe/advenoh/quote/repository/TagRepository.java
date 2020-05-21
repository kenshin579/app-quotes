package kr.pe.advenoh.quote.repository;

import kr.pe.advenoh.quote.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByTagNameIn(List<String> tagNames);
}

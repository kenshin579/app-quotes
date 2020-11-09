package kr.pe.advenoh.quote.repository;

import kr.pe.advenoh.quote.model.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> getAuthorByName(String name);
}

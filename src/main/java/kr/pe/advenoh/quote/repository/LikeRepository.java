package kr.pe.advenoh.quote.repository;

import kr.pe.advenoh.quote.model.entity.Like;
import kr.pe.advenoh.quote.model.entity.Quote;
import kr.pe.advenoh.quote.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByQuoteAndUser(Quote quote, User user);

    int deleteLikeByQuoteAndUser(Quote quote, User user);
}

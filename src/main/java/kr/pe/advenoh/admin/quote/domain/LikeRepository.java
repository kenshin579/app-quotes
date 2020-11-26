package kr.pe.advenoh.admin.quote.domain;

import kr.pe.advenoh.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByQuoteAndUser(Quote quote, User user);

    int deleteLikeByQuoteAndUser(Quote quote, User user);
}

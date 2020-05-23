package kr.pe.advenoh.quote.service;

import kr.pe.advenoh.quote.model.entity.Like;
import kr.pe.advenoh.quote.model.entity.Quote;
import kr.pe.advenoh.quote.model.entity.User;
import kr.pe.advenoh.quote.model.enums.YN;
import kr.pe.advenoh.quote.repository.LikeRepository;
import kr.pe.advenoh.quote.repository.QuoteRepository;
import kr.pe.advenoh.quote.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class QuoteLikeService {
    private final QuoteRepository quoteRepository;

    private final LikeRepository likeRepository;

    private final UserRepository userRepository;

    @Transactional
    public YN registerAndGetQuoteLikeInfo(Long quoteId, String username) {
        Quote quote = quoteRepository.findById(quoteId).orElseThrow(() -> {
            throw new RuntimeException("not found");
        });

        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            throw new RuntimeException("not found");
        });

        likeRepository.save(new Like(quote, user));
        return this.getRegisteredQuoteLikeInfo(quoteId, username);
    }

    public YN getRegisteredQuoteLikeInfo(Long quoteId, String username) {
        Quote quote = quoteRepository.findById(quoteId).orElseThrow(() -> {
            throw new RuntimeException("not found");
        });

        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            throw new RuntimeException("not found");
        });

        Optional<Like> quoteLikeOptional = likeRepository.findByQuoteAndUser(quote, user);

        return quoteLikeOptional.isPresent() ? YN.Y : YN.N;
    }

    @Transactional
    public YN unregisterAndGetQuoteLikeInfo(Long quoteId, String username) {
        Quote quote = quoteRepository.findById(quoteId).orElseThrow(() -> {
            throw new RuntimeException("not found");
        });

        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            throw new RuntimeException("not found");
        });

        likeRepository.deleteLikeByQuoteAndUser(quote, user);
        return this.getRegisteredQuoteLikeInfo(quoteId, username);
    }
}


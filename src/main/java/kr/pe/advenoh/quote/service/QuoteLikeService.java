package kr.pe.advenoh.quote.service;

import kr.pe.advenoh.quote.exception.ApiException;
import kr.pe.advenoh.quote.exception.QuoteExceptionCode;
import kr.pe.advenoh.quote.model.entity.Like;
import kr.pe.advenoh.quote.model.entity.Quote;
import kr.pe.advenoh.quote.model.entity.User;
import kr.pe.advenoh.quote.model.enums.YN;
import kr.pe.advenoh.quote.repository.LikeRepository;
import kr.pe.advenoh.quote.repository.quote.QuoteRepository;
import kr.pe.advenoh.quote.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Quote quote = quoteRepository.findById(quoteId).orElseThrow(() -> new ApiException(QuoteExceptionCode.QUOTE_NOT_FOUND));

        User user = userRepository.findByUsername(username).orElseThrow(() -> new ApiException(QuoteExceptionCode.USER_NOT_FOUND));

        likeRepository.save(new Like(quote, user));
        return this.getRegisteredQuoteLikeInfo(quoteId, username);
    }

    @Transactional(readOnly = true)
    public YN getRegisteredQuoteLikeInfo(Long quoteId, String username) {
        Quote quote = quoteRepository.findById(quoteId).orElseThrow(() -> new ApiException(QuoteExceptionCode.QUOTE_NOT_FOUND));

        User user = userRepository.findByUsername(username).orElseThrow(() -> new ApiException(QuoteExceptionCode.USER_NOT_FOUND));

        Optional<Like> quoteLikeOptional = likeRepository.findByQuoteAndUser(quote, user);

        return quoteLikeOptional.isPresent() ? YN.Y : YN.N;
    }

    @Transactional
    public YN unregisterAndGetQuoteLikeInfo(Long quoteId, String username) {
        Quote quote = quoteRepository.findById(quoteId).orElseThrow(() -> new ApiException(QuoteExceptionCode.QUOTE_NOT_FOUND));

        User user = userRepository.findByUsername(username).orElseThrow(() -> new ApiException(QuoteExceptionCode.USER_NOT_FOUND));

        likeRepository.deleteLikeByQuoteAndUser(quote, user);
        return this.getRegisteredQuoteLikeInfo(quoteId, username);
    }
}


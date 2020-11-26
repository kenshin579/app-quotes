package kr.pe.advenoh.admin.quote.service;

import kr.pe.advenoh.admin.quote.domain.Like;
import kr.pe.advenoh.admin.quote.domain.LikeRepository;
import kr.pe.advenoh.admin.quote.domain.Quote;
import kr.pe.advenoh.admin.quote.domain.QuoteRepository;
import kr.pe.advenoh.admin.quote.domain.enums.YN;
import kr.pe.advenoh.common.exception.ApiException;
import kr.pe.advenoh.common.exception.QuoteExceptionCode;
import kr.pe.advenoh.user.domain.User;
import kr.pe.advenoh.user.domain.UserRepository;
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


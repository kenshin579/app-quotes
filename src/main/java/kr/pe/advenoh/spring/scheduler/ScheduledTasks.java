package kr.pe.advenoh.spring.scheduler;

import kr.pe.advenoh.admin.quote.domain.QuoteHistory;
import kr.pe.advenoh.admin.quote.service.QuoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ScheduledTasks {
    private static final String CRON_EXPRESSION_EVERY_5_SECONDS = "*/5 * * * * *";
    private static final String CRON_EXPRESSION_EVERY_DAY = "0 0 0 * * ?";
    private final QuoteService quoteService;

    @Scheduled(cron = CRON_EXPRESSION_EVERY_DAY)
//    @Scheduled(cron = CRON_EXPRESSION_EVERY_5_SECONDS)
//    @Scheduled(fixedRate = 10 * 60 * 1000)
    public QuoteHistory generateDailyRandomQuote() {
        log.debug("[quotedebug] generate daily random quote");
        return quoteService.saveRandomQuote();
    }
}

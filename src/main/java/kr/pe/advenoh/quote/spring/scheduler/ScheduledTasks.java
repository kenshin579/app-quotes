package kr.pe.advenoh.quote.spring.scheduler;

import kr.pe.advenoh.quote.model.entity.QuoteHistory;
import kr.pe.advenoh.quote.service.QuoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScheduledTasks {
    private static final String CRON_EXPRESSION_EVERY_5_SECONDS = "*/5 * * * * *";
    private static final String CRON_EXPRESSION_EVERY_DAY = "0 0 0 * * ?";
    @Autowired
    private QuoteService quoteService;

    @Scheduled(cron = CRON_EXPRESSION_EVERY_DAY)
//    @Scheduled(cron = CRON_EXPRESSION_EVERY_5_SECONDS)
//    @Scheduled(fixedRate = 10 * 60 * 1000)
    public QuoteHistory generateDailyRandomQuote() {
        log.debug("[quotedebug] generate daily random quote");
        return quoteService.saveRandomQuote();
    }
}

package kr.pe.advenoh.spring.scheduler;

import kr.pe.advenoh.admin.quote.domain.QuoteHistory;
import kr.pe.advenoh.util.SpringBootTestSupport;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class ScheduledTasksTest extends SpringBootTestSupport {
    @Autowired
    private ScheduledTasks scheduledTasks;

    @Test
    void generateDailyRandomQuote() {
        QuoteHistory quoteHistory = scheduledTasks.generateDailyRandomQuote();
        assertThat(quoteHistory.getId()).isNotNull();
    }
}
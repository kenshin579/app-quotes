package kr.pe.advenoh.quote.spring.scheduler;

import kr.pe.advenoh.quote.model.entity.QuoteHistory;
import kr.pe.advenoh.quote.util.DefaultSpringTestSupport;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class ScheduledTasksTest extends DefaultSpringTestSupport {
    @Autowired
    private ScheduledTasks scheduledTasks;

    @Test
    void generateDailyRandomQuote() {
        QuoteHistory quoteHistory = scheduledTasks.generateDailyRandomQuote();
        assertThat(quoteHistory.getId()).isNotNull();
    }
}
package kr.pe.advenoh.quote.spring.scheduler;

import kr.pe.advenoh.quote.model.entity.QuoteHistory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ScheduledTasksTest {
    @Autowired
    private ScheduledTasks scheduledTasks;

    @Test
    public void generateDailyRandomQuote() {
        QuoteHistory quoteHistory = scheduledTasks.generateDailyRandomQuote();
        assertThat(quoteHistory.getId()).isNotNull();
    }
}
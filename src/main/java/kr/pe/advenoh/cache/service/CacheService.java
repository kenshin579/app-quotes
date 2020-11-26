package kr.pe.advenoh.cache.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CacheService {
    @Cacheable(value = "10secondCache", key = "#number", condition = "#number>10")
    //	@Cacheable(value = "squareCache", key = "#number", condition = "#number>10")
    public BigDecimal square(Long number) {
        BigDecimal square = BigDecimal.valueOf(number)
                .multiply(BigDecimal.valueOf(number));
        log.info("square of {} is {}", number, square);

        try {
            log.info("sleeping..");
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return square;
    }

    @CacheEvict(cacheNames = "10secondCache", allEntries = true)
    public void clearCache() {
    }
}
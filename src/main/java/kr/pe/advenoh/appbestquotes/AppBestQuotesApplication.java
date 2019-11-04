package kr.pe.advenoh.appbestquotes;

import kr.pe.advenoh.appbestquotes.repository.QuoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AppBestQuotesApplication {
    private static final Logger LOG = LoggerFactory.getLogger(AppBestQuotesApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AppBestQuotesApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(QuoteRepository repo) {
        return args -> {
            LOG.info("> 기존 데이터 삭제...");
            repo.deleteAll();

//            LOG.info("> 데이터를 새로 생성...");
//            repo.save(new Quote("평생 살 것처럼 공부하고, 내일 죽을 것처럼 살아라.", "간디", "2018/10/27", "korean", Arrays.asList("공부")));
//            repo.save(new Quote("좋은 일을 생각하면 좋은 일이 생긴다. 나쁜 일을 생각하면 나쁜 일이 생긴다. 여러분은 여러분이 하루 종일 생각하고 있는, 바로 그것이다", "조셉 머피", "2018/10/27", "korean", Arrays.asList("생각")));

            LOG.info("> 전체 데이터 조회...");
            repo.findAll().forEach(entry -> LOG.info(entry.toString()));

//            LOG.info("> 저자 데이터 조회...");
//            repo.findByAuthor("간디").forEach(entry -> LOG.info(entry.toString()));
        };
    }
}

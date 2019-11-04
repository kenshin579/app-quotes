package kr.pe.advenoh.appbestquotes.repository;

import kr.pe.advenoh.appbestquotes.domain.Quote;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface QuoteRepository extends MongoRepository<Quote, String> {
    List<Quote> findByAuthor(String author);
}

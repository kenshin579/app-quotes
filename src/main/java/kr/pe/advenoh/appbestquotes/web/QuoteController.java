package kr.pe.advenoh.appbestquotes.web;

import kr.pe.advenoh.appbestquotes.domain.Quote;
import kr.pe.advenoh.appbestquotes.repository.QuoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class QuoteController {
    private static final Logger LOG = LoggerFactory.getLogger(QuoteController.class);

    @Autowired
    private QuoteRepository repo;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Quote> getAllQuotes() {
        //todo: 이건 제공하면 안됨. 모든 quotes가 오픈되기 때문에
        return repo.findAll();
    }

    @RequestMapping(value = "/quote", method = RequestMethod.POST)
    Quote newQuote(@RequestBody Quote newQuote) {
        //todo: 중복되는 것에 대한 exception 처리
        return repo.save(newQuote);
    }

    @RequestMapping(value = "/quotes", method = RequestMethod.POST)
    public void newQuotes(@RequestBody List<Quote> quotes) {
        //todo: 중복되는 것에 대한 exception 처리
        repo.saveAll(quotes);
    }

    @RequestMapping(value = "/quote", method = RequestMethod.GET)
    public List<Quote> getQuotesByAuthor(
            @RequestParam(value = "author") String author) {
        return repo.findByAuthor(author);
    }

    @RequestMapping(value = "/random", method = RequestMethod.GET)
    public Quote getRandomQuote() {
//        SampleOperation sampleStage = Aggregation.sample(5);
//        Aggregation aggregation = Aggregation.newAggregation(sampleStage);
//        AggregationResults<Quote> output = repo.aggregate(aggregation, "collectionName", Quote.class);

        return null;
    }
}

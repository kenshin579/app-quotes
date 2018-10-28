package com.advenoh.appbestquotes.web;

import com.advenoh.appbestquotes.domain.Quote;
import com.advenoh.appbestquotes.repository.QuoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    QuoteRepository repo;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Quote> getAllQuotes() {
        return repo.findAll();
    }

    @RequestMapping(value = "/quote", method = RequestMethod.GET)
    public List<Quote> getQuotesByAuthor(
            @RequestParam(value = "author") String author) {
        return repo.findByAuthor(author);
    }
}

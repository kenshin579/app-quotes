package com.advenoh.appbestquotes.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Transient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Quote {
    private static final Logger LOG = LoggerFactory.getLogger(Quote.class);

    //    private Long id;
    private String quote;
    private String author;
    private Date created;
    private String lang;
    private List<String> tags;

    @Transient
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public Quote() {
    }

    public Quote(String quote, String author, String created, String lang, List<String> tags) throws ParseException {
        this.quote = quote;
        this.author = author;
        this.created = format.parse(created);
        this.lang = lang;
        this.tags = tags;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCreatedAsShort() {
        return format.format(created);
    }

    @Override
    public String toString() {
        return "Quote{" +
                "quote='" + quote + '\'' +
                ", author='" + author + '\'' +
                ", created=" + getCreatedAsShort() +
                ", lang='" + lang + '\'' +
                ", tags=" + tags +
                '}';
    }
}

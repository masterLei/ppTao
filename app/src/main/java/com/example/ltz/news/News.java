package com.example.ltz.news;

/**
 * Created by ltz on 2016/5/10.
 */
public class News {
    private String title;
    private String abstractBody;
    private String author;


    public News() {
    }

    public News(String title, String abstractBody, String author) {
        this.title = title;
        this.abstractBody = abstractBody;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstractBody() {
        return abstractBody;
    }

    public void setAbstractBody(String abstractBody) {
        this.abstractBody = abstractBody;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}

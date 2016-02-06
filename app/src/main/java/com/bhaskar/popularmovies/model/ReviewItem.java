package com.bhaskar.popularmovies.model;

/**
 * Created by bhaskar on 6/2/16.
 */
public class ReviewItem {
    public String author, comment;

    public ReviewItem(String author, String comment) {
        this.author = author;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}

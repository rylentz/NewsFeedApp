package com.ryanlentz.newsfeedapp;

/**
 * A class that holds the basic contents of a news article
 */
public class NewsArticle {
    /**
     * Holds the article's title
     */
    String mTitle;
    /**
     * Holds the article's news section
     */
    String mSection;
    /**
     * Holds the article's date
     */
    String mDate;

    /**
     * Holds the web address of the full article
     */
    String mUrl;

    public NewsArticle(String title, String section, String date, String url) {
        mTitle = title;
        mSection = section;
        mDate = date;
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getDate() {
        return mDate;
    }

    public String getUrl() {
        return mUrl;
    }
}

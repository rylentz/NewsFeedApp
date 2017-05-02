package com.ryanlentz.newsfeedapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Loads a list of NewsArticles by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class NewsLoader extends AsyncTaskLoader<List<NewsArticle>> {
    /**
     * Query url
     */
    private String mUrl;


    /**
     * Constructs a new NewsLoader
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    /**
     * Forces the loader to start loading
     */
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * Completes task on the background thread
     *
     * @return a list of NewsArticle objects
     */
    @Override
    public List<NewsArticle> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        return Utils.fetchNewsArticleData(mUrl);
    }
}

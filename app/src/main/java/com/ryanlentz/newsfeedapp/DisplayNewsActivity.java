package com.ryanlentz.newsfeedapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DisplayNewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsArticle>> {

    /**
     * String for log
     */
    public static final String LOG_TAG = DisplayNewsActivity.class.getName();
    /**
     * Constant value for the news loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWS_LOADER_ID = 1;
    /**
     * URL for news data from the Guardin API
     */
    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?api-key=test";
    /**
     * String to hold the query from SearchActivity
     */
    private String query;
    /**
     * Adapter for the list of NewsArticles
     */
    private NewsArticleAdapter mAdapter;
    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;
    /**
     * ProgressBar to let the user know that data is loading
     */
    private ProgressBar mProgressSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // Get the intent from the SearchActivity
        Intent intent = getIntent();

        // Get the query string from the intent
        query = intent.getStringExtra("query");

        // Find a reference to the ProgressBar in the layout
        mProgressSpinner = (ProgressBar) findViewById(R.id.progress_spinner);

        // Find a reference to the ListView in the layout
        ListView newsListView = (ListView) findViewById(R.id.list);

        // Find a reference to the TextView in the layout
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);

        // setEmptyView on the earthquakeListView
        newsListView.setEmptyView(mEmptyStateTextView);

        // Create a NewsArticleAdapter that takes an empty list of NewsArticle objects
        mAdapter = new NewsArticleAdapter(this, new ArrayList<NewsArticle>());

        // Set the adapter on the ListView
        newsListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected NewsArticle
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected NewsArticle
                NewsArticle currentNewsArticle = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsArticleUri = Uri.parse(currentNewsArticle.getUrl());

                // Create a new Intent to open a browser and view the full article
                Intent intent = new Intent(Intent.ACTION_VIEW, newsArticleUri);

                // Send the intent to launch a browser
                startActivity(intent);
            }
        });

        // Get a reference to the ConnectivityManager, in order to check connectivity
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Use NetworkInfo to check the connectivity
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        // Set a boolean based on the connectivity
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        // Check the internet status. If true, initialize the loader, otherwise let the user know
        // that there is no internet.
        if (isConnected) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            // Remove the ProgressBar
            mProgressSpinner.setVisibility(View.GONE);

            // Set the TextView
            mEmptyStateTextView.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader<List<NewsArticle>> onCreateLoader(int i, Bundle bundle) {
        // If no query was entered, just get the latest news,
        // otherwise append the query to the base URL
        if(TextUtils.isEmpty(query)) {
            // Create and return a NewsLoader to the Loader with the base URL
            return new NewsLoader(this, GUARDIAN_REQUEST_URL);
        } else {
            // Set the base URL
            Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);

            // Create a URI builder
            Uri.Builder uriBuilder = baseUri.buildUpon();

            // Append the query to the base URL
            uriBuilder.appendQueryParameter(getString(R.string.search_parameter), query);

            Log.i("URL", uriBuilder.toString());

            // Create and return a NewsLoader to the Loader with the new URL
            return new NewsLoader(this, uriBuilder.toString());
        }
    }

    @Override
    public void onLoadFinished(Loader<List<NewsArticle>> loader, List<NewsArticle> data) {
        // Set the ProgressBar to GONE when loading has finished
        mProgressSpinner.setVisibility(View.GONE);

        // Set empty state text to display "Terribly sorry, but no news has been found."
        mEmptyStateTextView.setText(R.string.no_news);

        // Clear the adapter of previous news data
        mAdapter.clear();

        // If there is a valid list, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsArticle>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}

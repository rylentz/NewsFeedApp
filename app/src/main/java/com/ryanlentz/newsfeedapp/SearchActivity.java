package com.ryanlentz.newsfeedapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;


public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Find the EditText with the ID search_text
        final EditText searchText = (EditText) findViewById(R.id.search_text);

        // Delete text from searchText in case the user returns to the SearchActivity
        searchText.setText("");

        // Find the ImageView with the ID search_view
        ImageView searchView = (ImageView) findViewById(R.id.search_view);

        // Set an OnClickListener on the searchView that sends an Intent to DisplayNewsActivity
        // to display news articles based on the user's query.
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the string from searchText
                String query = searchText.getText().toString();

                // Create an intent to launch the DisplayNewsActivity
                Intent intent = new Intent(getApplicationContext(), DisplayNewsActivity.class);

                // Add the query to the intent
                intent.putExtra("query", query);
                // Launch the DisplayNewsActivity
                startActivity(intent);
            }
        });
    }
}

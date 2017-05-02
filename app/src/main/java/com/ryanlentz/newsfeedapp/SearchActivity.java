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

        // Find the ImageView with the ID search_view
        ImageView searchView = (ImageView) findViewById(R.id.search_view);

        // Set an OnClickListener on the searchView that sends an Intent to DisplayNewsActivity
        // to display news articles based on the user's query.
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchText.getText().toString();
                searchText.setText("");
                Intent intent = new Intent(getApplicationContext(), DisplayNewsActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
            }
        });
    }
}

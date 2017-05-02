package com.ryanlentz.newsfeedapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * NewsArticleAdapter is an ArrayAdapter that can provide the layout based on a list of NewsArticle
 * objects.
 */
public class NewsArticleAdapter extends ArrayAdapter<NewsArticle> {

    /**
     * A custom constructor
     *
     * @param context The current context used to inflate the layout.
     * @param objects The list of NewsArticle objects
     */
    public NewsArticleAdapter(@NonNull Context context, @NonNull List<NewsArticle> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the NewArticle at the current position
        NewsArticle currentArticle = getItem(position);

        // Find the TextView with the ID section
        TextView sectionView = (TextView) listItemView.findViewById(R.id.section);

        // Set the text of sectionView
        sectionView.setText(currentArticle.getSection());

        // Find the TextView with the ID section
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);

        // Format the date and set the text of dateView
        dateView.setText(formatDate(currentArticle.getDate()));

        // Find the TextView with the ID section
        TextView titleView = (TextView) listItemView.findViewById(R.id.title);

        // Format the date and set the text of dateView
        titleView.setText(currentArticle.getTitle());

        return listItemView;
    }

    /**
     * Format the date
     *
     * @param date The raw date
     * @return Returns the date is the proper format for this app
     */
    private String formatDate(String date) {
        Log.i("Date", date);
        String split1[] = date.split("T");
        Log.i("Date", split1[0]);
        String split2[] = split1[0].split("-");
        return split2[1] + "-" + split2[2] + "-" + split2[0];
    }
}

package com.example.flixster.model;

import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.main.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    String backdropPath;
    String posterPath;
    String title;
    String overview;
    String rating;
    int maxLength = 150; //item_movie.xml line 41  tools:maxLength="150"

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w780/%s",backdropPath);
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
//        if (overview.length()>maxLength)
//            return overview.substring(0, maxLength-3)+"...";
//        else
        return overview;
    }

    public String getRating() {
        return rating + "/10";
    }

    public String[] getPosterSizes(JSONObject jsonObject) throws JSONException  {
        return jsonObject.getString("poster_sizes").split(",");
    }

    public Movie(JSONObject jsonObject) throws JSONException {
        backdropPath = jsonObject.getString("backdrop_path");
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        rating = jsonObject.getString("vote_average");
    }

    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<Movie>();
        for (int i=0; i < movieJsonArray.length(); i++){
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    private void addReadMore(final String text, final TextView textView) {
        SpannableString ss = new SpannableString(text.substring(0, maxLength) + "... read more");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                addReadLess(text, textView);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    ds.setColor(getResources().getColor(R.color.color_primary, getTheme()));
//                } else {
//                    ds.setColor(getResources().getColor(R.color.color_primary));
//                }
            }
        };
        ss.setSpan(clickableSpan, ss.length() - 10, ss.length() , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void addReadLess(final String text, final TextView textView) {
        SpannableString ss = new SpannableString(text + " read less");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                addReadMore(text, textView);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    ds.setColor(getResources().getColor(R.color.color_primary, getTheme()));
//                } else {
//                    ds.setColor(getResources().getColor(R.color.color_primary));
//                }
            }
        };
        ss.setSpan(clickableSpan, ss.length() - 10, ss.length() , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
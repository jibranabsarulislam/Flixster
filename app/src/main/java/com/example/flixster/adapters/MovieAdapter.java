package com.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Parcel;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.main.DetailActivity;
import com.example.main.R;
import com.example.flixster.model.Movie;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context;
    List<Movie> movies;
    final int maxLength = 180;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHodler" + position);
        Movie movie = movies.get(position);
        if (position%2==0)
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        else
            holder.itemView.setBackgroundColor(Color.parseColor("#e3e3e2"));
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    // addReadMore and addReadLess methods
    // found and implemented from https://pasanlive.com/2017/03/12/add-read-more-less-to-android-textview/

    private void addReadMore(final String text, final TextView textView) {
        if(text.length()>maxLength) {
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
            ss.setSpan(clickableSpan, ss.length() - 10, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(ss);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
        else
            textView.setText(text);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout container;
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
        TextView rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            rating = itemView.findViewById(R.id.rating);
            container = itemView.findViewById(R.id.container);

        }

        public void bind(Movie movie) {
            rating.setText(""+movie.getRating());
            tvTitle.setText(movie.getTitle());
            addReadMore(movie.getOverview(), tvOverview);
//            tvOverview.setText(movie.getOverview());

            String imgUrl;
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imgUrl = movie.getBackdropPath();
            }
            else {
                imgUrl = movie.getPosterPath();
            }
            Glide.with(context).load(imgUrl).into(ivPoster);
            container.setOnClickListener(new View.OnClickListener() { // listens for click on the title element
                @Override
                public void onClick(View v) { //navigate to another activity
                    //intent and start
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("movie", Parcels.wrap(movie));
                    context.startActivity(i);
                }
            });


        }
    }
}
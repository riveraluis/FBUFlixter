package com.example.flixter;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.flixter.models.Movie;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;
    TextView tvOverview;
    TextView tvTitle;
    RatingBar rbVoteAverage;
    ImageView ivPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        rbVoteAverage = findViewById(R.id.rbVoteAverage);
        ivPoster = findViewById(R.id.ivPoster);

        // unwrap movie passed via intent
        movie = Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s", movie.getTitle()));

        // set title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        // vote average is 0...10 which will be converted to 0..5
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage / 2.0f);

        String imageURL;
        int placeHolderKey;
        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageURL = movie.getBackdropPath();
            placeHolderKey = R.drawable.placeholder;
        }
        else {
            imageURL = movie.getPosterPath();
            placeHolderKey = R.drawable.backdrop;
        }

        int radius = 40; // corner radius, higher value = more rounded
        int margin = 0; // crop margin, set to 0 for corners with no crop
        Glide.with(getApplicationContext()).load(imageURL).placeholder(placeHolderKey).transform(new RoundedCornersTransformation(radius, margin))
                .into(ivPoster);


        // set click functionality on poster
        ivPoster.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MovieTrailerActivity.class);
                intent.putExtra("id", movie.getId());
                startActivity(intent);
            }
        });
    }
}
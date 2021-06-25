package com.example.flixter.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixter.MovieDetailsActivity;
import com.example.flixter.R;
import com.example.flixter.models.Movie;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    // Usually involves inflating a layout from XML and returning the holder.
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View moviewView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        Log.d("MovieAdapter", "onCreateViewHolder");
        return new ViewHolder(moviewView);
    }

    // Involves populating data into the item through holder.
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder " + position);
        // Get the movie at position
        Movie movie = movies.get(position);
        // Bind movie data into view holder
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageURL;
            int placeHolderKey;
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageURL = movie.getBackdropPath();
                placeHolderKey = R.drawable.placeholder;
            }
            else {
                imageURL = movie.getPosterPath();
                placeHolderKey = R.drawable.backdrop;
            }

            int radius = 40; // corner radius, higher value = more rounded
            int margin = 0; // crop margin, set to 0 for corners with no crop
            Glide.with(context).load(imageURL).placeholder(placeHolderKey).transform(new RoundedCornersTransformation(radius, margin))
                    .into(ivPoster);
            //Glide.with(context).load(imageURL).placeholder(placeHolderKey).into(ivPoster);
        }

        // when the user clicks on a row show MovieDetailsActivity for the selected movie
        @Override
        public void onClick(View v) {
            // get position of item
            int position = getAdapterPosition();
            // check if position is valid
            if (position != RecyclerView.NO_POSITION) {
                // get movie at position
                Movie movie = movies.get(position);
                // create intent for new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // serialize movie using parceler
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // show activity
                context.startActivity(intent);
            }
        }
    }

}

package com.example.flixter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixter.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

public class MovieTrailerActivity extends YouTubeBaseActivity {

    public static final String youtube_api_key = "AIzaSyA3f5f1CuUdfnnF8N6fpFkaykO4JSPNxoE";
    public final String YOUTUBE_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    Movie movie;
    String key;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);

        // movie = Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Integer mId = getIntent().getIntExtra("id", 0);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(YOUTUBE_URL, mId), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d("Youtube API Call", "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    if (results.length() == 0)
                        return;
                    key = results.getJSONObject(0).getString("key");
                    Log.d("hello", key);
                    initVideo(key);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d("Failure", "onFailure");
            }
        });

        // temporary test video id -- TODO replace with movie trailer video id
        //final String videoId = String.valueOf(movie.getId());

    }

    private void initVideo(String key) {
        // resolve the player view from the layout
        YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);

        // initialize with API key stored in secrets.xml
        playerView.initialize(youtube_api_key, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b) {
                // do any work here to cue video, play video, etc.
                youTubePlayer.cueVideo(key);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
                // log the error
                Log.e("MovieTrailerActivity", "Error initializing YouTube player");
            }
        });
    }

}
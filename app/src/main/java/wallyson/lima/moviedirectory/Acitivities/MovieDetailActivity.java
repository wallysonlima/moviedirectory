package wallyson.lima.moviedirectory.Acitivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import wallyson.lima.moviedirectory.Model.Movie;
import wallyson.lima.moviedirectory.R;
import wallyson.lima.moviedirectory.Util.Constants;

public class MovieDetailActivity extends AppCompatActivity {
    private Movie movie;
    private TextView movieTitle, movieYear, director, actors, category, rating, writers, plot, boxOffice, runtime;
    private ImageView movieImage;

    private RequestQueue queue;
    private String movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        queue = Volley.newRequestQueue(this);

        movie = (Movie) getIntent().getSerializableExtra("movie");
        movieId = movie.getImdbId();

        setUpUI();
        getMoviesDetails(movieId);
    }

    private void getMoviesDetails(String id) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.URL + id, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.has("Ratings")) {
                        JSONArray ratings = response.getJSONArray("Ratings");

                        String source = null;
                        String value = null;

                        if (ratings.length() > 0) {
                            JSONObject mRatings = ratings.getJSONObject(ratings.length() - 1);
                            source = mRatings.getString("Source");
                            value = mRatings.getString("Value");
                            rating.setText(source + " : " + value);
                        } else {
                            rating.setText("Rating: N/A");
                        }

                        movieTitle.setText(response.getString("Title"));
                        movieYear.setText("Released: " + response.getString("Released"));
                        director.setText("Director: " + response.getString("Director"));
                        writers.setText("Writers: " + response.getString("Writer"));
                        plot.setText("Plot: " + response.getString("Plot"));
                        runtime.setText("Runtime: " + response.getString("Runtime"));
                        actors.setText("Actors: " + response.getString("Actors"));

                        Picasso.with(getApplicationContext())
                                .load(response.getString("Poster"))
                                .into(movieImage);

                        boxOffice.setText("Box Office " + response.getString("BoxOffice"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: ", error.getMessage());
            }
        });

        queue.add(jsonObjectRequest);
    }

    private void setUpUI() {
        movieTitle = findViewById(R.id.movieTitleIDDet);
        movieImage = findViewById(R.id.movieImageIDDet);
        movieYear = findViewById(R.id.movieReleaseIDDet);
        director = findViewById(R.id.directedByDet);
        actors = findViewById(R.id.actorsDet);
        category = findViewById(R.id.movieCatID);
        rating = findViewById(R.id.movieRatingIDDet);
        writers = findViewById(R.id.writersDet);
        plot = findViewById(R.id.plotDet);
        boxOffice = findViewById(R.id.boxOfficeDet);
        runtime = findViewById(R.id.runtimeDet);
    }
}

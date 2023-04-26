package com.example.recyclerviewexample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rv: RecyclerView = findViewById(R.id.rv)
        rv.layoutManager = LinearLayoutManager(this)

        val jsonString = loadJsonFromAsset("movies.json")
        val movies: List<Movie> =
            Gson().fromJson(jsonString, object : TypeToken<List<Movie>>() {}.type)

        rv.adapter = MovieAdapter(movies)
    }

    private fun loadJsonFromAsset(filename: String): String? {
        return try {
            val inputStream = assets.open(filename)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
            bufferedReader.close()
            inputStream.close()
            stringBuilder.toString()
        } catch (exception: Exception) {
            exception.printStackTrace()
            null
        }
    }
}


class MovieAdapter(private val movies: List<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    // todo: use binding here to avoid name conflicts
    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val ratingTextView: TextView = itemView.findViewById(R.id.ratingTextView)
        val votesTextView: TextView = itemView.findViewById(R.id.votesTextView)
        val ratedTextView: TextView = itemView.findViewById(R.id.ratedTextView)
        val posterImageView: ImageView = itemView.findViewById(R.id.posterImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.titleTextView.text = movie.title
        holder.ratingTextView.text = "Rating: ${movie.rating}"
        holder.votesTextView.text = "Votes: ${movie.votes}"
        holder.ratedTextView.text = "Rated: ${movie.rated ?: "N/A"}"
        Glide.with(holder.itemView.context)
            .load(movie.posterUrl)
            .into(holder.posterImageView)
    }

    override fun getItemCount() = movies.size
}

data class Movie(
    val title: String,
    val year: Int,
    val rated: String,
    val runtime: String,
    val genre: String,
    val director: String,
    val actors: String,
    val plot: String,
    @SerializedName("poster")
    val posterUrl: String?,
    @SerializedName("imdbRating")
    val rating: Double,
    @SerializedName("imdbVotes")
    val votes: String
)


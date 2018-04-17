package haroldolivieri.moviescatalog.features.movies

import android.app.Activity
import android.content.Context
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import haroldolivieri.moviescatalog.R
import haroldolivieri.moviescatalog.domain.Movie
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class MovieAdapter(private var movies: MutableList<Movie>? = null,
                   private val context: Context,
                   private val favClick: (favored: Boolean, movie: Movie) -> Unit,
                   private val itemClick: (movie: Movie,
                                           options: ActivityOptionsCompat) -> Unit?) :
        RecyclerView.Adapter<MovieAdapter.MoviesViewHolder>() {

    val favItems: MutableMap<Int, Boolean> = HashMap()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie_list, parent, false)
        return MoviesViewHolder(view)
    }

    fun setMovies(movies: List<Movie>?) {
        this.movies = movies as MutableList<Movie>?
        refreshFavMap(movies)
        notifyDataSetChanged()
    }

    private fun refreshFavMap(newItems: List<Movie>?) {
        newItems?.map {
            it.id?.let { it1 ->
                it.favored?.let { it2 ->
                    favItems.put(it1, it2)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        movies?.get(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = movies?.size ?: 0

    inner class MoviesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val title by lazy { view.findViewById<TextView>(R.id.movieTitle) }
        val year by lazy { view.findViewById<TextView>(R.id.movieReleaseYear) }
        val voteAverage by lazy { view.findViewById<TextView>(R.id.voteAverage) }
        val favAction by lazy { view.findViewById<CheckBox>(R.id.favoriteButton) }
        val image by lazy { view.findViewById<ImageView>(R.id.movieImageView) }
        val adultImage by lazy { view.findViewById<ImageView>(R.id.adultImageStatus) }
        val voteContainer by lazy { view.findViewById<View>(R.id.voteContainer) }
        val favoriteContainer by lazy { view.findViewById<View>(R.id.favoriteContainer) }

        fun bind(movie: Movie) {
            populateMovieData(movie)

            if (layoutPosition%2 == 1) {
                favoriteContainer.setBackgroundColor(context.getColor(R.color.black))
                voteContainer.setBackgroundColor(context.getColor(R.color.blackTransparency))
            } else {
                favoriteContainer.setBackgroundColor(context.getColor(R.color.yellow))
                voteContainer.setBackgroundColor(context.getColor(R.color.yellowTransparency))
            }

            favAction.setOnClickListener { v ->
                val checked = (v as CheckBox).isChecked
                movie.id?.let { favItems.put(it, checked) }
                favClick(checked, movie)
            }

            itemView.setOnClickListener {
                itemClick(movie, animationTransitionSetup())
            }
        }

        private fun populateMovieData(movie: Movie) {
            val options = RequestOptions()
                    .priority(Priority.HIGH)

            Glide.with(context)
                    .load(movie.backDropPath)
                    .apply(options)
                    .into(image)

            title.text = "#${movie.position}  ${movie.title}"
            year.text = movie.releaseDate?.formatToString("yyyy")
            voteAverage.text = "${movie.voteAverage}"
            favAction.isChecked = favItems[movie.id]!!

            if (movie.adult!!) {
                adultImage.setImageResource(R.drawable.ic_clapperboard_adult)
            } else {
                adultImage.setImageResource(R.drawable.ic_clapperboard)
            }
        }

        private fun animationTransitionSetup(): ActivityOptionsCompat {
            val p1 = Pair(title as View,
                    context.getString(R.string.title_transition))
            val p2 = Pair(year as View,
                    context.getString(R.string.year_transition))
            val p3 = Pair(voteContainer as View,
                    context.getString(R.string.vote_transition))
            val p4 = Pair(favAction as View,
                    context.getString(R.string.favorite_transition))
            val p5 = Pair(favoriteContainer as View,
                    context.getString(R.string.fav_container_transition))
            val options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(context as Activity, p1, p2, p3, p4, p5)
            return options
        }
    }
}

fun Date.formatToString(pattern: String): String {
    val df = SimpleDateFormat(pattern)
    return df.format(this)
}
package haroldolivieri.moviescatalog.features.movies

import android.content.Context
import android.support.v4.app.ActivityOptionsCompat
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


class MoviesAdapter(private var movies: MutableList<Movie>? = null,
                    private val context: Context,
                    private val favClick: (favored: Boolean, movie: Movie) -> Unit,
                    private val itemClick: (movie: Movie,
                                            options: ActivityOptionsCompat) -> Unit?) :
        RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie_list, parent, false)
        return MoviesViewHolder(view, itemClick, favClick, context)
    }

    fun setMovies(movies: List<Movie>?) {
        if (this.movies == null) {
            this.movies = movies as MutableList<Movie>?
        } else {
            movies?.let { this.movies!!.addAll(it) }
        }

        this.movies?.distinct()

        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        movies?.get(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = movies?.size ?: 0

    class MoviesViewHolder(view: View,
                           private val itemClick: (movie: Movie,
                                                   options: ActivityOptionsCompat) -> Unit?,
                           private val favClick: (favored: Boolean, movie: Movie) -> Unit,
                           private val context: Context) : RecyclerView.ViewHolder(view) {

        val title by lazy { view.findViewById<TextView>(R.id.movieTitle) }
        val year by lazy { view.findViewById<TextView>(R.id.movieReleaseYear) }
        val voteAverage by lazy { view.findViewById<TextView>(R.id.voteAverage) }
        val favAction by lazy { view.findViewById<CheckBox>(R.id.favoriteButton) }
        val image by lazy { view.findViewById<ImageView>(R.id.movieImageView) }

        fun bind(movie: Movie) {

            val options = RequestOptions()
                    .priority(Priority.HIGH)

            Glide.with(context)
                    .load(movie.backDropPath)
                    .apply(options)
                    .into(image)

            title.text = movie.title
            year.text = movie.releaseDate?.formatToString("yyyy")
            voteAverage.text = "${movie.voteAverage}"
            favAction.isChecked = movie.favored!!

            favAction.setOnCheckedChangeListener { _, checked -> favClick(checked, movie) }
            itemView.setOnClickListener {
                //                val p1 = android.support.v4.util.Pair(badAssImage as View,
//                        context.getString(R.string.badass_image_transition_name))
//                val p2 = android.support.v4.util.Pair(badAssName as View,
//                        context.getString(R.string.badass_name_transition_name))
//                val p3 = android.support.v4.util.Pair(contentName as View,
//                        context.getString(R.string.content_transition_name))
//                val options = ActivityOptionsCompat
//                        .makeSceneTransitionAnimation(context as Activity?, p1, p2, p3)
//
//                itemClick(movie, options)
            }
        }
    }
}

fun Date.formatToString(pattern: String): String {
    val df = SimpleDateFormat(pattern)
    return df.format(this)
}
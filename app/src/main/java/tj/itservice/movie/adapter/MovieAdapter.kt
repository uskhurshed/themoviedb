package tj.itservice.movie.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import tj.itservice.movie.R
import tj.itservice.movie.data.MovieResult
import tj.itservice.movie.interfaces.DetailsListener
import tj.itservice.movie.utils.ApiHelper

class MovieAdapter(private val listener: DetailsListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val typeRegular = 1
    private val typeShimmer = 2

    var movieList:ArrayList<MovieResult> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = with(parent) {
        return when (viewType) {
            typeRegular -> MovieViewHolder(LayoutInflater.from(context).inflate(R.layout.item_movies, this, false))
            typeShimmer -> ShimmerViewHolder(LayoutInflater.from(context).inflate(R.layout.shimmer_item_movies, this, false))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = with(holder) {
        when (this) {
            is MovieViewHolder -> {
                val movieResult = movieList[position]

                Glide.with(itemView.context)
                    .load(ApiHelper.BASE_POSTER_PATH + movieResult.posterPath)
                    .placeholder(R.drawable.ic_movie)
                    .into(imageView)

                ratingTextView.text = movieResult.voteAverage.toString()
                langTextView.text = movieResult.originalLanguage
                itemView.setOnClickListener { listener.setClick(movieResult.id) }
            }

            is ShimmerViewHolder -> shimmerLayout.startShimmer()
        }
    }
    override fun getItemCount(): Int {
        return when {
            movieList.isEmpty() -> 6
            else -> movieList.size
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addList(mv: List<MovieResult>){
        movieList.addAll(mv)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (movieList.isEmpty()) typeShimmer
        else typeRegular
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val ratingTextView: TextView = itemView.findViewById(R.id.tv_rating)
        val langTextView: TextView = itemView.findViewById(R.id.tv_lang)
    }

    class ShimmerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val shimmerLayout: ShimmerFrameLayout = itemView.findViewById(R.id.movieCard)
    }


}

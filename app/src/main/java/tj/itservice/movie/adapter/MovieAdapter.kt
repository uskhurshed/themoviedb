package tj.itservice.movie.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import tj.itservice.movie.R
import tj.itservice.movie.activities.details.DetailsActivity
import tj.itservice.movie.data.MovieResult
import tj.itservice.movie.utils.ApiHelper

class MovieAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val typeRegular = 1
    private val typeShimmer = 2
    var movieList:ArrayList<MovieResult> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            typeRegular -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movies, parent, false)
                MovieViewHolder(view)
            } typeShimmer -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.shimmer_item_movies, parent, false)
                ShimmerViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieViewHolder -> {
                val movieResult = movieList[position]

                Glide.with(holder.itemView.context)
                    .load(ApiHelper.BASE_POSTER_PATH + movieResult.posterPath)
                    .placeholder(R.drawable.ic_movie)
                    .into(holder.imageView)

                holder.ratingTextView.text = movieResult.voteAverage.toString()
                holder.langTextView.text = movieResult.originalLanguage
                holder.itemView.setOnClickListener {
                    openActivity(holder.itemView.context,movieResult.id)
                }
            }
            is ShimmerViewHolder -> {
                holder.shimmerLayout.startShimmer()
            }
        }
    }
    override fun getItemCount(): Int {
        return when {
            movieList.isEmpty() -> 6
            else -> movieList.size
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addList(mv: ArrayList<MovieResult>){
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

    private fun openActivity(context:Context, id: Long?){
        val intent = Intent(context, DetailsActivity::class.java)
        intent.putExtra("id",id)
        context.startActivity(intent)
    }


}

package tj.itservice.movie.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tj.itservice.movie.R
import tj.itservice.movie.data.MovieResult
import tj.itservice.movie.interfaces.DetailsListener
import tj.itservice.movie.utils.ApiHelper

@SuppressLint("NotifyDataSetChanged")
class DiscoverAdapter(private val listener: DetailsListener) : RecyclerView.Adapter<DiscoverAdapter.MovieViewHolder>() {

    private var movieList: ArrayList<MovieResult> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder = with(parent) {
        return MovieViewHolder(LayoutInflater.from(context).inflate(R.layout.item_discover, this, false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) = with(holder) {
        val movieResult = movieList[position]

        Glide.with(itemView.context)
            .load(ApiHelper.BASE_POSTER_PATH + movieResult.posterPath)
            .placeholder(R.drawable.ic_movie)
            .into(imageView)

        name.text = movieResult.title
        date.text = movieResult.releaseDate
        detail.text = movieResult.overview

        itemView.setOnClickListener { listener.setClick(movieResult.id) }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    fun addList(mv: List<MovieResult>) {
        movieList.addAll(mv)
        notifyDataSetChanged()
    }

    fun setList(mv: List<MovieResult>) = with(movieList) {
        clear()
        addAll(mv)
        notifyDataSetChanged()
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        val imageView: ImageView = itemView.findViewById(R.id.image)
        val name: TextView = itemView.findViewById(R.id.name)
        val detail: TextView = itemView.findViewById(R.id.details)
        val date: TextView = itemView.findViewById(R.id.date_picker_actions)
    }
}


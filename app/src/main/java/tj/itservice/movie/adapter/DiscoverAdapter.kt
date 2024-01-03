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
import tj.itservice.movie.ui.interfaces.DetailsListener
import tj.itservice.movie.utils.ApiHelper

class DiscoverAdapter : RecyclerView.Adapter<DiscoverAdapter.MovieViewHolder>() {

    var movieList: ArrayList<MovieResult> = ArrayList()
    var mListener: DetailsListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_discover, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movieResult = movieList[position]

        Glide.with(holder.itemView.context)
            .load(ApiHelper.BASE_POSTER_PATH + movieResult.posterPath)
            .placeholder(R.drawable.ic_movie)
            .into(holder.imageView)

        holder.name.text = movieResult.title.toString()
        holder.date.text = movieResult.releaseDate.toString()
        holder.detail.text = movieResult.overview

        holder.itemView.setOnClickListener {
            mListener?.setClick(movieResult.id)
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addList(mv: List<MovieResult>) {
        movieList.addAll(mv)
        notifyDataSetChanged()
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image)
        val name: TextView = itemView.findViewById(R.id.name)
        val detail: TextView = itemView.findViewById(R.id.details)
        val date: TextView = itemView.findViewById(R.id.date_picker_actions)
    }
}


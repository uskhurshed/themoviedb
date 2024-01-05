package tj.itservice.movie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tj.itservice.movie.R
import tj.itservice.movie.data.MovieResult
import tj.itservice.movie.interfaces.DetailsListener
import tj.itservice.movie.utils.ApiHelper

class PagerAdapter (private val listener: DetailsListener): PagingDataAdapter<MovieResult, PagerAdapter.MovieViewHolder>(MovieComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder = with(parent) {
       return MovieViewHolder(LayoutInflater.from(context).inflate(R.layout.item_discover, this, false))
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        val imageView: ImageView = itemView.findViewById(R.id.image)
        val name: TextView = itemView.findViewById(R.id.name)
        val detail: TextView = itemView.findViewById(R.id.details)
        val date: TextView = itemView.findViewById(R.id.date_picker_actions)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int)  = with(holder){
        val movieResult = getItem(position)

        Glide.with(itemView.context)
            .load(ApiHelper.BASE_POSTER_PATH + movieResult?.posterPath)
            .placeholder(R.drawable.ic_movie)
            .into(imageView)

        name.text = movieResult?.title
        date.text = movieResult?.releaseDate
        detail.text = movieResult?.overview

        itemView.setOnClickListener { listener.setClick(movieResult?.id) }
    }

    object MovieComparator : DiffUtil.ItemCallback<MovieResult>() {
        override fun areItemsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean = oldItem == newItem
    }

    suspend fun addList(newList: List<MovieResult>) {
        val currentData = snapshot().items
        val updatedData = currentData + newList
        submitData(PagingData.from(updatedData))
    }

    suspend fun setList(newList: List<MovieResult>) {
        submitData(PagingData.from(newList))
    }

}

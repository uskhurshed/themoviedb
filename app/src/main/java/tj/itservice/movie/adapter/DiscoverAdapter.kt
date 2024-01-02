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
import tj.itservice.movie.R
import tj.itservice.movie.activities.details.DetailsActivity
import tj.itservice.movie.data.MovieResult
import tj.itservice.movie.utils.ApiHelper

class DiscoverAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var movieList: ArrayList<MovieResult> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_discover, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieViewHolder -> {
                val movieResult = movieList[position]
                Glide.with(holder.itemView.context)
                    .load(ApiHelper.BASE_POSTER_PATH + movieResult.posterPath)
                    .placeholder(R.drawable.ic_movie)
                    .into(holder.imageView)

                holder.name.text = movieResult.title.toString()
                holder.date.text = movieResult.releaseDate.toString()
                holder.detail.text = movieResult.overview

                holder.itemView.setOnClickListener {
                    openActivity(holder.itemView.context,movieResult.id)
                }
            }
        }
    }
    override fun getItemCount(): Int {
        return movieList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addList(mv: ArrayList<MovieResult>) {
        movieList.addAll(mv)
        notifyDataSetChanged()
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image)
        val name: TextView = itemView.findViewById(R.id.name)
        val detail: TextView = itemView.findViewById(R.id.details)
        val date: TextView = itemView.findViewById(R.id.date_picker_actions)
    }

    private fun openActivity(context: Context, id: Long?){
        val intent = Intent(context, DetailsActivity::class.java)
        intent.putExtra("id",id)
        context.startActivity(intent)
    }

}


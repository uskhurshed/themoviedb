package tj.itservice.movie.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter
import tj.itservice.movie.R
import tj.itservice.movie.data.MovieResult
import tj.itservice.movie.utils.ApiHelper

@SuppressLint("SetTextI18n")
class MovieSliderAdapter : SliderViewAdapter<MovieSliderAdapter.MyViewHolder>() {

    private var movies: ArrayList<MovieResult> = ArrayList()

    class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        val poster: ImageView = itemView.findViewById(R.id.iv_backdrow)
        val movieTitle: TextView = itemView.findViewById(R.id.title_single_movie_slider)
        val releaseDate: TextView = itemView.findViewById(R.id.date_single_movie_slider)
        val adultCheck: TextView = itemView.findViewById(R.id.tv_adult)
    }

    override fun onCreateViewHolder(parent: ViewGroup): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_slider, parent, false) )
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder?, position: Int) {
        val movieResult = movies[position]

        Glide.with(viewHolder!!.itemView.context)
            .load(ApiHelper.BASE_BACKDROP_PATH+(movieResult.backdropPath))
            .into(viewHolder.poster)

        if(movieResult.adult) viewHolder.adultCheck.text = "18+"
        else viewHolder.adultCheck.text = "13+"

        viewHolder.movieTitle.text = movieResult.title
        viewHolder.releaseDate.text = "Дата релиза: " + movieResult.releaseDate
    }

    override fun getCount(): Int {
        return if (movies.isEmpty()) 0 else 5
    }

    fun setList(newList:  List<MovieResult>){
        movies.clear()
        movies.addAll(newList)
        notifyDataSetChanged()
    }
}
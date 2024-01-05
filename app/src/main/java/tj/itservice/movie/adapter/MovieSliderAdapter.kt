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

    private var movieList: ArrayList<MovieResult> = ArrayList()

    class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        val poster: ImageView = itemView.findViewById(R.id.iv_backdrow)
        val movieTitle: TextView = itemView.findViewById(R.id.title_single_movie_slider)
        val releaseDate: TextView = itemView.findViewById(R.id.date_single_movie_slider)
        val adultCheck: TextView = itemView.findViewById(R.id.tv_adult)
    }

    override fun onCreateViewHolder(parent: ViewGroup): MyViewHolder = with(parent) {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_slider, this, false) )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = with(holder){
        val movieResult = movieList[position]

        Glide.with(itemView.context)
            .load(ApiHelper.BASE_BACKDROP_PATH+(movieResult.backdropPath))
            .into(poster)

        if (movieResult.adult) adultCheck.text = "18+"
        else adultCheck.text = "13+"

        movieTitle.text = movieResult.title
        releaseDate.text = "Дата релиза: " + movieResult.releaseDate
    }

    override fun getCount(): Int {
        return if (movieList.isEmpty()) 0 else 5
    }

    fun setList(newList:  List<MovieResult>) = with(movieList){
        clear()
        addAll(newList)
        notifyDataSetChanged()
    }
}
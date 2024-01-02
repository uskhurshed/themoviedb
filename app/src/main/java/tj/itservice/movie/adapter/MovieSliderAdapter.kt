package tj.itservice.movie.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter
import tj.itservice.movie.R
import tj.itservice.movie.activities.details.DetailsActivity
import tj.itservice.movie.data.MovieResult
import tj.itservice.movie.utils.ApiHelper


class MovieSliderAdapter(private var movies: ArrayList<MovieResult>): SliderViewAdapter<MovieSliderAdapter.MyViewHolder>() {

    override fun getCount(): Int {
        return 5
    }

    override fun onCreateViewHolder(parent: ViewGroup): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_slider, parent, false) )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: MyViewHolder?, position: Int) {
        val movie = movies[position]
        Glide
            .with(viewHolder!!.itemView.context)
            .load(ApiHelper.BASE_BACKDROP_PATH+(movie.backdropPath))
            .into(viewHolder.poster)
        if(movie.adult) viewHolder.adultCheck.text = "18+"
        else viewHolder.adultCheck.text = "13+"
        viewHolder.movieTitle.text = movie.title
        viewHolder.releaseDate.text = "Дата релиза: " + movie.releaseDate

        viewHolder.itemView.setOnClickListener {
            val intent = Intent(viewHolder.itemView.context, DetailsActivity::class.java)
            intent.putExtra("id",movie.id)
            viewHolder.itemView.context.startActivity(intent)
        }
    }

    class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        val poster: ImageView = itemView.findViewById(R.id.iv_backdrow)
        val movieTitle: TextView = itemView.findViewById(R.id.title_single_movie_slider)
        val releaseDate: TextView = itemView.findViewById(R.id.date_single_movie_slider)
        val adultCheck: TextView = itemView.findViewById(R.id.tv_adult)
    }
}
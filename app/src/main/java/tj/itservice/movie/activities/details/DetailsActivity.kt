package tj.itservice.movie.activities.details

import android.R.attr.value
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setMargins
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import tj.itservice.movie.R
import tj.itservice.movie.data.MovieResult
import tj.itservice.movie.databinding.ActivityDetailsBinding
import tj.itservice.movie.utils.ApiHelper
import tj.itservice.movie.utils.LoadingDialog
import tj.itservice.movie.utils.RateDialog
import java.text.NumberFormat
import java.util.Locale


@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private lateinit var bind: ActivityDetailsBinding
    private lateinit var detailVM: DetailViewModel
    private val rateDialog = RateDialog(this)
    var id: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(bind.root)


        id = intent.getLongExtra("id", 25)
        val loadingDialog = LoadingDialog(this)
        loadingDialog.show()


        detailVM = ViewModelProvider(this)[DetailViewModel::class.java]
        detailVM.error.observe(this){
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
            finish()
        }

        detailVM.load(id)
        detailVM.detailLD.observe(this) {
            setUI(it)
            loadingDialog.dismiss()
        }
        bind.btnRate.setOnClickListener {
            showProgressDialog()
        }
        bind.btnBack.setOnClickListener {
            finish()
        }

        detailVM.isFavorite.observe(this) { isFavorite ->
            if (isFavorite) bind.btnFav.setImageResource(R.drawable.ic_favorite_del)
             else bind.btnFav.setImageResource(R.drawable.ic_favorite)
        }

        bind.btnFav.setOnClickListener {
            lifecycleScope.launch {
                detailVM.toggleFavorite()
            }
        }

    }


    private fun formatNumber(number: Long?): String {
        // Барои разделить кадан разрядба
        val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
        return numberFormat.format(number)
    }


    @SuppressLint("SetTextI18n")
    private fun showProgressDialog() {
        rateDialog.show()

        rateDialog.setOnRateListener { rating ->
            detailVM.rate(id, (rating * 2).toInt()) { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        rateDialog.setOnDeleteListener {
            detailVM.deleteRate(id) { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setUI(movieResult: MovieResult?) {
        bind.apply {
            tvTitle.text = movieResult?.title
            tvOriginalTitle.text = movieResult?.original_title
            tvRealise.text = "Дата релиза: " + movieResult?.release_date
            tvInfo.text = movieResult?.overview
            if (movieResult?.adult == true) tvAdult.text = "+18"
            else tvAdult.text = "+13"
            Glide.with(this@DetailsActivity)
                .load(ApiHelper.BASE_BACKDROP_PATH + (movieResult?.backdrop_path))
                .error(R.drawable.ic_movie)
                .into(ivBackdrow)
            Glide.with(this@DetailsActivity)
                .load(ApiHelper.BASE_POSTER_PATH + (movieResult?.poster_path))
                .error(R.drawable.ic_movie)
                .into(ivPoster)
            bind.genre.removeAllViews()
            lifecycleScope.launch {
                detailVM.checkHaving()
            }
            for (i in 0 until (movieResult?.genres?.size ?: 0)) {
                val tv = TextView(this@DetailsActivity, null, 0, R.style.tag)
                tv.text = movieResult?.genres?.get(i)?.name
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(12)
                tv.layoutParams = layoutParams
                bind.genre.addView(tv)
            }
            tvRating.text = movieResult?.vote_average.toString()
            tvMoney.text = "Доход: $" + formatNumber(movieResult?.revenue)

        }
    }
}
package tj.itservice.movie.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import tj.itservice.movie.R
import tj.itservice.movie.data.MovieResult
import tj.itservice.movie.databinding.FragmentDetailsBinding
import tj.itservice.movie.ui.viewmodels.DetailViewModel
import tj.itservice.movie.utils.ApiHelper
import tj.itservice.movie.utils.LoadingDialog
import tj.itservice.movie.utils.RateDialog
import java.text.NumberFormat
import java.util.*

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private lateinit var bind: FragmentDetailsBinding
    private val detailVM: DetailViewModel by viewModels()

    private val rateDialog by lazy { RateDialog(requireActivity()) }
    private var id: Long = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        bind = FragmentDetailsBinding.inflate(inflater, container, false)
        id = arguments?.getLong("id", 25) ?: 25
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loadingDialog = LoadingDialog(requireContext())
        loadingDialog.show()

        detailVM.load(id)

        detailVM.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            loadingDialog.dismiss()
            bind.btnBack.performClick()
        }

        detailVM.movieList.observe(viewLifecycleOwner) {
            setUI(it)
            loadingDialog.dismiss()
        }

        detailVM.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
            if (isFavorite) bind.btnFav.setImageResource(R.drawable.ic_favorite_del)
            else bind.btnFav.setImageResource(R.drawable.ic_favorite)
        }

        bind.btnRate.setOnClickListener { showProgressDialog() }
        bind.btnBack.setOnClickListener { findNavController().navigateUp() }
        bind.btnFav.setOnClickListener { lifecycleScope.launch { detailVM.toggleFavorite() } }

    }

    private fun formatNumber(number: Long?): String {
        val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
        return numberFormat.format(number)
    }

    private fun showProgressDialog() = with (rateDialog){
        show()
        setOnRateListener { rating ->
            detailVM.rate(id, (rating * 2).toInt()) { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show() }
        }
        setOnDeleteListener {
            detailVM.deleteRate(id) { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show() }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUI(movieResult: MovieResult?) = with(bind) {
            tvTitle.text = movieResult?.title
            tvOriginalTitle.text = movieResult?.originalTitle
            tvRealise.text = "Дата релиза: " + movieResult?.releaseDate
            tvInfo.text = movieResult?.overview
            tvRating.text = movieResult?.voteAverage.toString()
            tvMoney.text = "Доход: $" + formatNumber(movieResult?.revenue)

            if (movieResult?.adult == true) tvAdult.text = "+18"
            else tvAdult.text = "+13"

            Glide.with(requireContext())
                .load(ApiHelper.BASE_BACKDROP_PATH + (movieResult?.backdropPath))
                .error(R.drawable.ic_movie)
                .into(ivBackdrow)

            Glide.with(requireContext())
                .load(ApiHelper.BASE_POSTER_PATH + (movieResult?.posterPath))
                .error(R.drawable.ic_movie)
                .into(ivPoster)

            genre.removeAllViews()
            lifecycleScope.launch { detailVM.checkHaving() }
            for (i in 0 until (movieResult?.genres?.size ?: 0)) {
                val tv = TextView(requireContext(), null, 0, R.style.tag)
                tv.text = movieResult?.genres?.get(i)?.name
                val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.setMargins(12)
                tv.layoutParams = layoutParams
                genre.addView(tv)
            }
    }
}

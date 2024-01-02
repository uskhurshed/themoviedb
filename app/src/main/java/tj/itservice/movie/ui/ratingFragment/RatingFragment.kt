package tj.itservice.movie.ui.ratingFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import tj.itservice.movie.R
import tj.itservice.movie.adapter.MovieAdapter
import tj.itservice.movie.databinding.FragmentRatingBinding

@AndroidEntryPoint
class RatingFragment : Fragment() {

    private lateinit var bindRate: FragmentRatingBinding
    private val viewModel: RateViewModel by viewModels()
    private lateinit var adapter:MovieAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        bindRate = FragmentRatingBinding.inflate(inflater, container, false)
        return bindRate.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter  = MovieAdapter()
        bindRate.rvTop.adapter = adapter
        viewModel.rateLD.observe(viewLifecycleOwner){
            adapter.addList(it)
        }
        initRecycleListeners()

        viewModel.error.observe(this.viewLifecycleOwner){
            if (it != "") showErrorMessage()
            else hideErrorMessage()
        }

    }

    private fun initRecycleListeners() = with(bindRate) {
        rvTop.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && adapter.movieList.isNotEmpty()) {
                    viewModel.getRate()
                }
            }
        })
    }
    private fun showErrorMessage() {
        bindRate.rvTop.visibility = View.GONE

        val v = LayoutInflater.from(requireContext()).inflate(R.layout.error_state, bindRate.root, false)
        val btnRetry = v.findViewById<Button>(R.id.button)

        btnRetry.setOnClickListener {
            viewModel.start()
            adapter.movieList.clear()
            hideErrorMessage()
            viewModel.error.postValue("")
            bindRate.main.removeView(v)
        }

        bindRate.main.addView(v)
    }

    private fun hideErrorMessage() {
        bindRate.rvTop.visibility = View.VISIBLE
    }

}
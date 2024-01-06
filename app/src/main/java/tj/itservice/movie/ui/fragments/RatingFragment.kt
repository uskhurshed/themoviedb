package tj.itservice.movie.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import tj.itservice.movie.R
import tj.itservice.movie.adapter.MovieAdapter
import tj.itservice.movie.data.MovieResult
import tj.itservice.movie.databinding.FragmentRatingBinding
import tj.itservice.movie.interfaces.DetailsListener
import tj.itservice.movie.ui.viewmodels.RateViewModel
import tj.itservice.movie.utils.ErrorManager

@AndroidEntryPoint
class RatingFragment : Fragment(), DetailsListener{

    private lateinit var bindRate: FragmentRatingBinding
    private val viewModel: RateViewModel by viewModels()
    private var adapter = MovieAdapter(this)
    private lateinit var errorManager: ErrorManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        bindRate = FragmentRatingBinding.inflate(inflater, container, false).apply {
            this.viewModel = this@RatingFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        errorManager = ErrorManager(requireContext(), bindRate.main)
        return bindRate.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.movieList.observe(viewLifecycleOwner){ adapter.movieList  = it as ArrayList<MovieResult> }
        bindRate.rvTop.adapter = adapter
        initRecycleListeners()
        observeErrors()
    }

    private fun initRecycleListeners() = with(viewModel) {
        bindRate.rvTop.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(0) && adapter.movieList.isNotEmpty() ) getRate()
            }
        })
    }

    private fun observeErrors() = with(viewModel) {
        isError.observe(viewLifecycleOwner) { isVisible ->
            if (isVisible) errorManager.showErrorMessage {
                adapter.movieList.clear()
                start()
            }
        }
    }

    override fun setClick(id: Long?)  = with(findNavController()){
        val bundle = Bundle().apply { id?.let { putLong("id", it) } }
        if (currentDestination?.id == R.id.ratingFragment) navigate(R.id.action_ratingFragment_to_detailsFragment, bundle)
    }


}
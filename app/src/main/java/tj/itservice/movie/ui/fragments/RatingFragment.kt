package tj.itservice.movie.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import tj.itservice.movie.R
import tj.itservice.movie.adapter.MovieAdapter
import tj.itservice.movie.databinding.FragmentHomeBinding
import tj.itservice.movie.databinding.FragmentRatingBinding
import tj.itservice.movie.ui.interfaces.DetailsListener
import tj.itservice.movie.ui.viewmodels.RateViewModel
import tj.itservice.movie.utils.ErrorManager

@AndroidEntryPoint
class RatingFragment : Fragment() {

    private lateinit var bindRate: FragmentRatingBinding
    private val viewModel: RateViewModel by viewModels()
    private lateinit var adapter:MovieAdapter
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
        showBottomNav()
        adapter  = MovieAdapter()
        bindRate.rvTop.adapter = adapter
        viewModel.rateLD.observe(viewLifecycleOwner){
            adapter.addList(it)
        }
        initRecycleListeners()
        observeErrors()

        adapter.mListener = object : DetailsListener {
            override fun setClick(id: Long?) {
                id?.let {
                    val bundle = Bundle().apply { putLong("id", it) }
                    findNavController().navigate(R.id.action_ratingFragment_to_detailsFragment, bundle)
                    val bottomNavigationView = requireActivity().findViewById<View>(R.id.bottomNavigationView)
                    bottomNavigationView.visibility = View.GONE
                }
            }
        }

    }

    private fun initRecycleListeners() = with(bindRate) {
        rvTop.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && adapter.movieList.isNotEmpty()) viewModel?.getRate()
            }
        })
    }

    private fun observeErrors() {
        viewModel.isErrorVisible.observe(viewLifecycleOwner) { isVisible ->
            if (isVisible) errorManager.showErrorMessage { viewModel.start() }
        }
    }

    private fun showBottomNav(){
        val bottom = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if (bottom != null) if (bottom.visibility == View.GONE) bottom.visibility = View.VISIBLE
    }

}
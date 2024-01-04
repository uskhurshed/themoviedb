package tj.itservice.movie.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import tj.itservice.movie.R
import tj.itservice.movie.adapter.MovieAdapter
import tj.itservice.movie.adapter.MovieSliderAdapter
import tj.itservice.movie.databinding.FragmentHomeBinding
import tj.itservice.movie.ui.interfaces.DetailsListener
import tj.itservice.movie.ui.viewmodels.HomeViewModel
import tj.itservice.movie.utils.ErrorManager

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var errorManager: ErrorManager

    private val movieSliderAdapter by lazy { MovieSliderAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            this.viewModel = this@HomeFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        errorManager = ErrorManager(requireContext(), binding.main)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavIsVisible(View.VISIBLE)

        viewModel.upcomingLD.observe(viewLifecycleOwner) { mList ->
            binding.slider.setSliderAdapter(movieSliderAdapter)
            movieSliderAdapter.setList(mList)
            binding.slider.startAutoCycle()
        }

        val popularAdapter = MovieAdapter()
        binding.rvPopular.adapter = popularAdapter
        viewModel.popularLD.observe(viewLifecycleOwner) {
            popularAdapter.addList(it)
        }
        popularAdapter.mListener = object : DetailsListener {
            override fun setClick(id: Long?) { id?.let { navigateToDetails(it) } }
        }

        observeErrors()
        setupNavigationListeners()
    }

    private fun observeErrors() {
        viewModel.isErrorVisible.observe(viewLifecycleOwner) { isVisible ->
            if (isVisible) errorManager.showErrorMessage { viewModel.start() }
        }
    }

    private fun setupNavigationListeners() = with(binding) {
        search.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_discoverFragment) }
        seeAll.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_discoverFragment) }
    }

    private fun navigateToDetails(movieId: Long) {
        val bundle = Bundle().apply { putLong("id", movieId) }
        findNavController().navigate(R.id.action_homeFragment_to_detailsFragment, bundle)
        bottomNavIsVisible(View.GONE)
    }

    private fun bottomNavIsVisible(visibility: Int){
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility = visibility
    }
}

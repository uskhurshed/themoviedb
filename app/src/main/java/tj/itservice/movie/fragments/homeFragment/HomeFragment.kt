package tj.itservice.movie.fragments.homeFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import tj.itservice.movie.R
import tj.itservice.movie.adapter.MovieAdapter
import tj.itservice.movie.adapter.MovieSliderAdapter
import tj.itservice.movie.databinding.FragmentHomeBinding

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var bindHome: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var movieSliderAdapter: MovieSliderAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        bindHome = FragmentHomeBinding.inflate(inflater, container, false)
        return bindHome.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.upcomingLD.observe(this.viewLifecycleOwner) {
            movieSliderAdapter = MovieSliderAdapter(it)
            bindHome.apply {
                slider.setSliderAdapter(movieSliderAdapter)
                slider.startAutoCycle()
            }
        }



        val popularAdapter = MovieAdapter()
        bindHome.rvPopular.adapter = popularAdapter
        viewModel.popularLD.observe(this.viewLifecycleOwner) {
            popularAdapter.addList(it)
        }

        bindHome.search.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_discoverFragment)
        }
        bindHome.seeAll.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_discoverFragment)
        }

        viewModel.error.observe(this.viewLifecycleOwner){
            if (it != "") showErrorMessage()
            else hideErrorMessage()
        }

    }

    private fun showErrorMessage() {
        bindHome.titles.visibility = View.GONE
        bindHome.rvPopular.visibility = View.GONE
        bindHome.slider.visibility = View.GONE

        val v = LayoutInflater.from(requireContext()).inflate(R.layout.error_state, bindHome.root, false)
        val btnRetry = v.findViewById<Button>(R.id.button)

        btnRetry.setOnClickListener {
            viewModel.start()
            hideErrorMessage()
            viewModel.error.postValue("")
            bindHome.main.removeView(v)
        }

        bindHome.main.addView(v)
    }

    private fun hideErrorMessage() {
        bindHome.titles.visibility = View.VISIBLE
        bindHome.rvPopular.visibility = View.VISIBLE
        bindHome.slider.visibility = View.VISIBLE
    }


}
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
import tj.itservice.movie.databinding.FragmentRatingBinding
import tj.itservice.movie.ui.interfaces.DetailsListener
import tj.itservice.movie.ui.viewmodels.RateViewModel

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

         showBottomNav()

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
    private fun showBottomNav(){
        val bottom = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if (bottom != null) if (bottom.visibility == View.GONE) bottom.visibility = View.VISIBLE
    }

}
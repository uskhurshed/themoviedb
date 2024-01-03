package tj.itservice.movie.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import tj.itservice.movie.R
import tj.itservice.movie.adapter.DiscoverAdapter
import tj.itservice.movie.data.MovieResult
import tj.itservice.movie.databinding.FragmentDiscoverBinding
import tj.itservice.movie.ui.interfaces.DetailsListener
import tj.itservice.movie.ui.viewmodels.DiscoverViewModel
import tj.itservice.movie.ui.viewmodels.HomeViewModel
import tj.itservice.movie.utils.LoadingDialog


@AndroidEntryPoint
class DiscoverFragment : Fragment() {

    private lateinit var bindDis:FragmentDiscoverBinding
    private val discVM: DiscoverViewModel by viewModels()
    private val mainVM: HomeViewModel by viewModels()
    private lateinit var adapter: DiscoverAdapter
    private var searchFrag = false
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        bindDis = FragmentDiscoverBinding.inflate(inflater, container, false)
        return bindDis.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = LoadingDialog(requireContext())
        loadingDialog.show()
        bindDis.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        adapter = DiscoverAdapter()
        bindDis.rvDiscover.adapter = adapter
        mainVM.getPopulars()
        mainVM.popularLD.observe(viewLifecycleOwner){
            adapter.addList(it)
            loadingDialog.dismiss()
        }

        initRecycleListeners()
        bindDis.etSearch.setOnKeyListener { _, _, _ ->
            if(bindDis.etSearch.text.toString() != ""){
                discVM.getSearch(bindDis.etSearch.text.toString())
                loadingDialog.show()
            }
            searchFrag = true
            false
        }

        discVM.movieList.observe(viewLifecycleOwner) {
            loadingDialog.dismiss()
            adapter.movieList = it as ArrayList<MovieResult>
            adapter.notifyDataSetChanged()
            if (it.isEmpty()) {
                bindDis.empty.visibility = View.VISIBLE
                bindDis.rvDiscover.visibility = View.GONE
            } else {
                bindDis.rvDiscover.visibility = View.VISIBLE
                bindDis.empty.visibility = View.GONE
            }
        }
        discVM.error.observe(this.viewLifecycleOwner){
            if (it != "") showErrorMessage()
            else hideErrorMessage()
        }
//        mainVM.error.observe(this.viewLifecycleOwner) {
//            if (it != "") showErrorMessage()
//            else hideErrorMessage()
//        }


        adapter.mListener = object : DetailsListener {
            override fun setClick(id: Long?) {
                id?.let {
                    val bundle = Bundle().apply {
                        putLong("id", it)
                    }
                    findNavController().navigate(R.id.action_discoverFragment_to_detailsFragment, bundle)
                    val bottomNavigationView = requireActivity().findViewById<View>(R.id.bottomNavigationView)
                    bottomNavigationView.visibility = View.GONE
                }
            }
        }

    }


    private fun initRecycleListeners() = with(bindDis) {
        rvDiscover.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && !searchFrag) {
                    mainVM.getPopulars()
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showErrorMessage() {
        bindDis.etSearch.visibility = View.GONE
        bindDis.rvDiscover.visibility = View.GONE
        bindDis.main.visibility = View.VISIBLE
        loadingDialog.dismiss()
        val v = LayoutInflater.from(requireContext()).inflate(R.layout.error_state, bindDis.root, false)
        val btnRetry = v.findViewById<Button>(R.id.button)

        btnRetry.setOnClickListener {
            mainVM.start()
            adapter.movieList.clear()
            loadingDialog.show()
            hideErrorMessage()
//            mainVM.error.postValue("")
            bindDis.main.removeView(v)
            adapter.notifyDataSetChanged()
        }

        bindDis.main.addView(v)
    }

    private fun hideErrorMessage() {
        bindDis.etSearch.visibility = View.VISIBLE
        bindDis.rvDiscover.visibility = View.VISIBLE
        bindDis.main.visibility = View.GONE
    }

}
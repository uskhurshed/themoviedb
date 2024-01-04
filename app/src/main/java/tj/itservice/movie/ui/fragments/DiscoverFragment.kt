package tj.itservice.movie.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import tj.itservice.movie.R
import tj.itservice.movie.adapter.DiscoverAdapter
import tj.itservice.movie.databinding.FragmentDiscoverBinding
import tj.itservice.movie.ui.interfaces.DetailsListener
import tj.itservice.movie.ui.viewmodels.DiscoverViewModel
import tj.itservice.movie.ui.viewmodels.HomeViewModel
import tj.itservice.movie.utils.ErrorManager
import tj.itservice.movie.utils.LoadingDialog


@AndroidEntryPoint
class DiscoverFragment : Fragment() {

    private lateinit var bindDis:FragmentDiscoverBinding

    private val viewModel: DiscoverViewModel by viewModels()
    private val mainVM: HomeViewModel by viewModels()
    private lateinit var errorManager: ErrorManager

    private lateinit var adapter: DiscoverAdapter
    private lateinit var loadingDialog: LoadingDialog
    private var searchFlag = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        bindDis = FragmentDiscoverBinding.inflate(inflater, container, false).apply {
            this.viewModel = this@DiscoverFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        errorManager = ErrorManager(requireContext(), bindDis.main)
        return bindDis.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = LoadingDialog(requireContext())
        loadingDialog.show()

        bindDis.btnBack.setOnClickListener { findNavController().navigateUp() }

        adapter = DiscoverAdapter()
        bindDis.rvDiscover.adapter = adapter

        mainVM.getPopulars()
        mainVM.popularLD.observe(viewLifecycleOwner){
            adapter.addList(it)
            loadingDialog.dismiss()
        }

        initRecycleListeners()
        initSearch()

        viewModel.movieList.observe(viewLifecycleOwner) {
            loadingDialog.dismiss()
            adapter.setList(it)
        }
        observeErrors()

        adapter.mListener = object : DetailsListener {
            override fun setClick(id: Long?) {
                id?.let { val bundle = Bundle().apply { putLong("id", it) }
                    findNavController().navigate(R.id.action_discoverFragment_to_detailsFragment, bundle)
                    val bottomNavigationView = requireActivity().findViewById<View>(R.id.bottomNavigationView)
                    bottomNavigationView.visibility = View.GONE
                }
            }
        }

    }

    private fun initSearch() = with (bindDis.etSearch){
        setOnKeyListener { _, _, _ ->
            if ("$text" != "") {
                viewModel.getSearch("$text")
                loadingDialog.show()
                searchFlag = true
            }
            false
        }
    }

    private fun initRecycleListeners() = with(bindDis) {
        rvDiscover.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && !searchFlag) mainVM.getPopulars()
            }
        })
    }

    private fun observeErrors() = with(viewModel) {
        isErrorVisible.observe(viewLifecycleOwner) { isVisible ->
            if (isVisible) {
                loadingDialog.dismiss()
                errorManager.showErrorMessage { getSearch(bindDis.etSearch.text.toString()) }
            }
        }
    }


}
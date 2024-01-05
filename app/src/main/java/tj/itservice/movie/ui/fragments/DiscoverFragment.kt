package tj.itservice.movie.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import tj.itservice.movie.R
import tj.itservice.movie.adapter.PagerAdapter
import tj.itservice.movie.databinding.FragmentDiscoverBinding
import tj.itservice.movie.interfaces.DetailsListener
import tj.itservice.movie.ui.viewmodels.DiscoverViewModel
import tj.itservice.movie.utils.ErrorManager
import tj.itservice.movie.utils.LoadingDialog

@AndroidEntryPoint
class DiscoverFragment : Fragment(),DetailsListener {

    private lateinit var bindDis: FragmentDiscoverBinding

    private val viewModel: DiscoverViewModel by viewModels()
    private lateinit var errorManager: ErrorManager
    private lateinit var loadingDialog: LoadingDialog

    private var adapter = PagerAdapter(this)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        bindDis = FragmentDiscoverBinding.inflate(inflater, container, false).apply {
            this.viewModel = this@DiscoverFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        loadingDialog = LoadingDialog(requireContext())
        errorManager = ErrorManager(requireContext(), bindDis.main)
        return bindDis.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(bindDis) {
        super.onViewCreated(view, savedInstanceState)

        btnBack.setOnClickListener { findNavController().navigateUp() }
        rvDiscover.adapter = adapter

        setupRecyclerView()
        observeErrors()

        viewModel.popularList.observe(viewLifecycleOwner) { pagingData ->
            adapter.submitData(lifecycle, pagingData)
        }

        adapter.addLoadStateListener { loadState ->
            when (loadState.refresh) {
                is LoadState.Error ->  {
                    viewModel.isError.value = true
                    loadingDialog.dismiss()
                }
                is LoadState.Loading -> loadingDialog.show()
                is LoadState.NotLoading -> loadingDialog.dismiss()
            }
        }

        viewModel.movieList.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launch {
                loadingDialog.dismiss()
                adapter.setList(it)
            }
        }

        initSearch()

    }

    private fun initSearch() = with(bindDis.etSearch) {
        setOnKeyListener { _, _, _ ->
            if ("$text" != "") {
                viewModel.getSearch("$text")
                loadingDialog.show()
            }
            false
        }
    }


    private fun setupRecyclerView() = with(bindDis.rvDiscover) {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = this@DiscoverFragment.adapter
    }

    private fun observeErrors() = with(viewModel.isError) {
        observe(viewLifecycleOwner) { isVisible ->
            if (isVisible) errorManager.showErrorMessage {
                adapter.retry()
                value = false
            }
        }
    }

    override fun setClick(id: Long?): Unit = with(findNavController()) {
        id?.let {
            val bundle = Bundle().apply { putLong("id", it) }
            navigate(R.id.action_discoverFragment_to_detailsFragment, bundle)
        }
    }
}
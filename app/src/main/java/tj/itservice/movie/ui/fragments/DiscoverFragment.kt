package tj.itservice.movie.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
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

    private var adapter = PagerAdapter(this)
    private var searchFlag = false
    private lateinit var loadingDialog: LoadingDialog

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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingDialog.show()

        bindDis.btnBack.setOnClickListener { findNavController().navigateUp() }
        bindDis.rvDiscover.adapter = adapter

        viewModel.getPopulars()
        viewModel.popularList.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launch {
                adapter.addList(it)
                loadingDialog.dismiss()
            }
        }

        viewModel.movieList.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launch {
                loadingDialog.dismiss()
                adapter.setList(it)
            }
        }

        initRecycleListeners()
        initSearch()
        observeErrors()
    }

    private fun initSearch() = with(bindDis.etSearch) {
        setOnKeyListener { _, _, _ ->
            if ("$text" != "") {
                viewModel.getSearch("$text")
                loadingDialog.show()
                searchFlag = true
            }
            false
        }
    }

    private fun initRecycleListeners() = with(viewModel) {
        bindDis.rvDiscover.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(0) && !searchFlag) getPopulars()
            }
        })
    }

    private fun observeErrors() = with(viewModel) {
        isError.observe(viewLifecycleOwner) { isVisible ->
            if (isVisible) {
                loadingDialog.dismiss()
                errorManager.showErrorMessage { getPopulars() }
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
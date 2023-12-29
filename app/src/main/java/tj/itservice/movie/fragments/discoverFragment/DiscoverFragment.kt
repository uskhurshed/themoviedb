package tj.itservice.movie.fragments.discoverFragment

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
import tj.itservice.movie.databinding.FragmentDiscoverBinding
import tj.itservice.movie.fragments.homeFragment.HomeViewModel
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

        discVM.searchLD.observe(viewLifecycleOwner) {
            loadingDialog.dismiss()
            adapter.movieList = it
            adapter.notifyDataSetChanged()
            if (it.size == 0) {
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
        mainVM.error.observe(this.viewLifecycleOwner) {
            if (it != "") showErrorMessage()
            else hideErrorMessage()
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

    private fun showErrorMessage() {
        bindDis.etSearch.visibility = View.GONE
        bindDis.rvDiscover.visibility = View.GONE
        bindDis.main.visibility = View.GONE
        loadingDialog.dismiss()
        val v = LayoutInflater.from(requireContext()).inflate(R.layout.error_state, bindDis.root, false)
        val btnRetry = v.findViewById<Button>(R.id.button)

        btnRetry.setOnClickListener {
            mainVM.start()
            hideErrorMessage()
            mainVM.error.postValue("")
            bindDis.main.removeView(v)
        }

        bindDis.main.addView(v)
    }

    private fun hideErrorMessage() {
        bindDis.etSearch.visibility = View.VISIBLE
        bindDis.rvDiscover.visibility = View.VISIBLE
        bindDis.main.visibility = View.VISIBLE
    }


}
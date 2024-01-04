package tj.itservice.movie.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tj.itservice.movie.R
import tj.itservice.movie.adapter.DiscoverAdapter
import tj.itservice.movie.databinding.FragmentFavoriteBinding
import tj.itservice.movie.db.MovieDao
import tj.itservice.movie.ui.interfaces.DetailsListener
import javax.inject.Inject

@AndroidEntryPoint

class FavoriteFragment : Fragment() {

    private lateinit var bindFav:FragmentFavoriteBinding

    private var adapter = DiscoverAdapter()
    @Inject lateinit var movieDao: MovieDao

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        bindFav = FragmentFavoriteBinding.inflate(inflater, container, false)
        return bindFav.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindFav.rvFavorite.adapter = adapter

        getFavorites()

        adapter.mListener = object : DetailsListener {
            override fun setClick(id: Long?) {
                id?.let {
                    val bundle = Bundle().apply { putLong("id", it) }
                    findNavController().navigate(R.id.action_favoriteFragment_to_detailsFragment, bundle)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getFavorites() = with(adapter){
        viewLifecycleOwner.lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) { movieDao.getAllMovies() }
            setList(result)
            notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        getFavorites()
    }

}
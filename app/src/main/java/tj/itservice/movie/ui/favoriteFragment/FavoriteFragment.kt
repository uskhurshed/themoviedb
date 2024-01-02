package tj.itservice.movie.ui.favoriteFragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tj.itservice.movie.App
import tj.itservice.movie.adapter.DiscoverAdapter
import tj.itservice.movie.data.MovieResult
import tj.itservice.movie.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private lateinit var bindFav:FragmentFavoriteBinding
    private val movieDao = App.database.movieDao()
    private lateinit var adapter: DiscoverAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        bindFav = FragmentFavoriteBinding.inflate(inflater, container, false)
        return bindFav.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = DiscoverAdapter()
        bindFav.rvFavorite.adapter = adapter
        getFavorites()
    }

    @SuppressLint("NotifyDataSetChanged")
    @OptIn(DelicateCoroutinesApi::class)
    fun getFavorites() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
               val result = movieDao.getAllMovies()
                withContext(Dispatchers.Main) {
                    adapter.movieList = result as ArrayList<MovieResult>
                    adapter.notifyDataSetChanged()
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        getFavorites()
    }




}
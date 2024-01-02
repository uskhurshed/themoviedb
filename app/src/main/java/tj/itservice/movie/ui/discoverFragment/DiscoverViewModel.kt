package tj.itservice.movie.ui.discoverFragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import tj.itservice.movie.data.MovieResult
import tj.itservice.movie.di.Repository
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel
@Inject constructor(private val postRepository: Repository) : ViewModel() {

    val searchLD: MutableLiveData<ArrayList<MovieResult>> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()

    fun getSearch(searchQuery: String) {
        viewModelScope.launch {
            try {
                val results = postRepository.getMovieByQuery(searchQuery)
                searchLD.postValue(results.results)
            } catch (e: Exception) {
                Log.d("main", "getPost: ${e.message}")
                error.postValue(e.message)
            }
        }
    }



}

package tj.itservice.movie.ui.viewmodels

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

    val movieList: MutableLiveData<List<MovieResult>> = MutableLiveData()
    val isErrorVisible: MutableLiveData<Boolean> = MutableLiveData(false)

    fun getSearch(searchQuery: String) = with(viewModelScope) {
            launch {
                try {
                    val results = postRepository.getMovieByQuery(searchQuery)
                    movieList.postValue(results.results)
                    Log.d("response", "getPost: ${results.results}")
                    isErrorVisible.postValue(false)
                } catch (e: Exception) {
                    isErrorVisible.postValue(true)
                    Log.d("response", "getPost: ${e.message}")
                }
            }
    }

}

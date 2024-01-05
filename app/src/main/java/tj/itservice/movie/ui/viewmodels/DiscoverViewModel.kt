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
    val popularList: MutableLiveData<List<MovieResult>> = MutableLiveData()

    val isError: MutableLiveData<Boolean> = MutableLiveData(false)
    private val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    private var popularPage:Int = 1

    fun getSearch(searchQuery: String) = viewModelScope.launch {
        try {
            val results = postRepository.getMovieByQuery(searchQuery)
            movieList.postValue(results.results)
            isError.postValue(false)
            Log.d("response", "getPost: ${results.results}")
        } catch (e: Exception) {
            isError.postValue(true)
            Log.e("response", "Error: $e")
        }

    }

    fun getPopulars() = viewModelScope.launch {
        if (isLoading.value == true) return@launch
        isLoading.postValue(true)

        try {
            val response = postRepository.getPopularMovie(popularPage)
            popularList.postValue(response.results)
            popularPage++
            isError.postValue(false)
            Log.e("response", "${response.results}")
        } catch (e: Exception) {
            isError.postValue(true)
            Log.e("response", "Error: $e")
        } finally {
            isLoading.postValue(false)
        }
    }

}

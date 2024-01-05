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
class HomeViewModel
@Inject constructor(private val postRepository: Repository) : ViewModel() {

    val upcomingLD: MutableLiveData<List<MovieResult>> = MutableLiveData()
    val popularLD: MutableLiveData<List<MovieResult>> = MutableLiveData()

    val isError: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        start()
    }

    fun start(){
        getPopulars()
        getUpcoming()
    }

    private fun getUpcoming() = viewModelScope.launch {
        try {
            val response = postRepository.getUpcomingMovie()
            upcomingLD.postValue(response.results)
            isError.postValue(false)
            Log.e("response", "${response.results}")
        } catch (e: Exception) {
            Log.e("response", "Error getUpcoming: $e")
            isError.postValue(true)
        }
    }


    private fun getPopulars() = viewModelScope.launch {
        try {
            val response = postRepository.getPopularMovie(1)
            popularLD.postValue(response.results)
            isError.postValue(false)
            Log.e("response", "${response.results}")
        } catch (e: Exception) {
            isError.postValue(true)
            Log.e("response", "Error: $e")
        }

    }

}

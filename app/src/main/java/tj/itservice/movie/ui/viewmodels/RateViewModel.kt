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
class RateViewModel @Inject constructor(private val postRepository: Repository) : ViewModel() {

    val movieList: MutableLiveData<List<MovieResult>> = MutableLiveData()
    private var ratePage:Int = 1

    val isError: MutableLiveData<Boolean> = MutableLiveData(false)
    private val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    init { start() }

    fun start(){
        ratePage = 1
        getRate()
    }

    fun getRate() = viewModelScope.launch {
        if (isLoading.value == true) return@launch
        isLoading.postValue(true)

        try {
            val response = postRepository.getTopRatedMovie(ratePage)
            movieList.postValue(response.results)
            ratePage++
            Log.d("response", "${response.results}")
            isError.postValue(false)
        } catch (e: Exception) {
            Log.d("response", "Error: ${e.message}")
            isError.postValue(true)
        } finally {
            isLoading.postValue(false)
        }
    }
}

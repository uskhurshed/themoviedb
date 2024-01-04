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

    val rateLD: MutableLiveData<List<MovieResult>> = MutableLiveData()
    private var ratePage:Int = 1
    val isErrorVisible: MutableLiveData<Boolean> = MutableLiveData(false)


    init {
        start()
    }

    fun start(){
        ratePage = 1
        getRate()
    }


    fun getRate() {
        viewModelScope.launch {
            try {
                val response = postRepository.getTopRatedMovie(ratePage)
                rateLD.postValue(response.results)
                ratePage ++
                Log.d("rateVM", "${response.results}")
                isErrorVisible.postValue(false)
            } catch (e: Exception) {
                Log.d("rateVM", "getPost: ${e.message}")
                isErrorVisible.postValue(true)
            }
        }
    }

}

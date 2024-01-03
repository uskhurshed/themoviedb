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

    val upcomingLD: MutableLiveData<ArrayList<MovieResult>> = MutableLiveData()
    val popularLD: MutableLiveData<ArrayList<MovieResult>> = MutableLiveData()
    val isErrorVisible: MutableLiveData<Boolean> = MutableLiveData(false)
    private var popularPage:Int = 1


    init {
        start()
    }

    fun start(){
        popularPage = 1
        getPopulars()
        getUpcoming()
    }

    private fun getUpcoming() {
        viewModelScope.launch {
            try {
                val response = postRepository.getUpcomingMovie()
                upcomingLD.postValue(response.results)
                isErrorVisible.postValue(false)
            } catch (e: Exception) {
                Log.e("main", "getUpcoming: ${e.message}")
                isErrorVisible.postValue(true)
            }
        }
    }


    fun getPopulars() {
        viewModelScope.launch {
            try {
                val response = postRepository.getPopularMovie(popularPage)
                popularLD.postValue(response.results)
                Log.e("mainVM", "${response.results}")
                popularPage ++
                isErrorVisible.postValue(false)
            } catch (e: Exception) {
                Log.e("main", "getPost: ${e.message}")
                isErrorVisible.postValue(true)
            }
        }
    }

}
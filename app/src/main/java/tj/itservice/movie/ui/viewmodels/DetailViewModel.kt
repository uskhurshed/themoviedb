package tj.itservice.movie.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import tj.itservice.movie.data.MovieResult
import tj.itservice.movie.db.MovieDao
import tj.itservice.movie.di.Repository
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val postRepository: Repository, private val movieDao: MovieDao): ViewModel() {

    val movieList: MutableLiveData<MovieResult?> = MutableLiveData()
    val isFavorite = MutableLiveData<Boolean>()

    val error: MutableLiveData<String> = MutableLiveData()
    private var movie:MovieResult? = null

    fun load(id: Long) = viewModelScope.launch {
        withContext(Dispatchers.IO) { movie = movieDao.getMovieById(id) }
        if (movie != null) {
            movieList.postValue(movie)
        } else {
            try {
                val response = postRepository.getMovieDetails(id)
                movieList.postValue(response)
                Log.d("response", "$response")
            } catch (e: Exception) {
                error.postValue(e.message)
                Log.d("response", "Error: ${e.message}")
            }
        }
    }

    fun rate(id: Long, rating: Int, message: (String) -> Unit) = viewModelScope.launch {
        try {
            val mediaType = "application/json;charset=utf-8".toMediaTypeOrNull()
            val jsonContent = "{\"value\":$rating}"
            val requestBody = jsonContent.toRequestBody(mediaType)
            val response = postRepository.postRate(id, requestBody)
            message.invoke(response.string())
            Log.e("response", response.string())
        } catch (e: Exception) {
            Log.d("response", "Error: ${e.message}")
        }
    }


    fun deleteRate(id: Long, message: (String) -> Unit) = viewModelScope.launch {
        try {
            val response = postRepository.deleteRate(id)
            message.invoke(response.string())
            Log.e("response", response.string())
        } catch (e: Exception) {
            Log.e("response", "Error: $e")
        }
    }

    suspend fun toggleFavorite()  = with(movieDao) {
        withContext(Dispatchers.IO) {
            if (isFavorite.value == true) movieList.value?.let { deleteMovie(it) }
            else movieList.value?.let { insertMovie(it) }
        }
        checkHaving()
    }

    suspend fun checkHaving() = withContext(Dispatchers.IO) {
        if (movieList.value?.id?.let { movieDao.getMovieById(it) } == movieList.value) isFavorite.postValue(true)
        else isFavorite.postValue(false)
    }

}

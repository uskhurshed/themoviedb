package tj.itservice.movie.activities.details

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
import tj.itservice.movie.App
import tj.itservice.movie.data.MovieResult
import tj.itservice.movie.di.Repository
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val postRepository: Repository): ViewModel() {

    val detailLD: MutableLiveData<MovieResult?> = MutableLiveData()
    private val movieDao = App.database.movieDao()
    val isFavorite = MutableLiveData<Boolean>()
    val error: MutableLiveData<String> = MutableLiveData()


    fun load(id:Long) {
        viewModelScope.launch {
            try {
                val response = postRepository.getMovieDetails(id)
                detailLD.postValue (response)
            } catch (e: Exception) {
                error.postValue(e.message)
                Log.d("main", "getPost: ${e.message}")
            }
        }
    }

    fun rate(id:Long,rating:Int, message: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val mediaType = "application/json;charset=utf-8".toMediaTypeOrNull()
                val jsonContent = "{\"value\":$rating}"
                val requestBody = jsonContent.toRequestBody(mediaType)
                val response = postRepository.postRate(id,requestBody)
                message.invoke(response.string())
                Log.e("SUCV", response.string())
            } catch (e: Exception) {
                Log.d("main", "getPost: ${e.message}")
            }
        }

    }


    fun deleteRate(id:Long, message: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = postRepository.deleteRate(id)
                message.invoke(response.string())
            } catch (e: Exception) {
                Log.d("main", "getPost: ${e.message}")
            }
        }
    }

    suspend fun toggleFavorite() {
        withContext(Dispatchers.IO) {
            if (isFavorite.value == true) {
                detailLD.value?.let { movieDao.deleteMovie(it) }
                Log.e("database","deleted")
            } else {
                detailLD.value?.let { movieDao.insertMovie(it) }
                Log.e("database","added")
            }
        }
        checkHaving()
    }


    suspend fun checkHaving() {
        withContext(Dispatchers.IO) {
            if (detailLD.value?.id?.let { movieDao.getMovieById(it) } == detailLD.value) isFavorite.postValue(true)
            else isFavorite.postValue(false)
        }
    }


}

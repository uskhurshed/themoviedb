package tj.itservice.movie.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import tj.itservice.movie.data.MovieResult
import tj.itservice.movie.di.Repository
import tj.itservice.movie.utils.PagingSource
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel
@Inject constructor(private val postRepository: Repository) : ViewModel() {

    val movieList: MutableLiveData<List<MovieResult>> = MutableLiveData()
    val isError: MutableLiveData<Boolean> = MutableLiveData(false)

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

    val popularList = Pager(
        PagingConfig(
            pageSize = 20
        )
    ) {
        PagingSource(postRepository)
    }.liveData.cachedIn(viewModelScope).also { ld ->
        ld.observeForever { pagingData ->
            pagingData?.let {
            } ?: run {
            }
        }
    }



}

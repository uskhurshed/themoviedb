package tj.itservice.movie.di

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import okhttp3.ResponseBody
import tj.itservice.movie.data.Movie
import tj.itservice.movie.data.MovieResult
import tj.itservice.movie.request.ApiService
import javax.inject.Inject

class Repository @Inject constructor(private val apiService: ApiService) {

    suspend fun getUpcomingMovie(): Movie = withContext(Dispatchers.IO) {
        return@withContext apiService.getUpcomingMovie(1)
    }

    suspend fun getPopularMovie(page:Int): Movie = withContext(Dispatchers.IO) {
        return@withContext apiService.getPopularMovie(page)
    }
    suspend fun getMovieByQuery(query: String): Movie = withContext(Dispatchers.IO) {
        return@withContext apiService.searchMovies(query)
    }
    suspend fun getTopRatedMovie(page:Int): Movie = withContext(Dispatchers.IO) {
        return@withContext apiService.getTopRatedMovie(page)
    }

    suspend fun getMovieDetails(movieID: Long): MovieResult? = withContext(Dispatchers.IO) {
        return@withContext apiService.getDetails(movieID)
    }

    suspend fun postRate(movieID: Long,body:RequestBody): ResponseBody = withContext(Dispatchers.IO) {
        return@withContext apiService.rateMovie(movieID,body)
    }

    suspend fun deleteRate(movieID: Long): ResponseBody = withContext(Dispatchers.IO) {
        return@withContext apiService.deleteMovieRate(movieID)
    }
}

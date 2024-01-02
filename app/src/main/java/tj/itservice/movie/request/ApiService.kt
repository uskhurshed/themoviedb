package tj.itservice.movie.request

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import tj.itservice.movie.data.Movie
import tj.itservice.movie.data.MovieResult
import tj.itservice.movie.utils.ApiHelper

interface ApiService {


    @GET(ApiHelper.UPCOMING_MOVIE)
    @Headers("Authorization: Bearer " + ApiHelper.HEADER_API_KEY)
    suspend fun getUpcomingMovie(
        @Query("page") page: Int?,
        @Query("language") language: String = ApiHelper.LANGUAGE
    ): Movie

    @GET(ApiHelper.POPULAR_MOVIE)
    @Headers("Authorization: Bearer " + ApiHelper.HEADER_API_KEY)
    suspend fun getPopularMovie(
        @Query("page") page: Int?,
        @Query("language") language: String = ApiHelper.LANGUAGE
    ): Movie

    @GET(ApiHelper.TOP_RATED_MOVIE)
    @Headers("Authorization: Bearer " + ApiHelper.HEADER_API_KEY)
    suspend fun getTopRatedMovie(@Query("page") page: Int?): Movie


    @GET("3/search/movie")
    @Headers("Authorization: Bearer " + ApiHelper.HEADER_API_KEY)
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("language") language: String = ApiHelper.LANGUAGE
    ):Movie

    @Headers("Authorization: Bearer " + ApiHelper.HEADER_API_KEY)
    @POST("${ApiHelper.MOVIE_DETAILS}/rating")
    suspend fun rateMovie(@Path("movieId") movieId: Long, @Body ratingRequestBody: RequestBody):ResponseBody

    @Headers("Authorization: Bearer " + ApiHelper.HEADER_API_KEY)
    @DELETE("${ApiHelper.MOVIE_DETAILS}/rating")
    suspend fun deleteMovieRate(@Path("movieId") movieId: Long): ResponseBody


    @Headers("Authorization: Bearer " + ApiHelper.HEADER_API_KEY)
    @GET("3/movie/{movie_id}")
    suspend fun getDetails(@Path("movie_id") movieId: Long, @Query("language") language: String = "ru-RU"
    ): MovieResult?



}





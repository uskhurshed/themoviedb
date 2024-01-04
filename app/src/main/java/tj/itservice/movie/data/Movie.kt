package tj.itservice.movie.data

import com.squareup.moshi.Json

data class Movie(
    @Json(name = "page")
    val page: Int,

    @Json(name = "results")
    val results: List<MovieResult>,

    @Json(name = "total_pages")
    val totalPages: Int,

    @Json(name = "total_results")
    val totalResults: Int
)

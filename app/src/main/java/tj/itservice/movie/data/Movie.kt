package tj.itservice.movie.data

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: ArrayList<MovieResult>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)
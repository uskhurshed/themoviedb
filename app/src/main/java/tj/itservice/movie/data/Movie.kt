package tj.itservice.movie.data

data class Movie(
    val page: Int,
    val results: ArrayList<MovieResult>,
    val total_pages: Int,
    val total_results: Int
)


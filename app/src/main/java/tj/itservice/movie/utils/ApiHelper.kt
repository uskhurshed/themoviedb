package tj.itservice.movie.utils

object ApiHelper {
    const val BASE_URL = "https://api.themoviedb.org/"
    const val ACCOUNT_ID = "20833837"
    const val HEADER_API_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI5NzIyZDY4Njg1MjkyNTk0OWM0ZmNiNWY1NTNjYzAwYiIsInN1YiI6IjY1N2JkNDc4NjNlNmZiMDEzYjQ2MDE2YiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.JqjZnqflKQytzhEhw4cyQ1BU0Msfm_nIUCHOlwVbpgU"
    const val BASE_POSTER_PATH = "https://image.tmdb.org/t/p/w342"
    const val BASE_BACKDROP_PATH = "https://image.tmdb.org/t/p/w780"
    const val UPCOMING_MOVIE = "3/movie/upcoming"
    const val POPULAR_MOVIE = "3/movie/popular"
    const val TOP_RATED_MOVIE = "3/movie/top_rated"
    const val MOVIE_DETAILS = "3/movie/{movieId}"
    const val LANGUAGE = "ru-RU"
}

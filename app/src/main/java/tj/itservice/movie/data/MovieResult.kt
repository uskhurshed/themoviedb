package tj.itservice.movie.data

import androidx.room.Entity
import androidx.room.PrimaryKey

import com.squareup.moshi.Json
@Entity(tableName = "favorite_movies")

data class MovieResult(
    @PrimaryKey(autoGenerate = false)
    val id: Long?,

    @Json(name = "adult")
    val adult: Boolean,

    @Json(name = "backdrop_path")
    val backdropPath: String?,

    @Json(name = "original_language")
    val originalLanguage: String?,

    @Json(name = "overview")
    val overview: String?,

    @Json(name = "poster_path")
    val posterPath: String?,

    @Json(name = "release_date")
    val releaseDate: String?,

    @Json(name = "title")
    val title: String?,

    @Json(name = "vote_average")
    val voteAverage: Double?,

    @Json(name = "revenue")
    val revenue: Long?,

    @Json(name = "original_title")
    val originalTitle: String?,

    @Json(name = "genres")
    val genres: List<Genres>?
)



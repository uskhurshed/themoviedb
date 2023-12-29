package tj.itservice.movie.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class MovieResult(
    @PrimaryKey(autoGenerate = false)
    val id: Long?,
    val adult: Boolean,
    val backdrop_path: String?,
    val original_language: String?,
    val overview: String?,
    val poster_path: String?,
    val release_date: String?,
    val title: String?,
    val vote_average: Double?,
    val revenue: Long?,
    val original_title: String?,
    val genres: List<Genres?>?
)

package tj.itservice.movie.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "favorite_movies")
data class MovieResult(
    @PrimaryKey(autoGenerate = false)
    val id: Long?,

    @SerializedName("adult")
    val adult: Boolean,

    @SerializedName("backdrop_path")
    val backdropPath: String?,

    @SerializedName("original_language")
    val originalLanguage: String?,

    @SerializedName("overview")
    val overview: String?,

    @SerializedName("poster_path")
    val posterPath: String?,

    @SerializedName("release_date")
    val releaseDate: String?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("vote_average")
    val voteAverage: Double?,

    @SerializedName("revenue")
    val revenue: Long?,

    @SerializedName("original_title")
    val originalTitle: String?,

    @SerializedName("genres")
    val genres: List<Genres?>?
)



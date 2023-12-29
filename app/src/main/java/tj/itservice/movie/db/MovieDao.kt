package tj.itservice.movie.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tj.itservice.movie.data.MovieResult

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: MovieResult)

    @Delete
    fun deleteMovie(movie: MovieResult)

    @Query("SELECT * FROM favorite_movies")
    fun getAllMovies(): List<MovieResult>

    @Query("SELECT * FROM favorite_movies WHERE id = :movieId")
    fun getMovieById(movieId: Long): MovieResult?
}
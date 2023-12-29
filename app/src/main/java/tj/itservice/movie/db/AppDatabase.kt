package tj.itservice.movie.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import tj.itservice.movie.data.MovieResult
import tj.itservice.movie.utils.Converters


@Database(entities = [MovieResult::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}

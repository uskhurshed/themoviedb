package tj.itservice.movie.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import tj.itservice.movie.data.Genres

class Converters {
    @TypeConverter
    fun fromGenresList(genres: List<Genres?>?): String? {
        return Gson().toJson(genres)
    }

    @TypeConverter
    fun toGenresList(genresString: String?): List<Genres?>? {
        val type = object : TypeToken<List<Genres?>>() {}.type
        return Gson().fromJson(genresString, type)
    }
}

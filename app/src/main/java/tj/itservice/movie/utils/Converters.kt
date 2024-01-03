package tj.itservice.movie.utils
import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import tj.itservice.movie.data.Genres
import java.lang.reflect.Type

class Converters {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @TypeConverter
    fun fromGenresList(genres: List<Genres>?): String? {
        val type: Type = Types.newParameterizedType(List::class.java, Genres::class.java)
        val adapter: JsonAdapter<List<Genres>> = moshi.adapter(type)
        return adapter.toJson(genres)
    }

    @TypeConverter
    fun toGenresList(genresString: String?): List<Genres?>? {
        val type: Type = Types.newParameterizedType(List::class.java, Genres::class.java)
        val adapter: JsonAdapter<List<Genres>> = moshi.adapter(type)
        return genresString?.let { adapter.fromJson(it) }
    }
}

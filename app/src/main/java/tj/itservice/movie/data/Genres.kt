package tj.itservice.movie.data

import com.squareup.moshi.Json

data class Genres (
   val id: Int?,
   @Json(name = "name")
   val name: String
)

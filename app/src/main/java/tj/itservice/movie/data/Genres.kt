package tj.itservice.movie.data

import com.google.gson.annotations.SerializedName

data class Genres (
   val id:Int?,
   @SerializedName("name")
   val name: String
)
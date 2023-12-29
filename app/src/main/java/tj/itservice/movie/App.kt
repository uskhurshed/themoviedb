package tj.itservice.movie

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp
import androidx.room.Room
import tj.itservice.movie.db.AppDatabase
import tj.itservice.movie.utils.MoreFunction

@HiltAndroidApp
class App : Application() {
    companion object {
        lateinit var database: AppDatabase
            private set
    }
    override fun onCreate() {
        super.onCreate()

        when (MoreFunction.getFromPref(applicationContext,"isNight",false)) {
            true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "my_database").build()
    }
}

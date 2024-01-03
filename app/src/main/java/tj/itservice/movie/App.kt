package tj.itservice.movie

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp
import tj.itservice.movie.utils.MoreFunction

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        when (MoreFunction.getFromPref(applicationContext,"isNight",false)) {
            true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}

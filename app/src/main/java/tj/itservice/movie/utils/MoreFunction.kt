package tj.itservice.movie.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import tj.itservice.movie.BuildConfig

object MoreFunction {

    fun getFromPref(context: Context, key: String, defValue: Boolean): Boolean {
        val prefs = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        return prefs.getBoolean(key, defValue)
    }

    fun setFromPref(context: Context, key: String, value: Boolean) {
        val prefs = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(key, value).apply()
    }

    fun share(context: Context, appName: String, shareMessage: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, appName)
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        context.startActivity(Intent.createChooser(shareIntent, "Выбор:"))
    }

    fun goWithUrl(context: Context, url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    }
}

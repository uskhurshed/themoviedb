package tj.itservice.movie.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import tj.itservice.movie.R

class ErrorManager(private val context: Context, private val container: ViewGroup) {

    private var errorView: View? = null
    fun showErrorMessage(onRetryClick: () -> Unit) {
        if (errorView == null) {
            errorView = LayoutInflater.from(context).inflate(R.layout.error_state, container, false)
            errorView?.findViewById<Button>(R.id.button)?.setOnClickListener {
                container.removeView(errorView)
                errorView = null
                onRetryClick.invoke()
            }
            container.addView(errorView)
        }
    }
}

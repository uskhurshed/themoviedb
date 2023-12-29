package tj.itservice.movie.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import tj.itservice.movie.R

class RateDialog(private val context: Context) {

    private lateinit var alertDialog: AlertDialog
    private var onRateListener: ((Float) -> Unit)? = null
    private var onDeleteListener: (() -> Unit)? = null

    @SuppressLint("SetTextI18n")
    fun show() {
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView: View = inflater.inflate(R.layout.dialog_rate, null)

        dialogBuilder.setView(dialogView)

        val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBar)
        val status = dialogView.findViewById<TextView>(R.id.tv_status)
        val btnRate = dialogView.findViewById<Button>(R.id.goRate)
        val btnDel = dialogView.findViewById<Button>(R.id.goDel)

        alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        alertDialog.show()

        btnRate.setOnClickListener {
            onRateListener?.invoke(ratingBar.rating)
            alertDialog.dismiss()
        }

        btnDel.setOnClickListener {
            onDeleteListener?.invoke()
            alertDialog.dismiss()
        }

        ratingBar.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { _, rating, _ ->
            status.text = "Оценка: ${rating * 2}"
        }
    }

    fun setOnRateListener(listener: (Float) -> Unit) {
        onRateListener = listener
    }

    fun setOnDeleteListener(listener: () -> Unit) {
        onDeleteListener = listener
    }
}

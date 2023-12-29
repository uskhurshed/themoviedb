package tj.itservice.movie.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import tj.itservice.movie.R

class LoadingDialog(context: Context) : Dialog(context) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setCancelable(false)
    }
}

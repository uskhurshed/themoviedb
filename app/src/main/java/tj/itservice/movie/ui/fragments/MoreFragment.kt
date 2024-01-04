package tj.itservice.movie.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import tj.itservice.movie.BuildConfig
import tj.itservice.movie.R
import tj.itservice.movie.databinding.FragmentMoreBinding
import tj.itservice.movie.utils.MoreFunction
import tj.itservice.movie.utils.MoreFunction.goWithUrl
import tj.itservice.movie.utils.MoreFunction.share


class MoreFragment : Fragment(),View.OnClickListener{

    private lateinit var bindMore: FragmentMoreBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        bindMore = FragmentMoreBinding.inflate(inflater, container, false)
        return bindMore.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindMore.apply {

            darkSwitch.isChecked = MoreFunction.getFromPref(requireContext(),"isNight",false)
            darkSwitch.setOnCheckedChangeListener { _, isChecked ->
                MoreFunction.setFromPref(requireContext(),"isNight",isChecked)
                if (isChecked)AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            btnRate.setOnClickListener(this@MoreFragment)
            btnApps.setOnClickListener(this@MoreFragment)
            btnShare.setOnClickListener (this@MoreFragment)
            btnAbout.setOnClickListener(this@MoreFragment)
        }
    }

    override fun onClick(view: View?) {
        val ctx = requireContext()
        when (view?.id){
            R.id.btn_rate -> goWithUrl(ctx,"https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")
            R.id.btn_apps -> goWithUrl(ctx,"https://play.google.com/store/apps/dev?id=7468148183308310395")
            R.id.btn_share -> share(ctx,"movie","Скачай приложение бесплатно!")
            R.id.btn_about -> Snackbar.make(view, "Программу создал Khurshed Usmonov.", Snackbar.LENGTH_LONG).show()
        }
    }
}
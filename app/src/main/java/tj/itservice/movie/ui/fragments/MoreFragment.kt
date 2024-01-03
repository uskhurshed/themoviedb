package tj.itservice.movie.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import tj.itservice.movie.BuildConfig
import tj.itservice.movie.databinding.FragmentMoreBinding
import tj.itservice.movie.utils.MoreFunction
import tj.itservice.movie.utils.MoreFunction.goWithUrl
import tj.itservice.movie.utils.MoreFunction.share


class MoreFragment : Fragment() {

    private lateinit var bindMore: FragmentMoreBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        bindMore = FragmentMoreBinding.inflate(inflater, container, false)
        return bindMore.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindMore.switch1.isChecked = MoreFunction.getFromPref(requireContext(),"isNight",false)

        bindMore.switch1.setOnCheckedChangeListener { _, isChecked ->
            MoreFunction.setFromPref(requireContext(),"isNight",isChecked)
            when (isChecked) {
                true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

        }
            bindMore.btnRate.setOnClickListener { goWithUrl(requireContext(),"https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}") }
            bindMore.btnApps.setOnClickListener { goWithUrl(requireContext(),"https://play.google.com/store/apps/dev?id=7468148183308310395") }
            bindMore.btnShare.setOnClickListener { share(requireContext(),"movie","Скачай приложение бесплатно!") }
            bindMore.btnAbout.setOnClickListener { Snackbar.make(requireView(), "Программу создал Khurshed Usmonov.", Snackbar.LENGTH_LONG).show()  }

    }
}
package tj.itservice.movie

import android.os.Bundle
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import tj.itservice.movie.R
import tj.itservice.movie.databinding.ActivityMainBinding
import tj.itservice.movie.ui.viewmodels.BottomViewModel


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val bottomViewModel by viewModels<BottomViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        binding.bottomViewModel = bottomViewModel

        navController = findNavController(R.id.main_fragment)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> bottomViewModel.showBottomNavigation()
                R.id.discoverFragment -> bottomViewModel.hideBottomNavigation()
                R.id.detailsFragment -> bottomViewModel.hideBottomNavigation()
                R.id.ratingFragment -> bottomViewModel.showBottomNavigation()
                R.id.favoriteFragment -> bottomViewModel.showBottomNavigation()
                R.id.moreFragment -> bottomViewModel.showBottomNavigation()
            }
        }
        setupSmoothBottomMenu()
    }

    private fun setupSmoothBottomMenu() {
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.menu_nav)
        binding.bottomNavigationView.setupWithNavController(navController)
    }

}
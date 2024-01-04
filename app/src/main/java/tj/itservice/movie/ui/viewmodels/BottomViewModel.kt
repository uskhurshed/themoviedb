package tj.itservice.movie.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BottomViewModel : ViewModel() {

    val isBottomNavigationVisible = MutableLiveData(true)

    fun hideBottomNavigation() = with(isBottomNavigationVisible) {
        value = false
    }

    fun showBottomNavigation() = with(isBottomNavigationVisible) {
        value = true
    }
}

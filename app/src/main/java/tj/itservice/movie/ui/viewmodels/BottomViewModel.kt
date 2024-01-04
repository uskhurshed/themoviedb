package tj.itservice.movie.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BottomViewModel : ViewModel() {

    val isBottomNavigationVisible = MutableLiveData<Boolean>(true)

    fun hideBottomNavigation() {
        isBottomNavigationVisible.value = false
    }

    fun showBottomNavigation() {
        isBottomNavigationVisible.value = true
    }
}

package me.sujanpoudel.mvidemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import me.sujanpoudel.mvidemo.MainDirections.Companion.actionToAuth
import me.sujanpoudel.mvidemo.data.Preferences
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (!preferences.isUserLoggedIn) {
            (supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment?)?.let {
                it.navController.popBackStack()
                it.navController.navigate(actionToAuth())
            }
        }

    }
}
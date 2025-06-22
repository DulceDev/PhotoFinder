package ar.edu.uade.valentin_lanus.photofinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import ar.edu.uade.valentin_lanus.photofinder.ui.navigation.AppNavigation
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            val navController = rememberNavController()
            AppNavigation(navController)
        }
    }
}

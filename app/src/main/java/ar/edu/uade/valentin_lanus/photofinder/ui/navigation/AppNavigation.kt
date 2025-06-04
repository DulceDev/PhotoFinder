package ar.edu.uade.valentin_lanus.photofinder.ui.navigation


import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.login.LoginScreen
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.photodetail.PhotoDetailScreen
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.photolist.PhotoListScreen
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.photolist.PhotoListViewModel
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.splash.SplashScreenn
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(navController: NavHostController) {
    val viewModel: PhotoListViewModel = viewModel()

    NavHost(navController = navController, startDestination = "splash"){
        composable("splash") {
            SplashScreenn(navController)
        }
        composable("login") {
            LoginScreen(navController)
        }
        composable("home"){
            PhotoListScreen(navController = navController,
                viewModel = viewModel,
                onLogout = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("login") {
                        popUpTo("hoke") { inclusive = true}
                    }
                })
        }
        composable("detail/{imageId}") { backStackEntry ->
            PhotoDetailScreen(backStackEntry, viewModel)
        }
    }
}
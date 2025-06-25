package ar.edu.uade.valentin_lanus.photofinder.ui.navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ar.edu.uade.valentin_lanus.photofinder.Secrets
import ar.edu.uade.valentin_lanus.photofinder.data.api.RetrofitInstance
import ar.edu.uade.valentin_lanus.photofinder.data.local.AppDatabase
import ar.edu.uade.valentin_lanus.photofinder.data.repository.PhotoRepositoryImpl
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.main.MainScreen
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.login.LoginScreen
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.photodetail.PhotoDetailScreen
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.photodetail.PhotoDetailViewModel
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.photolist.PhotoListViewModel
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.profile.ProfileViewModel
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.splash.SplashScreenn
import ar.edu.uade.valentin_lanus.photofinder.ui.viewmodel.PhotoViewModelFactory
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(navController: NavHostController) {
    val context = LocalContext.current

    val repository = remember {
        PhotoRepositoryImpl(
            apiService = RetrofitInstance.api,
            clientId = Secrets.unsplash_api
        )
    }

    val factory = remember { PhotoViewModelFactory(repository) }
    val listViewModel: PhotoListViewModel = viewModel(factory = factory)

    // Instanciación manual de DAO y ViewModels locales
    val dao = remember { AppDatabase.getInstance(context).likedPhotoDao() }
    val detailViewModel = remember { PhotoDetailViewModel(dao) }
    val profileViewModel = remember { ProfileViewModel(dao) }

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreenn(navController)
        }
        composable("login") {
            LoginScreen(navController)
        }
        composable("home") {
            MainScreen(
                onLogout = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                photoListViewModel = listViewModel,
                detailViewModel = detailViewModel,
                profileViewModel = profileViewModel
            )
        }
        composable("detail/{imageId}") {
            val photo = detailViewModel.photo.value
            PhotoDetailScreen(photo = photo, viewModel = detailViewModel)
        }
    }
}
package ar.edu.uade.valentin_lanus.photofinder.ui.navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.login.LoginScreen
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.photodetail.PhotoDetailScreen
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.photodetail.PhotoDetailViewModel
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.photolist.PhotoListScreen
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.photolist.PhotoListViewModel
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.profile.ProfileScreen
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.profile.ProfileViewModel
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.splash.SplashScreenn
import ar.edu.uade.valentin_lanus.photofinder.ui.viewmodel.PhotoViewModelFactory
import androidx.compose.runtime.getValue
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.photolist.PhotoListState

@Composable
fun AppNavigation(navController: NavHostController) {
    val context = LocalContext.current

    val dao = remember { AppDatabase.getInstance(context).likedPhotoDao() }

    val repository = remember {
        PhotoRepositoryImpl(
            apiService = RetrofitInstance.api,
            clientId = Secrets.unsplash_api,
            likedPhotoDao = dao
        )
    }

    val factory = remember { PhotoViewModelFactory(repository) }
    val listViewModel: PhotoListViewModel = viewModel(factory = factory)

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
            PhotoListScreen(
                navController = navController,
                viewModel = listViewModel,
                detailViewModel = detailViewModel
            )
        }
        composable("profile") {
            ProfileScreen(
                navController = navController,
                viewModel = profileViewModel
            )
        }
        composable("detail/{imageId}") { backStackEntry ->
            val imageId = backStackEntry.arguments?.getString("imageId")

            val likedPhotos by profileViewModel.likedPhotos.collectAsState()
            val homePhotos = listViewModel.uiState.let { state ->
                if (state is PhotoListState.Success) state.photos else emptyList()
            }

            val combinedPhotos = remember(likedPhotos, homePhotos) {
                (homePhotos + likedPhotos).distinctBy { it.id }
            }

            val selectedPhoto = combinedPhotos.find { it.id == imageId }

            LaunchedEffect(selectedPhoto?.id) {
                selectedPhoto?.let { detailViewModel.setPhoto(it) }
            }

            val currentPhoto by detailViewModel.photo.collectAsState()

            PhotoDetailScreen(
                photo = currentPhoto,
                detailViewModel = detailViewModel,
                profileViewModel = profileViewModel,
                navController = navController
            )
        }

    }
}
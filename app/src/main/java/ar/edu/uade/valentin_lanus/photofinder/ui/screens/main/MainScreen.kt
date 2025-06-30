package ar.edu.uade.valentin_lanus.photofinder.ui.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.photolist.PhotoListScreen
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.photolist.PhotoListViewModel
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.profile.ProfileScreen
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.profile.ProfileViewModel
import androidx.compose.runtime.mutableStateOf

@Composable
fun MainScreen(
    navController: NavHostController,
    photoListViewModel: PhotoListViewModel,
    profileViewModel: ProfileViewModel,
    onLogout: () -> Unit
) {
    var currentScreen by remember { mutableStateOf("home") }

    Scaffold(
        bottomBar = {
            BottomNavigation {
                BottomNavigationItem(
                    selected = currentScreen == "home",
                    onClick = { currentScreen = "home" },
                    icon = { Icon(Icons.Default.Home, "Inicio") },
                    label = { Text("Inicio") }
                )
                BottomNavigationItem(
                    selected = currentScreen == "profile",
                    onClick = { currentScreen = "profile" },
                    icon = { Icon(Icons.Default.Person, "Perfil") },
                    label = { Text("Perfil") }
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (currentScreen) {
                "home" -> PhotoListScreen(
                    viewModel = photoListViewModel,
                    onPhotoClick = { imageId ->
                        navController.navigate("detail/$imageId")
                    }
                )
                "profile" -> ProfileScreen(
                    viewModel = profileViewModel,
                    onLogout = onLogout
                )
            }
        }
    }
}


package ar.edu.uade.valentin_lanus.photofinder.ui.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.photodetail.PhotoDetailViewModel
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.photolist.PhotoListScreen
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.photolist.PhotoListViewModel
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.profile.ProfileScreen
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.profile.ProfileViewModel

sealed class BottomNavItem(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : BottomNavItem("home_main", "Inicio", Icons.Default.Home)
    object Profile : BottomNavItem("profile", "Perfil", Icons.Default.Person)
}

@Composable
fun MainScreen(
    onLogout: () -> Unit,
    photoListViewModel: PhotoListViewModel,
    detailViewModel: PhotoDetailViewModel,
    profileViewModel: ProfileViewModel
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(BottomNavItem.Home.route) {
                PhotoListScreen(
                    navController = navController,
                    viewModel = photoListViewModel,
                    onLogout = onLogout,
                    detailViewModel = detailViewModel
                )
            }

            composable(BottomNavItem.Profile.route) {
                ProfileScreen(
                    viewModel = profileViewModel,
                    onLogout = onLogout
                )
            }
        }
    }
}

package ar.edu.uade.valentin_lanus.photofinder.ui.screens.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Home : BottomNavItem("home_main", "Inicio", Icons.Default.Home)
    object Profile : BottomNavItem("profile", "Perfil", Icons.Default.Person)
}
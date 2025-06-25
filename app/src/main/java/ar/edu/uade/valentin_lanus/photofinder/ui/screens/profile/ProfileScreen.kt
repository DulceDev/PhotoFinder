package ar.edu.uade.valentin_lanus.photofinder.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLogout: () -> Unit
) {
    val likedPhotos = viewModel.likedPhotos.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Mi Perfil", style = MaterialTheme.typography.titleLarge)
            Button(onClick = onLogout) {
                Text("Logout")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(likedPhotos.value) { photo ->
                Image(
                    painter = rememberImagePainter(photo.urls.small),
                    contentDescription = photo.description,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            }
        }
    }
}
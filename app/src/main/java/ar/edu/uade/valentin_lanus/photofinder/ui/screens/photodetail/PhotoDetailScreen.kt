package ar.edu.uade.valentin_lanus.photofinder.ui.screens.photodetail

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ar.edu.uade.valentin_lanus.photofinder.data.model.Photo
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.profile.ProfileViewModel
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch

@Composable
fun PhotoDetailScreen(
    photo: Photo?,
    detailViewModel: PhotoDetailViewModel,
    profileViewModel: ProfileViewModel,
    navController: NavHostController
    ){
    if (photo == null) {
        Text("Foto no encontrada", modifier = Modifier.padding(16.dp))
        return
    }
    val isLiked by detailViewModel.isLiked.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF001F3F), Color(0xFF15796C))
                )
            )
            .padding(20.dp)
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 250.dp, max = 400.dp)
                .clip(RoundedCornerShape(20.dp))
        ){
            Image(
                painter = rememberImagePainter(photo.urls.small),
                contentDescription = "Imagen detallada",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)))
                    )
                    .padding(12.dp)
            ){
                Text(
                    text = "Por: ${photo.user.name}",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF5F1E8))
                .padding(16.dp)
        ) {
            Text(
                text = photo.description ?: "Descripción no disponible",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically){
                val likeCount by detailViewModel.likeCount.collectAsState()

                val scale = remember { Animatable(1f) }
                val coroutineScope = rememberCoroutineScope()

                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Likes",
                    tint = if (isLiked) Color.Red else Color.Gray,
                    modifier = Modifier
                        .clickable {
                            coroutineScope.launch {
                                scale.animateTo(
                                    targetValue = 1.3f,
                                    animationSpec = tween(durationMillis = 150)
                                )
                                scale.animateTo(
                                    targetValue = 1f,
                                    animationSpec = tween(durationMillis = 150)
                                )

                                val likedNow = detailViewModel.toggleLike()
                                if (likedNow) {
                                    profileViewModel.addPhoto(photo)
                                } else {
                                    profileViewModel.removePhoto(photo)
                                }
                                profileViewModel.loadLikedPhotos()
                            }
                        }
                        .size((24 * scale.value).dp) // escala dinámica
                )


                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "$likeCount Me gustas",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )

            }
        }
    }
}
package ar.edu.uade.valentin_lanus.photofinder.ui.screens.photolist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ar.edu.uade.valentin_lanus.photofinder.ui.screens.photodetail.PhotoDetailViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PhotoListScreen(
    navController: NavHostController,
    viewModel: PhotoListViewModel,
    detailViewModel: PhotoDetailViewModel
){
    var searchQuery by remember { mutableStateOf("") }
    val uiState = viewModel.uiState
    val gridState = rememberLazyGridState()

    LaunchedEffect(Unit) {
        viewModel.fetchInitialImages()
    }

    LaunchedEffect(gridState) {
        snapshotFlow {  gridState.layoutInfo.visibleItemsInfo }
            .collectLatest{ visibleItems ->
                val totalItems = gridState.layoutInfo.totalItemsCount
                val lastVisibleItem = visibleItems.lastOrNull()?.index ?: 0

                if (lastVisibleItem >= totalItems - 4 && !viewModel.isLoading() && !viewModel.isEndReached()) {
                    viewModel.loadImages()
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF001F3F), Color(0xFF15796C))
                )
            )
            .padding(16.dp)
    ){
        // header con buscador y perfil
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar imagenes") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color(0xFFF5F1E8),
                    unfocusedContainerColor = Color(0xFFF5F1E8)
                ),
                modifier = Modifier.weight(1f),
                trailingIcon = {
                    IconButton(onClick = {
                        // Busca imagenes
                        viewModel.searchImages(searchQuery)
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                }
            )

            IconButton(
                onClick = {
                    navController.navigate("profile")
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Perfil",
                    tint = Color.White
                )
            }
        }

        when (uiState) {
            is PhotoListState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }

            is PhotoListState.Error -> {
                Box(modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = uiState.message,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            is PhotoListState.Success -> {
                val images = uiState.photos

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    state = gridState,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()

                ) {
                    items(images.size) { index ->
                        val image = images[index]
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    detailViewModel.setPhoto(image)
                                    navController.navigate("detail/{imageId}")
                                }
                        ){
                            Image(
                                painter = rememberImagePainter(data = image.urls.small),
                                contentDescription = image.user.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height((150..300).random().dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomStart)
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color.Black.copy(alpha = 0.6f)
                                            )
                                        )
                                    )
                                    .padding(8.dp)
                            ){
                                Text(
                                    text = image.user.name,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    if (viewModel.isLoading()){
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

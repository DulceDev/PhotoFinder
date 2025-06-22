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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
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
import ar.edu.uade.valentin_lanus.photofinder.Secrets
import ar.edu.uade.valentin_lanus.photofinder.data.api.RetrofitInstance
import ar.edu.uade.valentin_lanus.photofinder.data.repository.PhotoRepositoryImpl

@Composable
fun PhotoListScreen(
    navController: NavHostController,
    viewModel: PhotoListViewModel,
    onLogout: () -> Unit
){
    val repository = remember {
        PhotoRepositoryImpl(RetrofitInstance.api, Secrets.unsplash_api)
    }

    val viewModel = remember {
        PhotoListViewModel(repository)
    }

    var searchQuery by remember { mutableStateOf("") }
    val state = viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.fetchInitialImages()
    }

    Column (modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                viewModel.searchImages(searchQuery)
            },
            label = { Text("Buscar imagenes") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        when (state) {
            is PhotoListState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is PhotoListState.Success -> {
                val listState = rememberLazyGridState()
                val photos = state.photos

                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(photos){ photo ->
                        PhotoItem(photo)
                    }
                }
            }
        }
    }
}
/*
fun PhotoListScreen(
    navController: NavHostController,
    viewModel: PhotoListViewModel,
    onLogout: () -> Unit
){
    val images = viewModel.images
    val isLoading = viewModel.isLoading
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.fetchInitialImages()
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
    ) {
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
                        viewModel.searchImages(searchQuery)
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                }
            )

            IconButton(
                onClick = onLogout,
                modifier = Modifier
                    .padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Cerrar Sesion",
                    tint = Color.White
                )
            }
        }


        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(images) { index, image ->
                if (index >= images.lastIndex -1 && !isLoading) {
                    LaunchedEffect(Unit) {
                        viewModel.loadMoreImages()
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            navController.navigate("detail/${image.id}")
                        }
                ) {
                    Image(
                        painter = rememberImagePainter(image.urls.small),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .heightIn(min = 180.dp, max = 300.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomStart)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                                    startY = 0f,
                                    endY = 300f
                                )
                            )
                            .padding(8.dp)
                    ) {
                        Text(
                            text = image.user.name,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            if (isLoading) {
                item(span = { GridItemSpan(2) }) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(24.dp)
                                .size(36.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}*/

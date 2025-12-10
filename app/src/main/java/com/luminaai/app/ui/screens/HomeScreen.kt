package com.luminaai.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.luminaai.app.data.model.Generation
import com.luminaai.app.ui.theme.LimeGreen

@Composable
fun HomeScreen(viewModel: MusicViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val completedSongs = uiState.generations.filter { it.status == "completed" }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D))
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Selamat Datang",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Lumina AI",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            
            Surface(
                shape = CircleShape,
                color = LimeGreen.copy(alpha = 0.2f)
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = LimeGreen,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
        
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = LimeGreen)
            }
        } else if (completedSongs.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.MusicOff,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Belum ada musik", color = Color.Gray)
                    Text("Mulai buat musik pertamamu!", color = Color.Gray, fontSize = 14.sp)
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(completedSongs) { song ->
                    SongCard(
                        song = song,
                        onPlay = { viewModel.playSong(song) },
                        onFavorite = { viewModel.toggleFavorite(song.id) },
                        isPlaying = uiState.currentSong?.id == song.id && uiState.isPlaying
                    )
                }
            }
        }
    }
}

@Composable
fun SongCard(
    song: Generation,
    onPlay: () -> Unit,
    onFavorite: () -> Unit,
    isPlaying: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlay() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPlaying) LimeGreen.copy(alpha = 0.1f) else Color(0xFF1A1A1A)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Album art
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(LimeGreen.copy(alpha = 0.3f), Color(0xFF22C55E).copy(alpha = 0.3f))
                        )
                    )
            ) {
                if (song.fullThumbnailUrl.isNotEmpty()) {
                    AsyncImage(
                        model = song.fullThumbnailUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = null,
                        tint = LimeGreen,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(32.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = song.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = LimeGreen.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = song.displayGenre,
                            color = LimeGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    if (song.isFavorite) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    if (song.isPublic) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = null,
                            tint = Color.Green,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
            
            // Favorite button
            IconButton(onClick = onFavorite) {
                Icon(
                    imageVector = if (song.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (song.isFavorite) Color.Red else Color.Gray
                )
            }
            
            // Play button
            Surface(
                shape = CircleShape,
                color = LimeGreen,
                modifier = Modifier.size(44.dp)
            ) {
                IconButton(onClick = onPlay) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.Black
                    )
                }
            }
        }
    }
}

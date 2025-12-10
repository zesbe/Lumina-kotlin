package com.luminaai.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luminaai.app.ui.theme.LimeGreen

@Composable
fun CollectionScreen(viewModel: MusicViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Semua", "Favorit", "Proses")
    
    val completedSongs = uiState.generations.filter { it.status == "completed" }
    val favoriteSongs = completedSongs.filter { it.isFavorite }
    val processingSongs = uiState.generations.filter { it.status == "processing" }
    
    val displayedSongs = when (selectedTab) {
        0 -> completedSongs
        1 -> favoriteSongs
        2 -> processingSongs
        else -> completedSongs
    }
    
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
            Text(
                text = "Koleksi Musik",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = LimeGreen.copy(alpha = 0.2f)
            ) {
                Text(
                    text = "${completedSongs.size} lagu",
                    color = LimeGreen,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
        
        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color(0xFF1A1A1A),
            contentColor = Color.White,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = LimeGreen
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            color = if (selectedTab == index) LimeGreen else Color.Gray
                        )
                    }
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
        } else if (displayedSongs.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = when (selectedTab) {
                            1 -> Icons.Default.FavoriteBorder
                            2 -> Icons.Default.HourglassEmpty
                            else -> Icons.Default.LibraryMusic
                        },
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = when (selectedTab) {
                            1 -> "Tidak ada favorit"
                            2 -> "Tidak ada yang diproses"
                            else -> "Tidak ada musik"
                        },
                        color = Color.Gray
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(displayedSongs) { song ->
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

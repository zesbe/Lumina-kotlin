package com.luminaai.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(viewModel: MusicViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    
    var title by remember { mutableStateOf("") }
    var prompt by remember { mutableStateOf("") }
    var lyrics by remember { mutableStateOf("") }
    var style by remember { mutableStateOf("") }
    var showSuccess by remember { mutableStateOf(false) }
    
    LaunchedEffect(uiState.isGenerating) {
        if (!uiState.isGenerating && showSuccess) {
            // Reset form after successful generation
            title = ""
            prompt = ""
            lyrics = ""
            style = ""
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = null,
                tint = LimeGreen,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Buat Musik",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Buat musik dengan AI",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Form
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Title
                Text("Judul Lagu", color = Color.White, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Masukkan judul lagu") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LimeGreen,
                        cursorColor = LimeGreen
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Style/Genre
                Text("Genre/Style", color = Color.White, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = style,
                    onValueChange = { style = it },
                    placeholder = { Text("Pop, Rock, Jazz, dll") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LimeGreen,
                        cursorColor = LimeGreen
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Prompt
                Text("Deskripsi Musik", color = Color.White, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = prompt,
                    onValueChange = { prompt = it },
                    placeholder = { Text("Deskripsikan musik yang ingin dibuat...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LimeGreen,
                        cursorColor = LimeGreen
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Lyrics
                Text("Lirik", color = Color.White, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = lyrics,
                    onValueChange = { lyrics = it },
                    placeholder = { Text("Masukkan lirik lagu...\n\n[Verse]\n...\n\n[Chorus]\n...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LimeGreen,
                        cursorColor = LimeGreen
                    )
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Error message
        if (uiState.error != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = null,
                        tint = Color.Red
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(uiState.error!!, color = Color.Red)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Generate button
        Button(
            onClick = {
                showSuccess = true
                viewModel.generateMusic(title, prompt, lyrics, style.ifBlank { null })
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LimeGreen),
            enabled = !uiState.isGenerating && title.isNotBlank() && lyrics.isNotBlank()
        ) {
            if (uiState.isGenerating) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.Black,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Generating...", color = Color.Black, fontWeight = FontWeight.Bold)
            } else {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Generate Musik", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Info
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = LimeGreen.copy(alpha = 0.1f))
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = LimeGreen
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Musik akan diproses dalam beberapa menit",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

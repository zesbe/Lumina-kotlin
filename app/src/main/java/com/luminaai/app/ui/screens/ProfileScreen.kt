package com.luminaai.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.luminaai.app.ui.theme.LimeGreen

@Composable
fun ProfileScreen(
    onLogout: () -> Unit
) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val user by authViewModel.user.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D))
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Profil",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Profile Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar
                Surface(
                    shape = CircleShape,
                    color = LimeGreen.copy(alpha = 0.2f),
                    modifier = Modifier.size(80.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = LimeGreen,
                        modifier = Modifier
                            .padding(20.dp)
                            .size(40.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = user?.name ?: "User",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Text(
                    text = user?.email ?: "email@example.com",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Menu items
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
        ) {
            Column {
                ProfileMenuItem(
                    icon = Icons.Default.Settings,
                    title = "Pengaturan",
                    onClick = { }
                )
                Divider(color = Color.Gray.copy(alpha = 0.2f))
                ProfileMenuItem(
                    icon = Icons.Default.Help,
                    title = "Bantuan",
                    onClick = { }
                )
                Divider(color = Color.Gray.copy(alpha = 0.2f))
                ProfileMenuItem(
                    icon = Icons.Default.Info,
                    title = "Tentang Aplikasi",
                    onClick = { }
                )
                Divider(color = Color.Gray.copy(alpha = 0.2f))
                ProfileMenuItem(
                    icon = Icons.Default.Logout,
                    title = "Keluar",
                    onClick = { showLogoutDialog = true },
                    isDestructive = true
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Version
        Text(
            text = "Lumina AI v1.0.0 (Kotlin)",
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
    
    // Logout dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Keluar") },
            text = { Text("Yakin ingin keluar dari aplikasi?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Keluar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Batal")
                }
            },
            containerColor = Color(0xFF1A1A1A)
        )
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isDestructive) Color.Red else LimeGreen
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                color = if (isDestructive) Color.Red else Color.White,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

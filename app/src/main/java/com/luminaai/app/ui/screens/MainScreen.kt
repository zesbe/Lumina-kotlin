package com.luminaai.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.luminaai.app.ui.theme.LimeGreen

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Home : BottomNavItem("home", "Beranda", Icons.Filled.Home, Icons.Outlined.Home)
    object Explore : BottomNavItem("explore", "Explore", Icons.Filled.Explore, Icons.Outlined.Explore)
    object Create : BottomNavItem("create", "Buat", Icons.Filled.Add, Icons.Outlined.Add)
    object Collection : BottomNavItem("collection", "Koleksi", Icons.Filled.LibraryMusic, Icons.Outlined.LibraryMusic)
    object Profile : BottomNavItem("profile", "Profil", Icons.Filled.Person, Icons.Outlined.Person)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onLogout: () -> Unit
) {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Explore,
        BottomNavItem.Create,
        BottomNavItem.Collection,
        BottomNavItem.Profile
    )
    
    val musicViewModel: MusicViewModel = hiltViewModel()
    
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF1A1A1A),
                contentColor = Color.White
            ) {
                items.forEachIndexed { index, item ->
                    val isSelected = selectedItem == index
                    
                    if (item == BottomNavItem.Create) {
                        // Special create button
                        NavigationBarItem(
                            icon = {
                                Surface(
                                    shape = MaterialTheme.shapes.medium,
                                    color = LimeGreen,
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Icon(
                                        imageVector = item.selectedIcon,
                                        contentDescription = item.title,
                                        tint = Color.Black,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            },
                            selected = isSelected,
                            onClick = { selectedItem = index },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.Transparent
                            )
                        )
                    } else {
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.title,
                                    tint = if (isSelected) LimeGreen else Color.Gray
                                )
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    color = if (isSelected) LimeGreen else Color.Gray
                                )
                            },
                            selected = isSelected,
                            onClick = { selectedItem = index },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedItem) {
                0 -> HomeScreen(musicViewModel)
                1 -> ExploreScreen(musicViewModel)
                2 -> CreateScreen(musicViewModel)
                3 -> CollectionScreen(musicViewModel)
                4 -> ProfileScreen(onLogout = onLogout)
            }
        }
    }
}

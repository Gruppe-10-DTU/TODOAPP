package com.gruppe11.todoApp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gruppe11.todoApp.ui.theme.TODOAPPTheme

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainApp()
        }
    }
}

@Composable
fun MainApp() {
    TODOAPPTheme {
        val navController = rememberNavController()
        val backStackEntry by navController.currentBackStackEntryAsState()

        var isMainDestination by rememberSaveable { mutableStateOf(true) }

        isMainDestination = when (backStackEntry?.destination?.route) {
            Task.route -> true
            Calendar.route -> true
            Settings.route -> true
            else -> false
        }

        Scaffold(
//            modifier = Modifier.background(MaterialTheme.colorScheme.secondary),
            bottomBar = {
                if (isMainDestination) {
                    BottomBar(navController = navController)
                }
            }
        ) { innerPadding ->
            MainNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun BottomBar(navController : NavHostController) {
    val screens = listOf(
        Calendar,
        Task,
        Settings
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.background,
    ){
        screens.forEach{ screen ->
            if (currentDestination != null) {
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem (
    screen: MainDestination,
    currentDestination: NavDestination,
    navController: NavHostController,
) {
    NavigationBarItem(
        label = {
            Text(
                text = screen.title,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        icon = {
            val textColor = if (currentDestination.route != screen.route) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.background

            Icon(
                imageVector = screen.icon,
                contentDescription = "Navigation icon",
                tint = textColor
            )
        },
        selected = currentDestination.hierarchy.any() {
            it.route == screen.route
        },
        onClick = {
            if (currentDestination.route != screen.route) {
                navController.navigate(screen.route)
            }
        }
    )
}
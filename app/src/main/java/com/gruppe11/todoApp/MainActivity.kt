package com.gruppe11.todoApp

import android.os.Bundle
import androidx.activity.compose.setContent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gruppe11.todoApp.ui.theme.TODOAPPTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

/*        val addTask: View = findViewById(R.id.floatingActionButton)
        addTask.setOnClickListener{
        }*/

        setContent {
            MainApp()
        }
    }

    private fun onAddButtonClicked() {

    }
}

@Composable
fun MainApp() {
    TODOAPPTheme {
        val navController = rememberNavController()

        Scaffold(
            bottomBar = {
                BottomBar(navController = navController)
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

    NavigationBar {
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
    navController: NavHostController
) {
    NavigationBarItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Navigation icon"
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
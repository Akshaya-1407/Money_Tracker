package com.example.trackermoney

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.trackermoney.feature.add_expense.AddExpense
import com.example.trackermoney.feature.home.HomeScreen
import com.example.trackermoney.feature.stats.StatsScreen
import com.example.trackermoney.ui.theme.Zinc

@Composable
fun NavHostScreen(){

    val navController = rememberNavController()
    var bottomBarVisibility by remember {
        mutableStateOf(true)
    }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(visible = bottomBarVisibility) {
                NavigationBottomBar(navController = navController,
                    items = listOf(
                        NavItem(route = "/home", icon = R.drawable.ic_home2),
                        NavItem(route = "/stats", icon = R.drawable.ic_stats2),
                    )
                )
            }
        }
    ) {
        NavHost(navController = navController,
            startDestination = "/home",
            modifier = androidx.compose.ui.Modifier.padding(it)
        ) {
            composable(route = "/home"){
                bottomBarVisibility = true
                HomeScreen(navController)
            }
            composable(route = "/addExpense"){
                bottomBarVisibility = false
                AddExpense(navController)
            }
            composable(route = "/stats"){
                bottomBarVisibility = true
                StatsScreen(navController)
            }
        }
    }


}

data class NavItem(
    val route: String,
    val icon : Int
)

@Composable
fun NavigationBottomBar(
    navController: NavController,
    items: List<NavItem>
){
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    BottomAppBar {
        items.forEach { item->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route){
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(painter = painterResource(id = item.icon), contentDescription = null)
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor = Zinc,
                    selectedIconColor = Zinc,
                    unselectedTextColor = Color.Gray,
                    unselectedIconColor = Color.Gray
                )
            )

        }
    }
}
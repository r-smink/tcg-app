package com.rick.tcgscanner.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rick.tcgscanner.R
import com.rick.tcgscanner.ui.navigation.Screen
import com.rick.tcgscanner.ui.screens.carddetail.CardDetailRoute
import com.rick.tcgscanner.ui.screens.dashboard.DashboardRoute
import com.rick.tcgscanner.ui.screens.portfolio.PortfolioRoute
import com.rick.tcgscanner.ui.screens.portfolio.PortfolioDetailRoute
import com.rick.tcgscanner.ui.screens.scanner.ScanRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomDestinations = listOf(
        Screen.Scan to Icons.Default.Camera to R.string.scan,
        Screen.Portfolio to Icons.Default.Collections to R.string.portfolio,
        Screen.Dashboard to Icons.Default.Dashboard to R.string.dashboard
    )

    val showBottomBar = currentDestination?.hierarchy?.any { dest ->
        bottomDestinations.any { it.first.first.route == dest.route }
    } == true

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomDestinations.forEach { (screenIcon, labelRes) ->
                        val (screen, icon) = screenIcon
                        val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                        NavigationBarItem(
                            icon = { Icon(icon, contentDescription = stringResource(labelRes)) },
                            label = { Text(stringResource(labelRes)) },
                            selected = selected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Scan.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Scan.route) { ScanRoute(navController) }
            composable(Screen.Portfolio.route) { PortfolioRoute(navController) }
            composable(Screen.Dashboard.route) { DashboardRoute() }
            composable(
                route = Screen.CardDetail.route,
                arguments = listOf(navArgument("cardId") { type = NavType.StringType })
            ) { backStackEntry ->
                val cardId = backStackEntry.arguments?.getString("cardId") ?: ""
                CardDetailRoute(
                    cardId = cardId,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(
                route = Screen.PortfolioDetail.route,
                arguments = listOf(navArgument("portfolioId") { type = NavType.IntType })
            ) { backStackEntry ->
                val portfolioId = backStackEntry.arguments?.getInt("portfolioId") ?: 0
                PortfolioDetailRoute(
                    portfolioId = portfolioId,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

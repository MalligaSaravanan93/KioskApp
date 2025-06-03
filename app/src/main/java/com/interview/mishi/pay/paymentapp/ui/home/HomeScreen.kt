package com.interview.mishi.pay.paymentapp.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.interview.mishi.pay.paymentapp.ui.cart.CartScreen
import com.interview.mishi.pay.paymentapp.ui.nav.Screen
import com.interview.mishi.pay.paymentapp.ui.orders.OrdersScreen
import com.interview.mishi.pay.paymentapp.ui.scan.ScanScreen
import com.interview.mishi.pay.paymentapp.ui.theme.AppTypography
import com.interview.mishi.pay.paymentapp.ui.theme.PaymentAppTheme
import java.util.Locale

@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    Scaffold (
        topBar = {
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
            AppTopBar (
                title = currentRoute?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                    ?: "PaymentApp",
                showBackButton = false, // Change logic if needed
                onBackPress = { navController.popBackStack() }
            )
        },
        bottomBar = { BottomNavBar(navController) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Scan.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Scan.route) { ScanScreen() }
            composable(Screen.Cart.route) { CartScreen() }
            composable(Screen.Orders.route) { OrdersScreen() }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    val screens = listOf(Screen.Scan, Screen.Cart, Screen.Orders)
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        screens.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = { navController.navigate(screen.route) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(title: String, showBackButton: Boolean, onBackPress: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text(title, color = MaterialTheme.colorScheme.background, style = AppTypography.titleLarge) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackPress) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PaymentAppTheme {
        HomeScreen()
    }
}
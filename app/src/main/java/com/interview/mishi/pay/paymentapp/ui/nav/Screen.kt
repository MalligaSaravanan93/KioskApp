package com.interview.mishi.pay.paymentapp.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector, val title: String) {
    data object Scan : Screen("scan", Icons.Filled.Search, "Scan")
    data object Cart : Screen("cart", Icons.Filled.ShoppingCart, "Cart")
    data object Orders : Screen("orders", Icons.AutoMirrored.Filled.List, "Orders")
}
package com.interview.mishi.pay.paymentapp.ui.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.interview.mishi.pay.paymentapp.R
import com.interview.mishi.pay.paymentapp.domain.model.CartItem
import com.interview.mishi.pay.paymentapp.ui.components.CardLayout
import com.interview.mishi.pay.paymentapp.ui.components.LabelHeading
import com.interview.mishi.pay.paymentapp.ui.components.NetworkImage
import com.interview.mishi.pay.paymentapp.ui.components.ProgressDialog
import com.interview.mishi.pay.paymentapp.ui.components.SnackBarLayout
import com.interview.mishi.pay.paymentapp.ui.components.SummaryLayout
import com.interview.mishi.pay.paymentapp.ui.theme.AppTypography
import com.interview.mishi.pay.paymentapp.ui.theme.getColors
import com.interview.mishi.pay.paymentapp.utils.UIState
import com.interview.mishi.pay.paymentapp.utils.toAmount
import com.interview.mishi.pay.paymentapp.utils.value

@Composable
fun CartScreen(viewModel: CartViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.getCartItems()
    }
    when (val result = viewModel.cartItems.collectAsState().value) {
        is UIState.Success -> {
            CartView(result.data, viewModel)
        }

        is UIState.Loading -> {
            ProgressDialog()
        }

        is UIState.Error -> {
            SnackBarLayout(result.error, getColors().error, getColors().errorContainer)
            viewModel.resetCartItems()
        }

        else -> {}
    }
    when (val result = viewModel.updateCartItem.collectAsState().value) {
        is UIState.Loading -> {
            ProgressDialog()
        }

        is UIState.Error -> {
            SnackBarLayout(result.error, getColors().error, getColors().errorContainer)
            viewModel.resetUpdateItem()
        }

        else -> {}
    }
}

@Composable
fun CartView(cartItems: List<CartItem>, viewModel: CartViewModel) {
    Column(
        Modifier
            .fillMaxHeight()
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn(modifier = Modifier.weight(1f, false)) {
            items(
                count = cartItems.size,
                key = { index -> cartItems[index].id.value() }
            ) { index ->
                CartItemView(cartItems[index], viewModel)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Column {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Cost Summary", style = AppTypography.titleLarge)
            SummaryLayout("Subtotal", viewModel.subTotal.doubleValue.toAmount())
            SummaryLayout("Shipping", viewModel.shipping.doubleValue.toAmount())
            SummaryLayout("Estimated Tax", viewModel.tax.doubleValue.toAmount())
            Spacer(modifier = Modifier.height(6.dp))
            SummaryLayout("Total", viewModel.total.doubleValue.toAmount())
            Button(
                onClick = {
                    viewModel.createOrder(cartItems)
                },
                enabled = cartItems.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                LabelHeading("Checkout", color = getColors().background)
            }
        }
    }
}

@Composable
fun CartItemView(item: CartItem, viewModel: CartViewModel) {
    CardLayout {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            NetworkImage(
                item.image.value(),
                contentDescription = item.name.value(),
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
            ) {
                LabelHeading(
                    item.name.value(),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f)
                )
                Row(
                    modifier = Modifier.wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LabelHeading("£${item.price}", modifier = Modifier.weight(1f))
                    // Add & Minus Buttons
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                if (item.quantity.value() > 0) {
                                    viewModel.updateQuantity(item.copy(quantity = item.quantity.value() - 1))
                                }
                            },
                            enabled = item.quantity.value() > 0
                        ) {
                            Icon(
                                ImageVector.vectorResource(R.drawable.ic_minus_circle),
                                contentDescription = "Remove"
                            )
                        }

                        LabelHeading(
                            text = item.quantity.value().toString(),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        IconButton(
                            onClick = {
                                viewModel.updateQuantity(item.copy(quantity = item.quantity.value() + 1))
                            }
                        ) {
                            Icon(Icons.Filled.AddCircle, contentDescription = "Add")
                        }
                    }
                }
            }
        }
    }
}

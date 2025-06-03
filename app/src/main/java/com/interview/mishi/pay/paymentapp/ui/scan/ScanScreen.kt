package com.interview.mishi.pay.paymentapp.ui.scan

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.interview.mishi.pay.paymentapp.R
import com.interview.mishi.pay.paymentapp.ui.components.CardLayout
import com.interview.mishi.pay.paymentapp.ui.components.LabelContent
import com.interview.mishi.pay.paymentapp.ui.components.LabelHeading
import com.interview.mishi.pay.paymentapp.ui.components.NetworkImage
import com.interview.mishi.pay.paymentapp.ui.components.ProgressDialog
import com.interview.mishi.pay.paymentapp.ui.components.SnackBarLayout
import com.interview.mishi.pay.paymentapp.ui.theme.PaymentAppTheme
import com.interview.mishi.pay.paymentapp.ui.theme.getColors
import com.interview.mishi.pay.paymentapp.utils.UIState
import com.interview.mishi.pay.paymentapp.utils.showMessage
import com.interview.mishi.pay.paymentapp.utils.value

@Composable
fun ScanScreen(viewModel: ScanViewModel = hiltViewModel()) {
    val scannedProduct = remember { viewModel.scannedProduct }

    when (val result = viewModel.addCartItem.collectAsState().value) {
        is UIState.Success -> {
            showMessage(LocalContext.current, R.string.product_added_to_cart_successfully)
        }

        is UIState.Loading -> {
            ProgressDialog()
        }

        is UIState.Error -> {
            SnackBarLayout(result.error, getColors().error, getColors().errorContainer)
            viewModel.reset()
        }

        else -> {}
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = {
                viewModel.scan()
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            LabelHeading("Scan Product", color = getColors().background)
            Spacer(Modifier.width(8.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_qrcode),
                contentDescription = "Scan Product",
                modifier = Modifier
                    .height(20.dp)
                    .width(20.dp)
            )
        }
        Spacer(Modifier.height(16.dp))
        if (scannedProduct.value != null) {
            scannedProduct.value?.let { product ->
                CardLayout {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        NetworkImage(
                            product.image.value(),
                            contentDescription = product.name,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(Modifier.height(16.dp))
                        LabelHeading(
                            product.name.value(),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        LabelContent(
                            product.desc.value(),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LabelHeading("£${product.price}", modifier = Modifier.weight(1f))
                            // Add & Minus Buttons
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = {
                                        //if (product.quantity > 0) product.quantity--
                                        viewModel.decreaseQuantity()
                                    },
                                    enabled = product.quantity.value() > 0
                                ) {
                                    Icon(
                                        ImageVector.vectorResource(R.drawable.ic_minus_circle),
                                        contentDescription = "Remove"
                                    )
                                }

                                LabelHeading(
                                    text = product.quantity.toString(),
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )

                                IconButton(
                                    onClick = {
                                        //product.quantity++
                                        viewModel.addQuantity()
                                    }
                                ) {
                                    Icon(Icons.Filled.AddCircle, contentDescription = "Add")
                                }
                            }
                        }
                        Button(
                            onClick = {
                                viewModel.addCartItem(product)
                            },
                            enabled = product.quantity.value() > 0,
                            modifier = Modifier.wrapContentWidth(),
                        ) {
                            LabelHeading("Add Product", color = getColors().background)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PaymentAppTheme {
        ScanScreen()
    }
}
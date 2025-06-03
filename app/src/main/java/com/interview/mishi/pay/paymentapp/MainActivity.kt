package com.interview.mishi.pay.paymentapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.interview.mishi.pay.paymentapp.ui.home.HomeScreen
import com.interview.mishi.pay.paymentapp.ui.theme.PaymentAppTheme
import com.interview.mishi.pay.scanner.QRScanner
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QRScanner.init(this)
        setContent {
            PaymentAppTheme {
                HomeScreen()
            }
        }
    }
}
package com.interview.mishi.pay.paymentapp.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.interview.mishi.pay.paymentapp.ui.theme.AppTypography

@Composable
fun SummaryLayout(title: String, value: String) {
    Row {
        Text(
            text = title,
            style = AppTypography.titleMedium,
            textAlign = TextAlign.Right,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "£${value}",
            style = AppTypography.titleMedium,
            textAlign = TextAlign.Right,
            modifier = Modifier.width(100.dp)
        )
    }
}

@Composable
fun OrderSummaryLayout(title: String, value: String) {
    Row {
        Text(
            text = title,
            style = AppTypography.labelSmall,
            textAlign = TextAlign.Right,
            color = Color.Gray,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "£${value}",
            style = AppTypography.labelSmall,
            textAlign = TextAlign.Right,
            color = Color.Gray,
            modifier = Modifier.width(100.dp)
        )
    }
}
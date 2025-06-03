package com.interview.mishi.pay.paymentapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.interview.mishi.pay.paymentapp.ui.theme.getColors

@Composable
fun CardLayout(onClick: (()->Unit)? = null,content: @Composable () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = getColors().background,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        shape = RoundedCornerShape(5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable (enabled = onClick != null) {
                onClick?.invoke()
            }
    ) {
        Column(modifier = Modifier
            .padding(12.dp)
            .wrapContentHeight()) {
            content.invoke()
        }
    }
}
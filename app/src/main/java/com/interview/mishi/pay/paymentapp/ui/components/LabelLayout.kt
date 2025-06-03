package com.interview.mishi.pay.paymentapp.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.interview.mishi.pay.paymentapp.ui.theme.AppTypography
import com.interview.mishi.pay.paymentapp.ui.theme.getColors

@SuppressLint("ModifierParameter")
@Composable
fun LabelHeading(
    text: String = "",
    textAlign: TextAlign = TextAlign.Start,
    color: Color = getColors().onBackground,
    modifier: Modifier = Modifier
) = Text(
    text,
    style = AppTypography.titleMedium,
    modifier = modifier.padding(bottom = 4.dp),
    color = color,
    fontWeight = FontWeight.Bold,
    textAlign = textAlign
)

@SuppressLint("ModifierParameter")
@Composable
fun LabelContent(
    text: String = "",
    textAlign: TextAlign = TextAlign.Start,
    color: Color = getColors().onBackground,
    modifier: Modifier = Modifier
) = Text(
    text,
    style = AppTypography.labelSmall,
    modifier = modifier,
    color = color,
    fontWeight = FontWeight.Normal,
    textAlign = textAlign
)
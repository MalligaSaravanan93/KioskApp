package com.interview.mishi.pay.paymentapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.interview.mishi.pay.paymentapp.R

@Composable
fun NetworkImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .error(R.drawable.ic_image_not_available) // Add your error drawable here
            .placeholder(R.drawable.ic_image_loading) // Optional loading placeholder
            .build()
    )

    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier
    )
}
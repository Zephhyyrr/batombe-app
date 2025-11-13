package com.firman.rima.batombe.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firman.rima.batombe.data.remote.models.FeedByIdResponse
import com.firman.rima.batombe.R
import com.firman.rima.batombe.ui.theme.*

@Composable
fun FeedDetailCard(
    data: FeedByIdResponse.FeedDetail,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = batombeGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.pantun_text_title),
                fontSize = 14.sp,
                fontFamily = PoppinsSemiBold,
                color = textColor,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = data.pantunBatombe ?: "Teks tidak tersedia.",
                fontSize = 16.sp,
                fontFamily = PoppinsRegular,
                color = textColor,
                lineHeight = 24.sp
            )
        }
    }
}
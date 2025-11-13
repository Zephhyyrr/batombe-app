package com.firman.rima.batombe.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.firman.rima.batombe.data.remote.models.GetCommentsResponse
import com.firman.rima.batombe.ui.theme.*
import com.firman.rima.batombe.R
import com.firman.rima.batombe.utils.MediaUrlUtils

@Composable
fun CommentItem(
    comment: GetCommentsResponse.CommentData,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(MediaUrlUtils.buildMediaUrl(comment.user?.profileImage))
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.unknownperson),
            error = painterResource(R.drawable.unknownperson),
            contentDescription = "Commenter Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = comment.user?.name ?: "Unknown User",
                    fontFamily = PoppinsSemiBold,
                    fontSize = 13.sp,
                    color = textColor
                )

                if (comment.user?.isDatuak == true) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(R.drawable.ic_verified),
                        contentDescription = "Akun Datuak Terverifikasi",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            comment.content?.let {
                Text(
                    text = it,
                    fontFamily = PoppinsRegular,
                    fontSize = 12.sp,
                    color = textColor.copy(alpha = 0.8f),
                    lineHeight = 18.sp
                )
            }
        }
    }
}
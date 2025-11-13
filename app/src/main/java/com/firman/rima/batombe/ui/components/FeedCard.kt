package com.firman.rima.batombe.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.firman.rima.batombe.data.remote.models.FeedResponse
import com.firman.rima.batombe.ui.theme.*
import com.firman.rima.batombe.utils.MediaUrlUtils
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonState
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonType
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSJetPackComposeProgressButton
import com.firman.rima.batombe.R

@Composable
fun FeedCard(
    modifier: Modifier = Modifier,
    feedItem: FeedResponse.FeedItem,
    // Hapus 'feedLike' dari parameter
    buttonState: SSButtonState,
    onPlayClick: () -> Unit,
    onItemClick: (Int) -> Unit,
    onLikeClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                feedItem.id?.let { id ->
                    onItemClick(id)
                }
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = batombeGray)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(MediaUrlUtils.buildMediaUrl(feedItem.user?.profileImage))
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.unknownperson),
                    error = painterResource(R.drawable.unknownperson),
                    contentDescription = "User Profile Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = feedItem.user?.name ?: "Unknown User",
                    fontFamily = PoppinsSemiBold,
                    fontSize = 14.sp,
                    color = textColor
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = feedItem.pantunBatombe ?: "",
                fontFamily = PoppinsSemiBold,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = textColor.copy(alpha = 0.8f),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                SSJetPackComposeProgressButton(
                    type = SSButtonType.CIRCLE,
                    width = 323.dp,
                    height = 45.dp,
                    buttonState = buttonState,
                    onClick = onPlayClick,
                    cornerRadius = 100,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = batombePrimary,
                        contentColor = Color.White,
                        disabledContainerColor = batombePrimary,
                        disabledContentColor = Color.White
                    ),
                    assetColor = Color.White,
                    text = when (buttonState) {
                        SSButtonState.LOADING -> stringResource(R.string.loading_audio)
                        SSButtonState.SUCCESS -> stringResource(R.string.pause_audio)
                        SSButtonState.FAILURE -> stringResource(R.string.failed_play_audio)
                        else -> stringResource(R.string.play_audio)
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(onClick = onLikeClick)
                ) {
                    val isLiked = feedItem.isLiked ?: false
                    val likeCount = feedItem.like ?: 0

                    val likeIcon =
                        if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
                    val likeColor = if (isLiked) batombePrimary else batombePrimary

                    Icon(
                        imageVector = likeIcon,
                        contentDescription = "Likes",
                        tint = likeColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$likeCount Suka",
                        fontFamily = PoppinsMedium,
                        color = likeColor,
                        fontSize = 12.sp,
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val commentCount = feedItem.count?.comments ?: 0

                    Icon(
                        painter = painterResource(R.drawable.ic_comment),
                        contentDescription = "Comments",
                        tint = batombePrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$commentCount Komentar",
                        fontFamily = PoppinsMedium,
                        color = batombePrimary,
                        fontSize = 12.sp,
                    )
                }
            }
        }
    }
}
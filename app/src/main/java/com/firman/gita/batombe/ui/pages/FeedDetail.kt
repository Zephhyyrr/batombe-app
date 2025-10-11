package com.firman.gita.batombe.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.airbnb.lottie.compose.*
import com.firman.gita.batombe.R
import com.firman.gita.batombe.ui.components.CommentInput
import com.firman.gita.batombe.ui.components.CommentItem
import com.firman.gita.batombe.ui.components.FeedDetailCard
import com.firman.gita.batombe.ui.theme.*
import com.firman.gita.batombe.ui.viewmodel.FeedViewModel
import com.firman.gita.batombe.utils.MediaUrlUtils
import com.firman.gita.batombe.utils.ResultState
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonState
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonType
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSJetPackComposeProgressButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedDetailScreen(
    feedId: Int,
    navController: NavController,
    viewModel: FeedViewModel = hiltViewModel()
) {
    val feedDetailState by viewModel.feedDetailState.collectAsState()
    val commentsState by viewModel.commentsState.collectAsState()
    val postCommentState by viewModel.postCommentState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val screenState = rememberFeedScreenState()

    LaunchedEffect(feedId) {
        viewModel.getFeedById(feedId)
        viewModel.getComments(feedId)
    }

    LaunchedEffect(postCommentState) {
        when (val result = postCommentState) {
            is ResultState.Success -> {
                snackbarHostState.showSnackbar("Komentar berhasil dikirim!")
                viewModel.resetPostCommentState()
            }

            is ResultState.Error -> {
                snackbarHostState.showSnackbar("Gagal mengirim komentar: ${result.errorMessage}")
                viewModel.resetPostCommentState()
            }

            else -> {}
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearFeedDetail()
            screenState.cleanup()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Detail Postingan",
                        fontSize = 14.sp,
                        fontFamily = PoppinsSemiBold,
                        color = whiteColor
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = whiteColor
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = batombePrimary)
            )
        },
        containerColor = batombeSecondary
    ) { innerPadding ->
        when (val state = feedDetailState) {
            is ResultState.Loading, is ResultState.Initial -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_animation))
                    LottieAnimation(
                        composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier.size(150.dp)
                    )
                }
            }

            is ResultState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(state.errorMessage)
                }
            }

            is ResultState.Success -> {
                val feedData = state.data
                if (feedData != null) {
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)) {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // 1. Header Info User
                            item {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(MediaUrlUtils.buildMediaUrl(feedData.user?.profileImage))
                                            .crossfade(true).build(),
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
                                        text = feedData.user?.name ?: "Unknown User",
                                        fontFamily = PoppinsSemiBold,
                                        fontSize = 14.sp,
                                        color = textColor
                                    )
                                }
                            }

                            item {
                                val buttonState = screenState.audioButtonState
                                SSJetPackComposeProgressButton(
                                    type = SSButtonType.CIRCLE,
                                    width = 400.dp,
                                    height = 45.dp,
                                    buttonState = buttonState,
                                    onClick = {
                                        screenState.handlePlayPause(
                                            feedId = feedData.id ?: -1,
                                            audioPath = feedData.fileAudio.orEmpty()
                                        )
                                    },
                                    cornerRadius = 100,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = batombePrimary,
                                        contentColor = Color.White
                                    ),
                                    assetColor = Color.White,
                                    text = when (buttonState) {
                                        SSButtonState.LOADING -> "Memuat Audio..."
                                        SSButtonState.SUCCESS -> "Jeda Audio"
                                        SSButtonState.FAILURE -> "Gagal Memutar"
                                        else -> "Putar Audio"
                                    }
                                )
                            }

                            item {
                                FeedDetailCard(data = feedData)
                            }

                            item {
                                when (val commentsResult = commentsState) {
                                    is ResultState.Success -> {
                                        Text(
                                            "Komentar (${commentsResult.data.size})",
                                            fontFamily = PoppinsSemiBold,
                                            fontSize = 14.sp,
                                            color = textColor
                                        )
                                    }

                                    else -> {}
                                }
                            }

                            when (val commentsResult = commentsState) {
                                is ResultState.Success -> {
                                    if (commentsResult.data.isEmpty()) {
                                        item {
                                            Text(
                                                "Jadilah yang pertama berkomentar!",
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 24.dp),
                                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                                fontFamily = PoppinsRegular,
                                                color = textColor.copy(alpha = 0.7f)
                                            )
                                        }
                                    } else {
                                        items(
                                            commentsResult.data,
                                            key = { it.id ?: 0 }) { comment ->
                                            CommentItem(comment = comment)
                                        }
                                    }
                                }

                                else -> {}
                            }
                        }
                        CommentInput(
                            onSendClick = { commentText ->
                                viewModel.postComment(feedId, commentText)
                            }
                        )
                    }
                }
            }
        }
    }
}
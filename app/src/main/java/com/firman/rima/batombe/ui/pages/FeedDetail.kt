package com.firman.rima.batombe.ui.pages

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.airbnb.lottie.compose.*
import com.firman.rima.batombe.ui.components.CommentInput
import com.firman.rima.batombe.ui.components.CommentItem
import com.firman.rima.batombe.ui.components.FeedDetailCard
import com.firman.rima.batombe.ui.theme.*
import com.firman.rima.batombe.ui.viewmodel.FeedViewModel
import com.firman.rima.batombe.utils.MediaUrlUtils
import com.firman.rima.batombe.utils.ResultState
import com.firman.rima.batombe.utils.rememberFeedScreenState
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonState
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonType
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSJetPackComposeProgressButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import com.firman.rima.batombe.R
import java.net.URL

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
    val context = LocalContext.current
    val screenState = rememberFeedScreenState()
    var isSharing by remember { mutableStateOf(false) }

    LaunchedEffect(feedId) {
        viewModel.getFeedById(feedId)
        viewModel.getComments(feedId)
    }

    LaunchedEffect(postCommentState) {
        when (val result = postCommentState) {
            is ResultState.Success -> {
                snackbarHostState.showSnackbar("Komentar berhasil dikirim!")
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("comment_updated", true)
                navController.popBackStack()
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
                actions = {
                    if (feedDetailState is ResultState.Success) {
                        val feedData = (feedDetailState as ResultState.Success).data
                        if (feedData != null) {
                            IconButton(
                                enabled = !isSharing,
                                onClick = {
                                    scope.launch {
                                        isSharing = true
                                        try {
                                            val sharedFile = withContext(Dispatchers.IO) {
                                                val audioUrl =
                                                    MediaUrlUtils.buildMediaUrl(feedData.fileAudio.orEmpty())
                                                val tempFile =
                                                    File(context.cacheDir, "shared_audio.mp3")
                                                URL(audioUrl).openStream().use { input ->
                                                    FileOutputStream(tempFile).use { output ->
                                                        input.copyTo(output)
                                                    }
                                                }
                                                tempFile
                                            }
                                            val authority = "${context.packageName}.provider"
                                            val audioUri = FileProvider.getUriForFile(
                                                context,
                                                authority,
                                                sharedFile
                                            )
                                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                                type = "audio/mp3"
                                                putExtra(Intent.EXTRA_STREAM, audioUri)
                                                putExtra(
                                                    Intent.EXTRA_TEXT,
                                                    "Dengarkan Pantun Batombe ini:\n\n${feedData.pantunBatombe}"
                                                )
                                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                            }
                                            context.startActivity(
                                                Intent.createChooser(
                                                    shareIntent,
                                                    "Bagikan audio via..."
                                                )
                                            )
                                        } catch (e: Exception) {
                                            Log.e("FeedDetailScreen", "Share failed", e)
                                            snackbarHostState.showSnackbar("Gagal membagikan audio")
                                        } finally {
                                            isSharing = false
                                        }
                                    }
                                }
                            ) {
                                if (isSharing) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = whiteColor,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_share),
                                        contentDescription = "Share",
                                        tint = whiteColor
                                    )
                                }
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = batombePrimary)
            )
        },
        containerColor = batombeSecondary
    ) { innerPadding ->
        when (val state = feedDetailState) {
            is ResultState.Loading, is ResultState.Initial -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_animation))
                    LottieAnimation(
                        composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier.size(150.dp)
                    )
                }
            }

            is ResultState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(state.errorMessage)
                }
            }

            is ResultState.Success -> {
                val feedData = state.data
                if (feedData != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
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
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {

                                    SSJetPackComposeProgressButton(
                                        type = SSButtonType.CIRCLE,
                                        width = 400.dp,
                                        height = 45.dp,
                                        buttonState = screenState.audioButtonState,
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
                                        text = when (screenState.audioButtonState) {
                                            SSButtonState.LOADING -> "Memuat Audio..."
                                            SSButtonState.SUCCESS -> "⏸️ Jeda Audio"
                                            SSButtonState.FAILURE -> "Gagal Memutar"
                                            else -> "Putar Audio"
                                        }
                                    )
                                }
                            }
                            item { FeedDetailCard(data = feedData) }
                            item {
                                val isLiked = feedData.isLiked ?: false
                                val likeCount = feedData.like ?: 0

                                val likeIcon =
                                    if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
                                val likeColor = if (isLiked) batombePrimary else batombePrimary

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clickable {
                                            feedData.id?.let { id ->
                                                viewModel.likeFeed(id)
                                            }
                                        }
                                ) {
                                    Icon(
                                        imageVector = likeIcon,
                                        contentDescription = "Like",
                                        tint = likeColor,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "$likeCount Suka",
                                        fontFamily = PoppinsMedium,
                                        color = likeColor,
                                        fontSize = 14.sp,
                                    )
                                }
                            }

                            when (val commentsResult = commentsState) {
                                is ResultState.Success -> {
                                    item {
                                        Text(
                                            "Komentar (${commentsResult.data.size})",
                                            fontFamily = PoppinsSemiBold,
                                            fontSize = 14.sp,
                                            color = textColor,
                                            modifier = Modifier.padding(top = 8.dp)
                                        )
                                    }
                                    if (commentsResult.data.isEmpty()) {
                                        item {
                                            Text(
                                                "Jadilah yang pertama berkomentar!",
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 24.dp),
                                                textAlign = TextAlign.Center,
                                                fontFamily = PoppinsRegular,
                                                color = textColor.copy(alpha = 0.7f)
                                            )
                                        }
                                    } else {
                                        items(
                                            commentsResult.data,
                                            key = {
                                                it.id ?: 0
                                            }) { comment -> CommentItem(comment = comment) }
                                    }
                                }

                                is ResultState.Loading -> {
                                    item {
                                        val composition by rememberLottieComposition(
                                            LottieCompositionSpec.RawRes(R.raw.loading_animation)
                                        )
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            LottieAnimation(
                                                composition = composition,
                                                iterations = LottieConstants.IterateForever,
                                                modifier = Modifier
                                                    .size(150.dp)
                                                    .align(Alignment.Center)
                                            )
                                        }
                                    }
                                }

                                is ResultState.Error -> {
                                    item {
                                        Text(
                                            "Gagal memuat komentar: ${commentsResult.errorMessage}",
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }

                                is ResultState.Initial -> {}
                            }
                        }
                        CommentInput(
                            onSendClick = { commentText ->
                                viewModel.postComment(
                                    feedId,
                                    commentText
                                )
                            }
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) { Text("Detail postingan tidak ditemukan.") }
                }
            }
        }
    }
}
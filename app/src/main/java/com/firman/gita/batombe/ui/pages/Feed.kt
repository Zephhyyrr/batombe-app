package com.firman.gita.batombe.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.*
import com.firman.gita.batombe.R
import com.firman.gita.batombe.ui.components.FeedCard
import com.firman.gita.batombe.ui.navigation.Screen
import com.firman.gita.batombe.ui.theme.*
import com.firman.gita.batombe.ui.viewmodel.FeedViewModel
import com.firman.gita.batombe.utils.ResultState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: FeedViewModel = hiltViewModel(),
    onCommentClick: (Int) -> Unit
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = batombePrimary, darkIcons = false)
    }

    val feedsState by viewModel.feedsState.collectAsState()
    val screenState = rememberFeedScreenState()
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val shouldRefresh = savedStateHandle?.getLiveData<Boolean>("comment_updated")?.observeAsState()

    LaunchedEffect(shouldRefresh?.value) {
        if (shouldRefresh?.value == true) {
            viewModel.getFeeds()

            savedStateHandle.remove<Boolean>("comment_updated")
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            screenState.cleanup()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.menu_feed_title),
                        fontSize = 14.sp,
                        fontFamily = PoppinsSemiBold,
                        color = whiteColor
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = batombePrimary)
            )
        },
        modifier = modifier,
        containerColor = batombeSecondary
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = feedsState) {
                is ResultState.Initial, is ResultState.Loading -> {
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_animation))
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier
                            .size(150.dp)
                            .align(Alignment.Center)
                    )
                }

                is ResultState.Success -> {
                    val feedList = state.data
                    if (feedList.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(15.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                val composition by rememberLottieComposition(
                                    LottieCompositionSpec.RawRes(
                                        R.raw.nodata_animation
                                    )
                                )
                                LottieAnimation(
                                    modifier = Modifier.size(400.dp),
                                    composition = composition,
                                    iterations = LottieConstants.IterateForever
                                )
                                Text(
                                    text = "Belum ada feed yang tersedia",
                                    style = TextStyle(
                                        color = textColor,
                                        fontSize = 16.sp,
                                        fontFamily = PoppinsSemiBold
                                    )
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(items = feedList, key = { it.id ?: 0 }) { feedItem ->
                                val buttonState =
                                    if (screenState.currentlyPlayingId == feedItem.id) {
                                        screenState.audioButtonState
                                    } else {
                                        SSButtonState.IDLE
                                    }

                                FeedCard(
                                    feedItem = feedItem,
                                    buttonState = buttonState,
                                    onPlayClick = {
                                        screenState.handlePlayPause(
                                            feedId = feedItem.id ?: -1,
                                            audioPath = feedItem.fileAudio.orEmpty()
                                        )
                                    },
                                    onItemClick = { feedId ->
                                        navController.navigate(Screen.FeedDetail.createRoute(feedId))
                                    }
                                )
                            }
                        }
                    }
                }

                is ResultState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = stringResource(R.string.error_title),
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = state.errorMessage,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.getFeeds() },
                                colors = ButtonDefaults.buttonColors(containerColor = batombePrimary)
                            ) {
                                Text(text = stringResource(R.string.retry), color = whiteColor)
                            }
                        }
                    }
                }
            }
        }
    }
}
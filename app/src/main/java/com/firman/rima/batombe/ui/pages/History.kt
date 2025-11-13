package com.firman.rima.batombe.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.firman.rima.batombe.data.remote.models.HistoryResponse
import com.firman.rima.batombe.ui.components.HistoryCard
import com.firman.rima.batombe.ui.theme.PoppinsSemiBold
import com.firman.rima.batombe.ui.theme.batombePrimary
import com.firman.rima.batombe.ui.theme.batombeSecondary
import com.firman.rima.batombe.ui.theme.textColor
import com.firman.rima.batombe.ui.theme.whiteColor
import com.firman.rima.batombe.ui.viewmodel.HistoryViewModel
import com.firman.rima.batombe.utils.ResultState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.firman.rima.batombe.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel(),
    onHistoryItemClick: (HistoryResponse.Data) -> Unit = {}
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = batombePrimary,
            darkIcons = false
        )
    }

    val historyListState by viewModel.historyListState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getAllHistory()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.menu_history_title),
                        fontSize = 14.sp,
                        fontFamily = PoppinsSemiBold,
                        color = whiteColor
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = batombePrimary
                )
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
            when (val state = historyListState) {
                is ResultState.Initial, is ResultState.Loading -> {
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_animation))
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(150.dp)
                            .align(Alignment.Center)
                    )
                }

                is ResultState.Success -> {
                    val historyList = state.data
                    if (historyList.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(15.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                val composition by rememberLottieComposition(
                                    LottieCompositionSpec.RawRes(
                                        R.raw.nodata_animation
                                    )
                                )
                                LottieAnimation(
                                    modifier = Modifier.size(400.dp),
                                    composition = composition,
                                    iterations = LottieConstants.IterateForever,
                                    contentScale = ContentScale.Fit
                                )
                                Text(
                                    text = stringResource(R.string.nodata_history_title),
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
                            contentPadding = PaddingValues(
                                start = 15.dp,
                                end = 15.dp,
                                top = 16.dp,
                                bottom = 120.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                items = historyList,
                                key = { it.id ?: 0 }
                            ) { historyItem ->
                                HistoryCard(
                                    historyItem = historyItem,
                                    onClick = { onHistoryItemClick(historyItem) }
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
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
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
                                onClick = { viewModel.getAllHistory() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = batombePrimary
                                )
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
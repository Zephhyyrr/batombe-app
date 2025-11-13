package com.firman.rima.batombe.ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.firman.rima.batombe.ui.theme.*
import com.firman.rima.batombe.ui.viewmodel.OnBoardingViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonState
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonType
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSJetPackComposeProgressButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.firman.rima.batombe.R

data class OnBoardModel(
    val imageRes: Int,
    val titleRes: Int,
    val descriptionRes: Int
)

@Composable
fun getOnBoardModel(): List<OnBoardModel> {
    return listOf(

        OnBoardModel(
            titleRes = R.string.batombe_title_2,
            descriptionRes = R.string.batombe_desc_2,
            imageRes = R.drawable.iv_batombe_ilus_2
        ),

        OnBoardModel(
            titleRes = R.string.batombe_title_3,
            descriptionRes = R.string.batombe_desc_3,
            imageRes = R.drawable.iv_batombe_ilus_3
        ),

        OnBoardModel(
            titleRes = R.string.batombe_title_1,
            descriptionRes = R.string.batombe_desc_1,
            imageRes = R.drawable.iv_batombe_ilus_1
        )
    )
}

@Composable
fun OnBoardItem(page: OnBoardModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = page.imageRes),
            contentDescription = stringResource(R.string.onboarding_image_desc),
            modifier = Modifier
                .height(350.dp)
                .width(350.dp)
        )
        Text(
            text = stringResource(id = page.titleRes),
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 19.dp),
            style = TextStyle(
                fontFamily = PoppinsSemiBold,
                fontSize = 20.sp,
                color = textColor,
                textAlign = TextAlign.Center,
            )
        )
        Text(
            text = stringResource(id = page.descriptionRes),
            modifier = Modifier.padding(horizontal = 15.dp),
            style = TextStyle(
                fontFamily = PoppinsRegular,
                fontSize = 12.sp,
                color = textColor,
                textAlign = TextAlign.Center,
            )
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ButtonSection(
    pagerState: PagerState,
    onFinishOnboarding: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var buttonState by remember { mutableStateOf(SSButtonState.IDLE) }
    val onBoardModels = getOnBoardModel()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        if (pagerState.currentPage == onBoardModels.size - 1) {
            SSJetPackComposeProgressButton(
                type = SSButtonType.CIRCLE,
                width = 380.dp,
                height = 50.dp,
                buttonState = buttonState,
                onClick = {
                    coroutineScope.launch {
                        buttonState = SSButtonState.LOADING
                        delay(1000)
                        buttonState = SSButtonState.SUCCESS
                        delay(500)
                        onFinishOnboarding()
                    }
                },
                cornerRadius = 100,
                assetColor = whiteColor,
                colors = buttonColors(
                    containerColor = batombePrimary,
                    contentColor = Color.White,
                    disabledContainerColor = batombePrimary,
                    disabledContentColor = Color.White
                ),
                text = stringResource(R.string.button_start_now),
                fontSize = 14.sp,
                fontFamily = PoppinsSemiBold
            )
        } else {
            SSJetPackComposeProgressButton(
                type = SSButtonType.CIRCLE,
                width = 380.dp,
                height = 50.dp,
                buttonState = buttonState,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                },
                cornerRadius = 100,
                assetColor = whiteColor,
                colors = buttonColors(
                    containerColor = batombePrimary,
                    contentColor = Color.White,
                    disabledContainerColor = batombePrimary,
                    disabledContentColor = Color.White
                ),
                text = stringResource(R.string.button_next),
                fontSize = 14.sp,
                fontFamily = PoppinsSemiBold
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardingScreen(
    navController: NavController,
    onFinishOnboarding: () -> Unit,
    viewModel: OnBoardingViewModel = hiltViewModel()
) {
    val onBoardModels = getOnBoardModel()
    val pagerState = rememberPagerState()

    fun completeOnboarding() {
        viewModel.completeOnBoarding()
        onFinishOnboarding()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = batombeSecondary
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(batombeSecondary),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                count = onBoardModels.size,
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { pageIndex ->
                OnBoardItem(page = onBoardModels[pageIndex])
            }

            Row(
                modifier = Modifier.padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(onBoardModels.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .width(if (isSelected) 24.dp else 12.dp)
                            .height(12.dp)
                            .background(
                                color = if (isSelected) batombePrimary else Color.Gray,
                                shape = CircleShape
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected) batombePrimary else Color.Gray,
                                shape = CircleShape
                            )
                    )
                }
            }

            ButtonSection(
                pagerState = pagerState,
                onFinishOnboarding = ::completeOnboarding
            )
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Preview(showBackground = true)
@Composable
fun OnBoardingPreview() {
    UrVoiceTheme {
        OnBoardingScreen(
            navController = rememberNavController(),
            onFinishOnboarding = {}
        )
    }
}
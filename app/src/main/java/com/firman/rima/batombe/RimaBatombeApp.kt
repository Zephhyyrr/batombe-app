package com.firman.rima.batombe

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.firman.rima.batombe.ui.navigation.BottomAppBarWithFab
import com.firman.rima.batombe.ui.navigation.BottomNavItem
import com.firman.rima.batombe.ui.navigation.Screen
import com.firman.rima.batombe.ui.pages.*
import com.firman.rima.batombe.ui.theme.UrVoiceTheme
import com.firman.rima.batombe.ui.viewmodel.ArticleViewModel
import com.firman.rima.batombe.ui.viewmodel.HistoryViewModel
import com.firman.rima.batombe.ui.viewmodel.HomeViewModel
import com.firman.rima.batombe.ui.viewmodel.ProfileViewModel
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun UrVoiceApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BackHandler(enabled = true) {
        when (currentRoute) {
            Screen.Home.route, "article", Screen.Record.route,
            Screen.History.route, Screen.Profile.route, Screen.Login.route,
            Screen.Register.route, Screen.Sign.route, Screen.OnBoarding.route -> {
                (context as? Activity)?.finish()
            }

            else -> {
                if (!navController.popBackStack()) {
                    (context as? Activity)?.finish()
                }
            }
        }
    }
    UrVoiceRootApp(navController)
}

@Composable
fun UrVoiceRootApp(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val routesWithBottomNav = listOf(
        Screen.Home.route,
        Screen.Feed.route,
        Screen.Baraja.route,
        Screen.GeneratePantunLogin.route,
        Screen.History.route,
        Screen.Profile.route
    )

    val bottomNavItems = listOf(
        BottomNavItem(Screen.Home.route, "Home", painterResource(R.drawable.ic_home)),
        BottomNavItem(Screen.Feed.route, "Post", painterResource(R.drawable.ic_post)),
        BottomNavItem(Screen.Baraja.route, "Baraja", painterResource(R.drawable.ic_books)),
        BottomNavItem(
            route = Screen.GeneratePantunLogin.route,
            icon = painterResource(R.drawable.ic_star),
            isMainFeature = true
        ),
        BottomNavItem(Screen.History.route, "History", painterResource(R.drawable.ic_history)),
        BottomNavItem(Screen.Profile.route, "Profile", painterResource(R.drawable.ic_profile))
    )


    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(navController)
            }
            composable(Screen.OnBoarding.route) {
                OnBoardingScreen(
                    navController = navController,
                    onFinishOnboarding = {
                        navController.navigate(Screen.GeneratePantun.route) {
                            popUpTo(Screen.OnBoarding.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Sign.route) {
                SignScreen(navController)
            }
            composable(Screen.Login.route) {
                LoginScreen(navController)
            }
            composable(Screen.Register.route) {
                RegisterScreen(navController)
            }
            composable(Screen.Home.route) { backStackEntry ->
                val viewModel: HomeViewModel = hiltViewModel()
                val homeEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.Home.route)
                }
                val refreshHome by homeEntry.savedStateHandle.getLiveData<Boolean>("refreshHome")
                    .observeAsState()
                LaunchedEffect(refreshHome) {
                    if (refreshHome == true) {
                        viewModel.loadInitialData()
                        homeEntry.savedStateHandle["refreshHome"] = false
                    }
                }
                HomeScreen(navController = navController, viewModel = viewModel)
            }
            composable("article") {
                val viewModel: ArticleViewModel = hiltViewModel()
                ArticleScreen(viewModel = viewModel, navController = navController)
            }
            composable("article/{id}") { backStackEntry ->
                val viewModel: ArticleViewModel = hiltViewModel()
                val articleId =
                    backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: return@composable
                ArticleDetailScreen(
                    viewModel = viewModel,
                    articleId = articleId,
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(Screen.GeneratePantun.route) {
                GeneratePantunScreen(navController = navController)
            }
            composable(Screen.GeneratePantunLogin.route) {
                Box(modifier = Modifier.padding(bottom = 80.dp)) {
                    GeneratePantunLoginScreen(navController = navController)
                }
            }
            composable(
                route = Screen.OutputPantun.route,
                arguments = listOf(navArgument("pantunResult") { type = NavType.StringType })
            ) { backStackEntry ->
                val encodedPantun = backStackEntry.arguments?.getString("pantunResult") ?: ""
                val decodedPantun = URLDecoder.decode(encodedPantun, StandardCharsets.UTF_8.name())
                OutputPantunScreen(navController = navController, pantunText = decodedPantun)
            }
            composable(
                route = Screen.OutputPantunLogin.route,
                arguments = listOf(navArgument("pantunResult") { type = NavType.StringType })
            ) { backStackEntry ->
                val encodedPantun = backStackEntry.arguments?.getString("pantunResult") ?: ""
                val decodedPantun = URLDecoder.decode(encodedPantun, StandardCharsets.UTF_8.name())
                OutputPantunLoginScreen(navController = navController, pantunText = decodedPantun)
            }
            composable(
                route = Screen.Record.route,
                arguments = listOf(navArgument("pantunText") { type = NavType.StringType })
            ) { backStackEntry ->
                val encodedPantun = backStackEntry.arguments?.getString("pantunText") ?: ""
                val decodedPantun = URLDecoder.decode(encodedPantun, StandardCharsets.UTF_8.name())
                RecordScreen(navController = navController, pantunText = decodedPantun)
            }
            composable(
                route = Screen.RecordResult.route,
                arguments = listOf(
                    navArgument("pantunText") { type = NavType.StringType },
                    navArgument("audioFileName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val encodedPantun = backStackEntry.arguments?.getString("pantunText") ?: ""
                val decodedPantun = URLDecoder.decode(encodedPantun, StandardCharsets.UTF_8.name())
                val encodedAudioFileName =
                    backStackEntry.arguments?.getString("audioFileName") ?: ""
                val decodedAudioFileName =
                    URLDecoder.decode(encodedAudioFileName, StandardCharsets.UTF_8.name())
                RecordResultScreen(
                    navController = navController,
                    pantunText = decodedPantun,
                    audioFileName = decodedAudioFileName
                )
            }
            composable(
                route = "analyze/{text}/{audioFileName}",
                arguments = listOf(
                    navArgument("text") { type = NavType.StringType },
                    navArgument("audioFileName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val encodedText = backStackEntry.arguments?.getString("text") ?: ""
                val encodedAudio = backStackEntry.arguments?.getString("audioFileName") ?: ""
                URLDecoder.decode(encodedText, StandardCharsets.UTF_8.toString())
                URLDecoder.decode(encodedAudio, StandardCharsets.UTF_8.toString())
            }
            composable(Screen.History.route) {
                val viewModel: HistoryViewModel = hiltViewModel()
                val currentEntry by navController.currentBackStackEntryAsState()
                val refreshTrigger =
                    currentEntry?.savedStateHandle?.getLiveData<Boolean>("refreshHistory")
                        ?.observeAsState()
                LaunchedEffect(refreshTrigger?.value) {
                    if (refreshTrigger?.value == true) {
                        viewModel.getAllHistory()
                        currentEntry?.savedStateHandle?.set("refreshHistory", false)
                    }
                }
                HistoryScreen(
                    viewModel = viewModel,
                    onHistoryItemClick = { historyData ->
                        val historyId = historyData.id
                        if (historyId != null) {
                            navController.navigate("history_detail/$historyId")
                        }
                    }
                )
            }
            composable(
                route = "history_detail/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val historyId = backStackEntry.arguments?.getInt("id") ?: return@composable
                HistoryDetailScreen(
                    historyId = historyId,
                    onBackClick = { navController.popBackStack() },
                    navController = navController
                )
            }
            composable(Screen.Profile.route) {
                val viewModel: ProfileViewModel = hiltViewModel()
                val shouldRefresh =
                    navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>("refreshProfile")
                        ?.observeAsState()
                LaunchedEffect(shouldRefresh?.value) {
                    if (shouldRefresh?.value == true) {
                        viewModel.getCurrentUser()
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            "refreshProfile",
                            false
                        )
                    }
                }
                LaunchedEffect(Unit) {
                    viewModel.getCurrentUser()
                }
                ProfileScreen(
                    viewModel = viewModel,
                    navController = navController,
                    onEditProfileClick = { navController.navigate(Screen.EditProfile.route) }
                )
            }
            composable(Screen.EditProfile.route) {
                val viewModel: ProfileViewModel = hiltViewModel()
                EditProfileScreen(
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    onSaveClick = {
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            "refreshProfile",
                            true
                        )
                        val homeEntry = navController.getBackStackEntry(Screen.Home.route)
                        homeEntry.savedStateHandle["refreshHome"] = true
                        navController.popBackStack()
                    },
                    onNavigateToImagePreview = { imageUri ->
                        val encodedUri = URLEncoder.encode(
                            imageUri.toString(),
                            StandardCharsets.UTF_8.toString()
                        )
                        navController.navigate("image_profile_preview/$encodedUri")
                    }
                )
            }
            composable(
                route = "image_profile_preview/{imageUri}",
                arguments = listOf(navArgument("imageUri") { type = NavType.StringType })
            ) { backStackEntry ->
                val viewModel: ProfileViewModel = hiltViewModel()
                val encodedUri =
                    backStackEntry.arguments?.getString("imageUri") ?: return@composable
                val imageUri =
                    URLDecoder.decode(encodedUri, StandardCharsets.UTF_8.toString()).toUri()
                ImageProfilePreviewScreen(
                    imageUri = imageUri,
                    onBackClick = { navController.popBackStack() },
                    onImageUploaded = {
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            "refreshProfile",
                            true
                        )
                        val homeEntry = navController.getBackStackEntry(Screen.Home.route)
                        homeEntry.savedStateHandle["refreshHome"] = true
                        navController.popBackStack()
                    },
                    viewModel = viewModel
                )
            }

            composable(Screen.Feed.route) {
                FeedScreen(
                    navController = navController,
                    onCommentClick = { feedId ->
                        navController.navigate(Screen.FeedDetail.createRoute(feedId))
                    }
                )
            }

            composable(
                route = Screen.FeedDetail.route,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val feedId = backStackEntry.arguments?.getInt("id") ?: return@composable
                FeedDetailScreen(
                    feedId = feedId,
                    navController = navController
                )
            }

            composable(Screen.Baraja.route) {
                BarajaScreen()
            }
        }
        if (currentRoute in routesWithBottomNav) {
            BottomAppBarWithFab(
                items = bottomNavItems,
                navController = navController,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UrVoiceAppPreview() {
    UrVoiceTheme {
        UrVoiceRootApp()
    }
}
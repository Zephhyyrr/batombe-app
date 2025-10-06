package com.firman.gita.batombe.ui.navigation

import kotlinx.serialization.Serializable
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Serializable
sealed class Screen(val route: String) {
    @Serializable
    object Splash : Screen("splash")

    @Serializable
    object OnBoarding : Screen("onboarding")

    @Serializable
    object Sign : Screen("sign")

    @Serializable
    object Login : Screen("login")

    @Serializable
    object Register : Screen("register")

    @Serializable
    object Home : Screen("home")

    @Serializable
    object Analyze : Screen("analyze")

    @Serializable
    object History : Screen("history")

    @Serializable
    data class HistoryDetail(val id: Int) : Screen("history_detail/$id") {
        fun createRoute() = "history_detail/$id"
    }

    @Serializable
    object Profile : Screen("profile")

    @Serializable
    object EditProfile : Screen("editProfile")

    @Serializable
    object ImageProfilePreview : Screen("image_profile_preview")

    @Serializable
    object Record : Screen("record")

    @Serializable
    object SpeechToText : Screen("speech-to-text")

    @Serializable
    data class Article(val id: String) : Screen("article/{id}") {
        fun articleRoute() = "article/$id"
    }

    @Serializable
    object GeneratePantunLogin : Screen("generate_pantun_login")

    @Serializable
    object OutputPantunLogin : Screen("output_pantun_login/{pantunResult}") {
        fun createRoute(pantunText: String): String {
            val encodedPantun = URLEncoder.encode(pantunText, StandardCharsets.UTF_8.name())
            return "output_pantun_login/$encodedPantun"
        }
    }

    @Serializable
    object GeneratePantun : Screen("generate_pantun")

    @Serializable
    object OutputPantun : Screen("output_pantun/{pantunResult}") {
        fun createRoute(pantunText: String): String {
            val encodedPantun = URLEncoder.encode(pantunText, StandardCharsets.UTF_8.name())
            return "output_pantun/$encodedPantun"
        }
    }
}

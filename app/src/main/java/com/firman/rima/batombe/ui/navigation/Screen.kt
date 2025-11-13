package com.firman.rima.batombe.ui.navigation

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
    object Record : Screen("record/{pantunText}") {
        fun createRoute(pantunText: String): String {
            val encodedPantun = URLEncoder.encode(pantunText, StandardCharsets.UTF_8.name())
            return "record/$encodedPantun"
        }
    }

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
    object RecordResult : Screen("record_result/{pantunText}/{audioFileName}") {
        fun createRoute(pantunText: String, audioFileName: String): String {
            val encodedPantun = URLEncoder.encode(pantunText, StandardCharsets.UTF_8.name())
            val encodedAudioFileName = URLEncoder.encode(audioFileName, StandardCharsets.UTF_8.name())
            return "record_result/$encodedPantun/$encodedAudioFileName"
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

    @Serializable
    object Feed : Screen("feed")

    @Serializable
    object FeedDetail : Screen("feed_detail/{id}") {
        fun createRoute(id: Int) = "feed_detail/$id"
    }

    @Serializable
    object Baraja : Screen("baraja")
}

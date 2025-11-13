package com.firman.rima.batombe.utils

import com.firman.rima.batombe.BuildConfig

object MediaUrlUtils {
    fun buildMediaUrl( mediaUrl: String?): String {
        return when {
            mediaUrl.isNullOrBlank() -> ""
            mediaUrl.startsWith("http://") || mediaUrl.startsWith("https://") -> mediaUrl
            mediaUrl.startsWith("/uploads/") -> "${BuildConfig.BASE_URL.trimEnd('/')}$mediaUrl"
            else -> "${BuildConfig.BASE_URL.trimEnd('/')}/uploads/$mediaUrl"
        }
    }
}
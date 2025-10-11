package com.firman.gita.batombe.utils

import com.firman.gita.batombe.BuildConfig

object ApiConstant {
    const val BASE_URL = BuildConfig.BASE_URL

    // User Hit API
    const val USER_REGISTER = "api/users/register"
    const val USER_LOGIN = "api/users/login"
    const val USER_CURRENT = "/api/users/current"
    const val USER_LOGOUT = "api/users/logout"
    const val USER_PROFILE = "api/users/uploadPhotos"
    const val USER_UPDATE = "api/users/update"
    const val USER_DELETE = "api/users/delete"

    // Article Hit API
    const val ARTICLE_LIST = "api/articles/getAll"
    const val ARTICLE_DETAIL = "api/articles/getArticle/{id}"

    // Model Hit API
    const val SPEECH_TO_TEXT = "api/models/speech-to-text"
    const val ANALYZE_TEXT = "api/models/analyze-speech"

    // History Hit API
    const val HISTORY_LIST = "api/history/"
    const val HISTORY_DETAIL = "api/history/{id}"
    const val SAVE_HISTORY = "api/history/save"

    // Generate Pantun Hit API
    const val GENERATE_PANTUN = "/api/generate/"

    const val GET_ALL_EXAMPLE_VIDEOS = "/api/batombe-videos/"

    // Feed Hit API
    const val FEED_LIST = "api/feed/"
    const val FEED_BY_ID = "api/feed/{id}"
    const val PUBLISH_FEED = "api/history/{id}/public"

    // Comment Hit API
    const val POST_COMMENT = "api/comments/"
    const val GET_COMMENTS = "api/comments/history/{historyId}"

}
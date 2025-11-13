package com.firman.rima.batombe.data.remote.request

data class RegisterRequest (
    val name: String,
    val email: String,
    val password: String
)
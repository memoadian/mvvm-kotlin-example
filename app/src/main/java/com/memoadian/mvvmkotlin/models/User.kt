package com.memoadian.mvvmkotlin.models

data class User (
    val login: String,
    val avatarUrl: String?,
    val name: String?,
    val company: String?,
    val repoUrl: String?,
    val blog: String?
)
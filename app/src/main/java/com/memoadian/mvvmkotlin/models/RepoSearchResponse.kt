package com.memoadian.mvvmkotlin.models

import com.google.gson.annotations.SerializedName

class RepoSearchResponse (
    @SerializedName("total_count")
    val totalCount: Int = 0,
    @SerializedName("items")
    val items: List<Repo>
) {
    var nextPages: Int? = null
}
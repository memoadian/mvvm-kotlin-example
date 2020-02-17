package com.memoadian.mvvmkotlin.models

import androidx.room.Entity
import androidx.room.TypeConverters
import com.memoadian.mvvmkotlin.db.GithubTypeConverters

@Entity(
    primaryKeys = ["query"]
)
@TypeConverters(GithubTypeConverters::class)
class RepoSearchResult (
    val query: String,
    val repoIds: List<Int>,
    val totalCount: Int,
    val next: Int?
) {

}
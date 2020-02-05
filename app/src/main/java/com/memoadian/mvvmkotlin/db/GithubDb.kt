package com.memoadian.mvvmkotlin.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.memoadian.mvvmkotlin.models.Contributor
import com.memoadian.mvvmkotlin.models.Repo
import com.memoadian.mvvmkotlin.models.RepoSearchResult
import com.memoadian.mvvmkotlin.models.User

@Database(
    entities = [
        User::class,
        Repo::class,
        Contributor::class,
        RepoSearchResult::class
    ],
    version = 1
)
abstract class GithubDb: RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun repoDao(): RepoDao
}
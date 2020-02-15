package com.memoadian.mvvmkotlin.repository

import androidx.lifecycle.LiveData
import com.memoadian.mvvmkotlin.AppExecutors
import com.memoadian.mvvmkotlin.api.ApiResponse
import com.memoadian.mvvmkotlin.api.GithubApi
import com.memoadian.mvvmkotlin.db.GithubDb
import com.memoadian.mvvmkotlin.db.RepoDao
import com.memoadian.mvvmkotlin.models.Repo
import com.memoadian.mvvmkotlin.utils.RateLimiter
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepoRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val repoDao: RepoDao,
    private val githubApi: GithubApi,
    private val db: GithubDb
){
    private val repoListRateLimiter = RateLimiter<String>(10, TimeUnit.MINUTES)

    fun loadRepos (owner: String): LiveData<Resource<List<Repo>>>{
        return object: NetWorkBoundResource<List<Repo>, List<Repo>>(appExecutors){
            override fun loadFromDb(): LiveData<List<Repo>> {
                return repoDao.loadRepositories(owner)
            }

            override fun shoouldFetch(data: List<Repo>?): Boolean {
                return data == null || data.isEmpty() || repoListRateLimiter.shouldFecth(owner)
            }

            override fun saveCallResult(item: List<Repo>) {
                repoDao.insertRepos(item)
            }

            override fun createCall(): LiveData<ApiResponse<List<Repo>>> {
                return githubApi.getRepos(owner)
            }

            override fun onFetchFailed() {
                repoListRateLimiter.reset(owner) // si la peticion falla se hace un reset
            }

        }.asLiveData()
    }

    fun loadRepo (owner: String, name: String):LiveData<Resource<Repo>> {
        return object: NetWorkBoundResource<Repo, Repo>(appExecutors){
            override fun loadFromDb(): LiveData<Repo> {
                return repoDao.load(owner, name)
            }

            override fun shoouldFetch(data: Repo?): Boolean {
                return data == null
            }

            override fun saveCallResult(item: Repo) {
                repoDao.insert(item)
            }

            override fun createCall(): LiveData<ApiResponse<Repo>> {
                return githubApi.getRepo(owner, name)
            }

        }.asLiveData()
    }
}
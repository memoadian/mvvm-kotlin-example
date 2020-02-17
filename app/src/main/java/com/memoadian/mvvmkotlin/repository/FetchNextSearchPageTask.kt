package com.memoadian.mvvmkotlin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.memoadian.mvvmkotlin.api.*
import com.memoadian.mvvmkotlin.db.GithubDb
import com.memoadian.mvvmkotlin.models.RepoSearchResponse
import com.memoadian.mvvmkotlin.models.RepoSearchResult
import java.io.IOException
import java.lang.Exception

class FetchNextSearchPageTask (
    private val query: String,
    private val githubApi: GithubApi,
    private val db: GithubDb
): Runnable{
    private val _liveData = MutableLiveData<Resource<Boolean>>()
    val liveData: LiveData<Resource<Boolean>> = _liveData

    override fun run() {
        val currentData = db.repoDao().findSearchResult(query)
        if (currentData == null) {
            _liveData.postValue(null)
            return
        }
        val nextPage = currentData.next
        if (nextPage == null) {
            _liveData.postValue(Resource.success(false))
            return
        }

        val newValue = try {
            val response = githubApi.searchRepos(query, nextPage).execute()
            val apiResponse: ApiResponse<RepoSearchResponse> = ApiResponse.create(response)
            when (apiResponse) {
                is ApiSuccessResponse->{
                    val ids = arrayListOf<Int>()
                    ids.addAll(currentData.repoIds)
                    ids.addAll(apiResponse.body.items.map{it.id})
                    val merge = RepoSearchResult(query, ids, apiResponse.body.total, apiResponse.nextPage)
                    try {
                        db.beginTransaction()
                        db.repoDao().insert(merge)
                        db.repoDao().insertRepos(apiResponse.body.items)
                        db.setTransactionSuccessful()
                    } finally {
                        db.endTransaction()
                    }

                    Resource.success(apiResponse.nextPage != null)
                }
                is ApiEmptyResponse->{
                    Resource.success(false)
                }
                is ApiErrorResponse->{
                    Resource.error(apiResponse.errorMessage, true)
                }
            }
        } catch (e: IOException){
            Resource.error(e.message!!, true)
        }
        _liveData.postValue(newValue)
    }
}
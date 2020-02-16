package com.memoadian.mvvmkotlin.repository

import androidx.lifecycle.LiveData
import com.memoadian.mvvmkotlin.AppExecutors
import com.memoadian.mvvmkotlin.api.ApiResponse
import com.memoadian.mvvmkotlin.api.GithubApi
import com.memoadian.mvvmkotlin.db.UserDao
import com.memoadian.mvvmkotlin.models.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val userDao: UserDao,
    private val githubApi: GithubApi
) {
    fun loadUser (login: String): LiveData<Resource<User>> {
        return object : NetWorkBoundResource<User, User>(appExecutors){

            override fun loadFromDb(): LiveData<User> {
                return userDao.findByLogin(login) //get user from local db after saved
            }

            override fun shoouldFetch(data: User?): Boolean {
                return data == null//if the user exists on db local not load again
            }

            override fun saveCallResult(item: User) {
                userDao.insert(item)//save user in database after get from createCall
            }

            override fun createCall(): LiveData<ApiResponse<User>> {
                return githubApi.getUser(login) //get user from github api if shouldFetch return true
            }

        }.asLiveData()
    }
}
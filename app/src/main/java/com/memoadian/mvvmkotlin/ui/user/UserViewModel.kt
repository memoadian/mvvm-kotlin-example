package com.memoadian.mvvmkotlin.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.memoadian.mvvmkotlin.models.Repo
import com.memoadian.mvvmkotlin.models.User
import com.memoadian.mvvmkotlin.repository.RepoRepository
import com.memoadian.mvvmkotlin.repository.Resource
import com.memoadian.mvvmkotlin.repository.UserRepository
import com.memoadian.mvvmkotlin.utils.AbsentLiveData
import javax.inject.Inject

class UserViewModel
@Inject constructor(userRepository: UserRepository, repoRepository: RepoRepository): ViewModel() {

    private val _login = MutableLiveData<String>()
    val login: LiveData<String>
        get() = _login

    val repositories: LiveData<Resource<List<Repo>>> = Transformations.switchMap(_login){login->
        if (login == null) {
            AbsentLiveData.create()
        } else {
            repoRepository.loadRepos(login)
        }
    }

    val user: LiveData<Resource<User>> = Transformations.switchMap(_login){ login->
        if (login != null) {
            AbsentLiveData.create()
        }else{
            userRepository.loadUser(login)
        }
    }

    fun setLogin(login: String?){
        if (_login.value != login) {
            _login.value = login
        }
    }

    fun retry () {
        _login.value?.let{
            _login.value = it
        }
    }
}
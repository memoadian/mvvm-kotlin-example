package com.memoadian.mvvmkotlin.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.memoadian.mvvmkotlin.models.User

@Dao
interface UserDao {
    //si le llega un usuario con el mismo primary key reemplaza los datos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    //seleccionamos el usuario por login que es el primary key
    @Query("SELECT * FROM user WHERE login = :login")
    fun findByLogin(login: String): LiveData<User>


}
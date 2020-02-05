package com.memoadian.mvvmkotlin.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.SerializedName

//esto se usa para agilizar las consultas
//pero es lento con las inserciones
@Entity(
    indices = [Index("id"), Index("owner_login")],
    primaryKeys = ["name", "owner_login"]
)
data class Repo (
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("node_id")
    val nodeId: String,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("full_name")
    val fullName: String,
    @field:SerializedName("private")
    val private: Boolean,
    @field:SerializedName("description")
    val description: String?,
    @field:SerializedName("owner")
    @field:Embedded(prefix = "owner_")
    val owner: Owner,
    @field:SerializedName("stargazers_count")
    val stars: Int
){
    data class Owner (
        @field:SerializedName("login")
        val login: String,
        @field:SerializedName("url")
        val url: String?
    )

    companion object{
        const val UNKNOWN_ID = -1
    }
}

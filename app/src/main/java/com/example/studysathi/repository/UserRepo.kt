package com.example.studysathi.repository

import com.example.studysathi.model.Usermodel

interface UserRepo {
    fun login(email: String, password: String, Callback: (Usermodel?, String) -> Unit)

    fun register(email: String,password: String,Callback: (Boolean, String, String) -> Unit)

    fun addUserToDatabase(userID: String, model: Usermodel, Callback: (Boolean, String) -> Unit)

    fun forgetpassword(email: String, Callback: (Boolean, String) -> Unit)

    fun DeleteAccount(userID:String, Callback: (Boolean, String) -> Unit)

    fun editProfile(userID: String, model: Usermodel, Callback: (Boolean, String) -> Unit)

    fun getUserByID(userID: String,Callback: (Boolean, String, Usermodel?) -> Unit)

    fun getAllUser(Callback: (Boolean, String, List<Usermodel>) -> Unit)
}
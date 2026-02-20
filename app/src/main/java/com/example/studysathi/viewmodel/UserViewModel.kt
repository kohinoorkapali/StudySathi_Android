package com.example.studysathi.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studysathi.model.Usermodel
import com.example.studysathi.repository.UserRepo

class UserViewModel(val repo: UserRepo): ViewModel() {
    fun login(email: String, password: String, callback: (Usermodel?, String) -> Unit) {
        repo.login(email, password, callback)
    }

    fun register(
        email: String, password: String,
        callback: (Boolean, String, String) -> Unit
    ){
        repo.register(email,password,callback)
    }

    fun addUserToDatabase(
        userId: String, model: Usermodel,
        callback: (Boolean, String) -> Unit
    ){
        repo.addUserToDatabase(userId,model,callback)
    }

    fun forgetPassword(
        email: String,
        callback: (Boolean, String) -> Unit
    ){
        repo.forgetpassword(email,callback)
    }

    fun deleteAccount(userId: String, callback: (Boolean, String) -> Unit){
        repo.DeleteAccount(userId,callback)
    }

    fun editProfile(
        userId: String, model: Usermodel,
        callback: (Boolean, String) -> Unit
    ){
        repo.editProfile(userId,model,callback)
    }

    private val _users = MutableLiveData<Usermodel?>()
    val users : MutableLiveData<Usermodel?>
        get() = _users

    private val _allUsers = MutableLiveData<List<Usermodel>?>()
    val allUsers : MutableLiveData<List<Usermodel>?>
        get() = _allUsers

    fun getUserById(
        userId: String
    ){
        repo.getUserByID(userId){
                success,msg,data->
            if(success){
                _users.postValue(data)
            }
        }
    }

    fun getAllUser(){
        repo.getAllUser {
                success,msg,data->
            if(success){
                _allUsers.postValue(data)
            }
        }
    }
}
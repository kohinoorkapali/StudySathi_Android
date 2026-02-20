package com.example.studysathi.repository

import com.example.studysathi.model.Usermodel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.collections.toMap

class UserRepoImpl: UserRepo {
    val auth : FirebaseAuth = FirebaseAuth.getInstance()
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val ref : DatabaseReference = database.getReference("Users")

    override fun login(email: String, password: String, Callback: (Usermodel?, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    if (uid != null) {
                        ref.child(uid).get().addOnSuccessListener { snapshot ->
                            val user = snapshot.getValue(Usermodel::class.java)
                            if (user != null) {
                                Callback(user, "Login success")
                            } else {
                                Callback(null, "User data not found")
                            }
                        }.addOnFailureListener {
                            Callback(null, it.message ?: "Failed to fetch user")
                        }
                    } else {
                        Callback(null, "User ID not found")
                    }
                } else {
                    Callback(null, task.exception?.message ?: "Login failed")
                }
            }
    }

    override fun register(
        email: String,
        password: String,
        Callback: (Boolean, String, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Callback(true,"Registration success", "${auth.currentUser?.uid}")
                }else{
                    Callback(false,"${it.exception?.message}","")
                }
            }
    }

    override fun addUserToDatabase(
        userID: String,
        model: Usermodel,
        Callback: (Boolean, String) -> Unit
    ) {
        ref.child(userID).setValue(model).addOnCompleteListener {
            if (it.isSuccessful){
                Callback(true,"Registration Complete")
            }else{
                Callback(false,"${it.exception?.message}")
            }
        }
    }

    override fun forgetpassword(
        email: String,
        Callback: (Boolean, String) -> Unit
    ) {
        val trimmedEmail = email.trim()
        if (trimmedEmail.isBlank()) {
            Callback(false, "Please enter email")
            return
        }

        auth.sendPasswordResetEmail(trimmedEmail)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Callback(true, "Reset email sent to $trimmedEmail")
                } else {
                    Callback(false, task.exception?.message ?: "Email not registered")
                }
            }
    }



    override fun DeleteAccount(
        userID: String,
        Callback: (Boolean, String) -> Unit
    ) {
        ref.child(userID).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                Callback(true, "Account deleted successfullt")
            } else {
                Callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun editProfile(
        userID: String,
        model: Usermodel,
        Callback: (Boolean, String) -> Unit
    ) {
        ref.child(userID).updateChildren(model.toMap()).addOnCompleteListener {
            if (it.isSuccessful) {
                Callback(true, "Profile Updated successfully")
            } else {
                Callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun getUserByID(
        userID: String,
        Callback: (Boolean, String, Usermodel?) -> Unit
    ) {
        ref.child(userID)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val user = snapshot.getValue(Usermodel::class.java)
                        if(user != null){
                            Callback(true,"Profile fetched",user)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Callback(false,error.message, null)
                }
            })
    }

    override fun getAllUser(Callback: (Boolean, String, List<Usermodel>) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    var allUsers = mutableListOf<Usermodel>()
                    for(data in snapshot.children){
                        var user = data.getValue(Usermodel::class.java)
                        if(user != null){
                            allUsers.add(user)
                        }
                    }
                    Callback(true,"data fetched",allUsers)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Callback(false,error.message,emptyList())
            }
        })
    }
}
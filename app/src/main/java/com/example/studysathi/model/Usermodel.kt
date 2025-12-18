package com.example.studysathi.model

data class Usermodel(
    val id: String = "",
    val fullName: String = "",
    val username: String = "",
    val email: String = "",
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "fullName" to fullName,
            "username" to username,
            "email" to email,
            )
    }

}

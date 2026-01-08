package com.example.studysathi.model

data class MaterialModel(
    val id: String = "",
    val title: String = "",
    val stream: String = "",
    val description: String = "",
    val fileName: String = ""
){
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "title" to title,
            "stream" to stream,
            "description" to description,
            "fileName" to fileName
        )
    }
}

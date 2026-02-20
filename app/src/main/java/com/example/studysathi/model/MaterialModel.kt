package com.example.studysathi.model

data class MaterialModel(
    val id: String = "",
    val title: String = "",
    val stream: String = "",
    val description: String = "",
    val fileName: String = "",
    val uploadedBy: String = "", // who uploaded this
    val fileUrl: String = ""     // Cloudinary URL of the file
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "title" to title,
            "stream" to stream,
            "description" to description,
            "fileName" to fileName,
            "uploadedBy" to uploadedBy,
            "fileUrl" to fileUrl
        )
    }
}

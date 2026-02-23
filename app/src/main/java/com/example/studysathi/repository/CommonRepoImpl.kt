package com.example.studysathi.repository

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import java.io.InputStream
import java.util.concurrent.Executors

class CommonRepoImpl: CommonRepo {
    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to "delpryl7r",
            "api_key" to "819945737367188",
            "api_secret" to "IZbwYZaPMKUUx236N6BUyvN3kDc"
        )
    )
    override fun uploadFile(
        context: Context,
        fileUri: Uri,
        callback: (String?) -> Unit
    ) {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(fileUri)
                var fileName = getFileNameFromUri(context, fileUri)
                fileName = fileName?.substringBeforeLast(".") ?: "uploaded_file"

                val response = cloudinary.uploader().upload(
                    inputStream, ObjectUtils.asMap(
                        "public_id", fileName,
                        "resource_type", "raw" // auto-detect file type
                    )
                )

                var fileUrl = response["url"] as String?
                fileUrl = fileUrl?.replace("http://", "https://")

                Handler(Looper.getMainLooper()).post {
                    callback(fileUrl)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Handler(Looper.getMainLooper()).post {
                    callback(null)
                }
            }
        }
    }
    override fun getFileNameFromUri(
        context: Context,
        fileUri: Uri
    ): String? {
        var fileName: String? = null
        val cursor: Cursor? = context.contentResolver.query(fileUri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName
    }
}

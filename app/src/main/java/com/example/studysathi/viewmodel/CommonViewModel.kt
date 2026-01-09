package com.example.studysathi.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.studysathi.repository.CommonRepo

class CommonViewModel (private val repo: CommonRepo) : ViewModel(){
    fun uploadFile(
        context: Context,
        fileUri: Uri,
        callback: (String?) -> Unit
    ) {
        repo.uploadFile(context, fileUri, callback)
    }

    fun getFileName(context: Context, fileUri: Uri): String? {
        return repo.getFileNameFromUri(context, fileUri)
    }
}
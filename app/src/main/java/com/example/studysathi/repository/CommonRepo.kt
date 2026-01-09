package com.example.studysathi.repository

import android.content.Context
import android.net.Uri

interface CommonRepo {

        fun uploadFile(context: Context, fileUri: Uri, callback: (String?) -> Unit)
        fun getFileNameFromUri(context: Context, fileUri: Uri): String?
    }

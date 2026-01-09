package com.example.studysathi.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studysathi.model.MaterialModel
import com.example.studysathi.repository.MaterialRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MaterialViewModel (private val repo: MaterialRepo) : ViewModel() {
    val title = MutableStateFlow("")
    val stream = MutableStateFlow("")
    val description = MutableStateFlow("")
    val fileUri = MutableStateFlow<Uri?>(null) // optional for now
    val fileName = MutableStateFlow("")       // store filename for now

    // Status for UI
    private val _status = MutableStateFlow<String?>(null)
    val status: StateFlow<String?> get() = _status

    // Clear status after showing
    fun clearStatus() {
        _status.value = null
    }

    // Setters
    fun setTitle(value: String) {
        viewModelScope.launch { title.value = value }
    }

    fun setStream(value: String) {
        viewModelScope.launch { stream.value = value }
    }

    fun setDescription(value: String) {
        viewModelScope.launch { description.value = value }
    }

    fun setFile(uri: Uri, name: String) {
        viewModelScope.launch {
            fileUri.value = uri
            fileName.value = name
        }
    }

    /**
     * Upload material metadata to Firebase DB
     * For now, file itself is not uploaded, just fileName
     */
    fun uploadMaterial(onSuccess: (() -> Unit)? = null) {
        val currentTitle = title.value.trim()
        val currentStream = stream.value.trim()
        val currentDescription = description.value.trim()
        val currentFileName = fileName.value.trim()

        if (currentTitle.isEmpty() || currentStream.isEmpty() || currentDescription.isEmpty()) {
            _status.value = "Please fill all fields"
            return
        }

        // Create MaterialModel
        val material = MaterialModel(
            id = "",  // will be generated in repo
            title = currentTitle,
            stream = currentStream,
            description = currentDescription,
            fileName = currentFileName
        )

        viewModelScope.launch {
            repo.addMaterial(material) { success, message ->
                _status.value = message
                if (success) {
                    // call the success callback to navigate
                    onSuccess?.invoke()
                }
            }
        }
    }

}
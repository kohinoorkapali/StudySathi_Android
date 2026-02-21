package com.example.studysathi.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studysathi.model.MaterialModel
import com.example.studysathi.repository.MaterialRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MaterialViewModel(private val repo: MaterialRepo) : ViewModel() {
    val title = MutableStateFlow("")
    val stream = MutableStateFlow("")
    val description = MutableStateFlow("")
    val fileUri = MutableStateFlow<Uri?>(null)
    val fileName = MutableStateFlow("")

    private val _status = MutableStateFlow<String?>(null)
    val status: StateFlow<String?> get() = _status

    private val _materials = MutableStateFlow<List<MaterialModel>>(emptyList())
    val materials: StateFlow<List<MaterialModel>> get() = _materials

    fun clearStatus() { _status.value = null }

    fun setTitle(value: String) { viewModelScope.launch { title.value = value } }
    fun setStream(value: String) { viewModelScope.launch { stream.value = value } }
    fun setDescription(value: String) { viewModelScope.launch { description.value = value } }
    fun setFile(uri: Uri, name: String) {
        viewModelScope.launch {
            fileUri.value = uri
            fileName.value = name
        }
    }

    fun uploadMaterial(
        uploadedBy: String,
        uploadedById: String,
        fileUrl: String,
        onSuccess: (() -> Unit)? = null
    ) {
        val currentTitle = title.value.trim()
        val currentStream = stream.value.trim()
        val currentDescription = description.value.trim()

        if (currentTitle.isEmpty() || currentStream.isEmpty() || currentDescription.isEmpty()) {
            _status.value = "Please fill all fields"
            return
        }

        val material = MaterialModel(
            id = "",
            title = currentTitle,
            stream = currentStream,
            description = currentDescription,
            fileName = fileName.value,
            uploadedBy = uploadedBy,
            uploadedById = uploadedById,   // ✅ ADDED
            fileUrl = fileUrl,
            uploadedAt = System.currentTimeMillis()
        )

        viewModelScope.launch {
            repo.addMaterial(material) { success, message ->
                _status.value = message
                if (success) onSuccess?.invoke()
            }
        }
    }

    fun fetchAllMaterials() {
        repo.getAllMaterials { success, _, list ->
            if (success) _materials.value = list
        }
    }

    fun updateMaterial(
        id: String,
        title: String,
        stream: String,
        description: String
    ) {
        viewModelScope.launch {

            repo.getAllMaterials { success, _, list ->
                if (success) {

                    val oldMaterial = list.find { it.id == id }
                    if (oldMaterial != null) {

                        val updatedMaterial = oldMaterial.copy(
                            title = title.trim(),
                            stream = stream.trim(),
                            description = description.trim()
                        )

                        repo.updateMaterial(id, updatedMaterial) { updateSuccess, message ->
                            _status.value = message
                            if (updateSuccess) {
                                fetchAllMaterials() // refresh list
                            }
                        }
                    }
                }
            }
        }
    }

    fun deleteMaterial(
        id: String,
        onComplete: ((Boolean, String) -> Unit)? = null
    ) {
        viewModelScope.launch {
            repo.deleteMaterial(id) { success, message ->
                _status.value = message
                if (success) fetchAllMaterials() // refresh list
                onComplete?.invoke(success, message)
            }
        }
    }
    fun updateAllMaterialsUsername(userID: String, newUsername: String) {
        viewModelScope.launch {
            repo.getAllMaterials { success, _, list ->
                if (success) {
                    list.filter { it.uploadedById == userID }.forEach { material ->
                        val updated = material.copy(uploadedBy = newUsername)
                        repo.updateMaterial(material.id, updated) { _, _ -> }
                    }
                    fetchAllMaterials() // refresh local list
                }
            }
        }
    }
}
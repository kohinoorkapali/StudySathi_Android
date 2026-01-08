package com.example.studysathi.repository

import com.example.studysathi.model.MaterialModel

interface MaterialRepo {
    fun addMaterial(model: MaterialModel, callback: (Boolean, String) -> Unit)

    fun updateMaterial(materialID: String, model: MaterialModel, callback: (Boolean, String) -> Unit)

    fun deleteMaterial(materialID: String, callback: (Boolean, String) -> Unit)

    fun getMaterialByID(materialID: String, callback: (Boolean, String, MaterialModel?) -> Unit)

    fun getAllMaterials(callback: (Boolean, String, List<MaterialModel>) -> Unit)

    fun getMaterialsByStream(stream: String, callback: (Boolean, String, List<MaterialModel>) -> Unit)
}
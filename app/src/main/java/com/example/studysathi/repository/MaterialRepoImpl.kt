package com.example.studysathi.repository

import com.example.studysathi.model.MaterialModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MaterialRepoImpl : MaterialRepo {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ref: DatabaseReference = database.getReference("Materials")
    override fun addMaterial(
        model: MaterialModel,
        callback: (Boolean, String) -> Unit
    ) {
        val materialID = model.id.ifBlank { ref.push().key ?: "" }

        if (materialID.isBlank()) {
            callback(false, "Unable to generate material ID")
            return
        }

        // ✅ VERY IMPORTANT
        val materialWithId = model.copy(id = materialID)

        ref.child(materialID)
            .setValue(materialWithId)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(true, "Material added successfully")
                } else {
                    callback(false, it.exception?.message ?: "Failed to add material")
                }
            }
    }

    override fun updateMaterial(
        materialID: String,
        model: MaterialModel,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(materialID).updateChildren(model.toMap()).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Material updated successfully")
            } else {
                callback(false, it.exception?.message ?: "Failed to update material")
            }
        }
    }

    override fun deleteMaterial(
        materialID: String,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(materialID).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Material deleted successfully")
            } else {
                callback(false, it.exception?.message ?: "Failed to delete material")
            }
        }
    }

    override fun getMaterialByID(
        materialID: String,
        callback: (Boolean, String, MaterialModel?) -> Unit
    ) {
        ref.child(materialID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val material = snapshot.getValue(MaterialModel::class.java)
                if (material != null) {
                    callback(true, "Material fetched", material)
                } else {
                    callback(false, "Material not found", null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }

    override fun getAllMaterials(callback: (Boolean, String, List<MaterialModel>) -> Unit) {
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val materials = mutableListOf<MaterialModel>()
                for (data in snapshot.children) {
                    val material = data.getValue(MaterialModel::class.java)
                    if (material != null) {
                        materials.add(material)
                    }
                }
                callback(true, "Materials fetched", materials)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, emptyList())
            }
        })
    }

    override fun getMaterialsByStream(
        stream: String,
        callback: (Boolean, String, List<MaterialModel>) -> Unit
    ) {
        ref.orderByChild("stream").equalTo(stream)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val filteredMaterials = mutableListOf<MaterialModel>()
                    for (data in snapshot.children) {
                        val material = data.getValue(MaterialModel::class.java)
                        if (material != null) {
                            filteredMaterials.add(material)
                        }
                    }
                    callback(true, "Materials fetched for stream: $stream", filteredMaterials)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, emptyList())
                }
            })
    }
}
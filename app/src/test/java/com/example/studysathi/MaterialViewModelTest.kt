package com.example.studysathi

import android.net.Uri
import com.example.studysathi.model.MaterialModel
import com.example.studysathi.repository.MaterialRepo
import com.example.studysathi.viewmodel.MaterialViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.mockito.kotlin.*

class MaterialViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        // Set Main dispatcher for viewModelScope
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun upload_success_test() {
        val repo = mock<MaterialRepo>()
        val viewModel = MaterialViewModel(repo)

        // Set values
        viewModel.setTitle("Test Title")
        viewModel.setStream("CSIT")
        viewModel.setDescription("Test Description")
        viewModel.setFile(Uri.parse("test://file"), "file.pdf")

        // Mock repo callback
        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(1)
            callback(true, "Upload success")
            null
        }.`when`(repo).addMaterial(any(), any())

        // Call function
        viewModel.uploadMaterial(
            uploadedBy = "TestUser",
            uploadedById = "123",
            fileUrl = "testUrl"
        )

        // Run all coroutines
        testDispatcher.scheduler.advanceUntilIdle()

        // Assertions
        Assert.assertEquals("Upload success", viewModel.status.value)
        verify(repo).addMaterial(any(), any())
    }

    @Test
    fun update_success_test() {
        val repo = mock<MaterialRepo>()
        val viewModel = MaterialViewModel(repo)

        val existingMaterial = MaterialModel(
            id = "mat1",
            title = "Old Title",
            stream = "CSIT",
            description = "Old Description",
            fileName = "file.pdf",
            uploadedBy = "User",
            uploadedById = "123",
            fileUrl = "url",
            uploadedAt = System.currentTimeMillis()
        )

        // Mock getAllMaterials to return a list
        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String, List<MaterialModel>) -> Unit>(0)
            callback(true, "Fetched", listOf(existingMaterial))
            null
        }.`when`(repo).getAllMaterials(any())

        // Mock updateMaterial callback
        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(2)
            callback(true, "Update success")
            null
        }.`when`(repo).updateMaterial(eq("mat1"), any(), any())

        // Call update
        viewModel.updateMaterial(
            id = "mat1",
            title = "New Title",
            stream = "CSIT",
            description = "New Description"
        )

        // Run all coroutines
        testDispatcher.scheduler.advanceUntilIdle()

        // Assertions
        Assert.assertEquals("Update success", viewModel.status.value)
        verify(repo).updateMaterial(eq("mat1"), any(), any())
    }
}
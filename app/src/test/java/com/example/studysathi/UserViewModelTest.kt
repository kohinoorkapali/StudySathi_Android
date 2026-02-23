package com.example.studysathi

import com.example.studysathi.model.Usermodel
import org.junit.Test
import com.example.studysathi.repository.UserRepo
import com.example.studysathi.viewmodel.UserViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class UserViewModelTest {
    @Test
    fun login_success_test() {

        val repo = mock<UserRepo>()
        val viewModel = UserViewModel(repo)

        val fakeUser = Usermodel(
            id = "1",
            fullName = "Test User",
            username = "testuser",
            email = "test@gmail.com"
        )

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Usermodel?, String) -> Unit>(2)
            callback(fakeUser, "Login success")
            null
        }.`when`(repo).login(eq("test@gmail.com"), eq("123456"), any())

        var userResult: Usermodel? = null
        var messageResult = ""

        viewModel.login("test@gmail.com", "123456") { user, msg ->
            userResult = user
            messageResult = msg
        }

        assertEquals(fakeUser, userResult)
        assertEquals("Login success", messageResult)

        verify(repo).login(eq("test@gmail.com"), eq("123456"), any())
    }

    @Test
    fun register_success_test() {

        val repo = mock<UserRepo>()
        val viewModel = UserViewModel(repo)

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String, String) -> Unit>(2)
            callback(true, "Register success", "user123")
            null
        }.`when`(repo).register(eq("test@gmail.com"), eq("123456"), any())

        var successResult = false
        var messageResult = ""
        var userIdResult = ""

        viewModel.register("test@gmail.com", "123456") { success, msg, userId ->
            successResult = success
            messageResult = msg
            userIdResult = userId
        }

        assertTrue(successResult)
        assertEquals("Register success", messageResult)
        assertEquals("user123", userIdResult)

        verify(repo).register(eq("test@gmail.com"), eq("123456"), any())
    }
}
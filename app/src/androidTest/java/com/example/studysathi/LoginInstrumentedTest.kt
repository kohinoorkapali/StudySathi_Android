package com.example.studysathi

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import com.example.studysathi.model.Usermodel
import com.example.studysathi.repository.UserRepoImpl
import com.example.studysathi.view.LoginActicity
import org.junit.After

@RunWith(AndroidJUnit4::class)
class LoginInstrumentedTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<LoginActicity>()

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testLoginNavigatesToUserDashboard() {
        // Input credentials
        composeRule.onNodeWithTag("email")
            .performTextInput("kohinoorkapali27@gmail.com")

        composeRule.onNodeWithTag("password")
            .performTextInput("Kohinoor123%")

        // Click Login
        composeRule.onNodeWithTag("login")
            .performClick()

        // Wait for Firebase login to finish (up to 10 seconds)
        composeRule.waitUntil(timeoutMillis = 10000) {
            // Check if the intent has fired
            try {
                intended(hasComponent(DashboardActivity::class.java.name))
                true
            } catch (e: AssertionError) {
                false
            }
        }
    }
}
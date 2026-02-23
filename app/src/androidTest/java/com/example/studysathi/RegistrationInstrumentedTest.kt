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
import com.example.studysathi.view.LoginActicity
import com.example.studysathi.view.RegistrationActivity
import org.junit.After

@RunWith(AndroidJUnit4::class)
class RegistrationInstrumentedTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<RegistrationActivity>()

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testSuccessfulRegistrationNavigatesToLogin() {
        // Fill registration form
        composeRule.onNodeWithTag("fullName").performTextInput("Kohinoor Kapali")
        composeRule.onNodeWithTag("username").performTextInput("kohinoor27")
        composeRule.onNodeWithTag("email").performTextInput("testuser@gmail.com")
        composeRule.onNodeWithTag("password").performTextInput("Kohinoor123%")
        composeRule.onNodeWithTag("confirmPassword").performTextInput("Kohinoor123%")

        // Click Register button
        composeRule.onNodeWithTag("register").performClick()

        // Wait until navigation to LoginActivity occurs
        composeRule.waitUntil(timeoutMillis = 10000) {
            try {
                intended(hasComponent(LoginActicity::class.java.name))
                true
            } catch (e: AssertionError) {
                false
            }
        }
    }
}
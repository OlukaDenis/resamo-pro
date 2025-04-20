package com.dennytech.resamopro.ui.screen.auth.login

import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.usecases.auth.LoginUseCase
import com.dennytech.resamopro.ui.models.events.LoginEvent
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class CoroutineTestRule : TestWatcher() {
    private val testDispatcher = StandardTestDispatcher()
    val testScope = TestScope(testDispatcher)

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var viewModel: LoginViewModel
    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun setup() {
        loginUseCase = mockk()
        viewModel = LoginViewModel(loginUseCase)
    }

    @Test
    fun `initial state is correct`() {
        val initialState = viewModel.formState
        assertThat(initialState.email).isEmpty()
        assertThat(initialState.password).isEmpty()
        assertThat(initialState.loading).isFalse()
        assertThat(initialState.error).isEmpty()
        assertThat(initialState.errorDialog).isFalse()
        assertThat(initialState.user).isNull()
    }

    @Test
    fun `email change updates state`() {
        val testEmail = "test@example.com"
        viewModel.onEvent(LoginEvent.EmailChanged(testEmail))
        assertThat(viewModel.formState.email).isEqualTo(testEmail)
    }

    @Test
    fun `password change updates state`() {
        val testPassword = "password123"
        viewModel.onEvent(LoginEvent.PasswordChanged(testPassword))
        assertThat(viewModel.formState.password).isEqualTo(testPassword)
    }

    @Test
    fun `submit with empty email shows error`() {
        viewModel.onEvent(LoginEvent.Submit)
        assertThat(viewModel.formState.error).isEqualTo("Email required")
    }

    @Test
    fun `submit with empty password shows error`() {
        viewModel.onEvent(LoginEvent.EmailChanged("test@example.com"))
        viewModel.onEvent(LoginEvent.Submit)
        assertThat(viewModel.formState.error).isEqualTo("Password required")
    }

    @Test
    fun `successful login updates state correctly`() = coroutineTestRule.testScope.runTest {
        val testEmail = "test@example.com"
        val testPassword = "password123"
        val testUser = UserDomainModel(id = "1", firstName = "Test", lastName = "User", email = testEmail)

        coEvery { loginUseCase(LoginUseCase.Param(testEmail, testPassword)) } returns flow {
            emit(Resource.Success(testUser))
        }

        viewModel.onEvent(LoginEvent.EmailChanged(testEmail))
        viewModel.onEvent(LoginEvent.PasswordChanged(testPassword))
        viewModel.onEvent(LoginEvent.Submit)

        advanceUntilIdle()

        assertThat(viewModel.formState.loading).isFalse()
        assertThat(viewModel.formState.error).isEmpty()
        assertThat(viewModel.formState.user).isEqualTo(testUser)
        assertThat(viewModel.loginComplete).isTrue()
    }

    @Test
    fun `failed login shows error dialog`() = coroutineTestRule.testScope.runTest {
        val testEmail = "test@example.com"
        val testPassword = "password123"
        val errorMessage = "Invalid credentials"

        coEvery { loginUseCase(LoginUseCase.Param(testEmail, testPassword)) } returns flow {
            emit(Resource.Error(errorMessage))
        }

        viewModel.onEvent(LoginEvent.EmailChanged(testEmail))
        viewModel.onEvent(LoginEvent.PasswordChanged(testPassword))
        viewModel.onEvent(LoginEvent.Submit)

        advanceUntilIdle()

        assertThat(viewModel.formState.loading).isFalse()
        assertThat(viewModel.formState.errorDialog).isTrue()
        assertThat(viewModel.formState.error).isEqualTo(errorMessage)
        assertThat(viewModel.loginComplete).isFalse()
    }
} 
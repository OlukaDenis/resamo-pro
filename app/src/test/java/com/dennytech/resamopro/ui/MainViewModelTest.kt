package com.dennytech.resamopro.ui

import com.dennytech.domain.models.ProductCategoryDomainModel
import com.dennytech.domain.models.StoreDomainModel
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.usecases.LogoutUseCase
import com.dennytech.domain.usecases.account.GetCurrentUserUseCase
import com.dennytech.domain.usecases.store.GetSelectedStoreUseCase
import com.dennytech.domain.usecases.store.GetUserStoreListUseCase
import com.dennytech.resamopro.models.KeyValueModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
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
class MainViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var viewModel: MainViewModel
    private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase
    private lateinit var logoutUseCase: LogoutUseCase
    private lateinit var getUserStoreListUseCase: GetUserStoreListUseCase
    private lateinit var getSelectedStoreUseCase: GetSelectedStoreUseCase

    @Before
    fun setup() {
        getCurrentUserUseCase = mockk(relaxed = true)
        logoutUseCase = mockk(relaxed = true)
        getUserStoreListUseCase = mockk(relaxed = true)
        getSelectedStoreUseCase = mockk(relaxed = true)

        viewModel = MainViewModel(
            getCurrentUserUseCase = getCurrentUserUseCase,
            logoutUseCase = logoutUseCase,
            getUserStoreListUseCase = getUserStoreListUseCase,
            getSelectedStoreUseCase = getSelectedStoreUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() {
        val initialState = viewModel.state
        assertThat(initialState.user).isNull()
        assertThat(initialState.userStores).isEmpty()
        assertThat(initialState.currentStore).isNull()
        assertThat(initialState.productTypes).isEmpty()
        assertThat(initialState.productCategories).isEmpty()
    }

    @Test
    fun `getCurrentUser updates state with user data`() = coroutineTestRule.testScope.runTest {
        val testUser = UserDomainModel(
            id = "1",
            firstName = "John",
            lastName = "Doe",
            email = "john@example.com"
        )

        coEvery { getCurrentUserUseCase() } returns testUser

        viewModel.refreshUser()
        advanceUntilIdle()

        assertThat(viewModel.state.user).isEqualTo(testUser)
    }

    @Test
    fun `getUserStores updates state with store list`() = coroutineTestRule.testScope.runTest {
        val testStores = listOf(
            StoreDomainModel(id = "1", name = "Store 1"),
            StoreDomainModel(id = "2", name = "Store 2")
        )

        coEvery { getUserStoreListUseCase() } returns flow { emit(testStores) }

        viewModel.initiate()
        advanceUntilIdle()

        assertThat(viewModel.state.userStores).isEqualTo(testStores)
    }

    @Test
    fun `getCurrentStore updates state with store data and transforms types and categories`() = coroutineTestRule.testScope.runTest {
        val testStore = StoreDomainModel(
            id = "1",
            name = "Test Store",
            productTypes = listOf("type1", "type2"),
            categories = listOf(
                ProductCategoryDomainModel(id = "cat1", name = "Category 1", storeId = "123", description = ""),
                ProductCategoryDomainModel(id = "cat2", name = "Category 2", storeId = "123", description = "")
            )
        )

        coEvery { getSelectedStoreUseCase() } returns flow { emit(testStore) }

        viewModel.initiate()
        advanceUntilIdle()

        assertThat(viewModel.state.currentStore).isEqualTo(testStore)
        assertThat(viewModel.state.productTypes).containsExactly(
            KeyValueModel("type1", "Type1"),
            KeyValueModel("type2", "Type2")
        )
        assertThat(viewModel.state.productCategories).containsExactly(
            KeyValueModel("cat1", "Category 1"),
            KeyValueModel("cat2", "Category 2")
        )
    }

    @Test
    fun `logout calls logout use case`() = coroutineTestRule.testScope.runTest {
        coEvery { logoutUseCase() } returns Unit

        viewModel.logout()
        advanceUntilIdle()

        coVerify { logoutUseCase() }
    }
} 
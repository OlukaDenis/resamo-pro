
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.Resource
import com.dennytech.domain.repository.SalesRepository
import com.dennytech.domain.repository.UtilRepository
import com.dennytech.domain.usecases.sales.ConfirmSaleUseCase
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateSaleUseCaseTest {

    private lateinit var mockDispatcher: AppDispatcher
    private lateinit var mockProductRepository: SalesRepository
    private lateinit var mockUtilRepository: UtilRepository
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var useCase: ConfirmSaleUseCase


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        Dispatchers.setMain(testDispatcher)
        mockDispatcher = mockk()
        mockProductRepository = mockk()
        mockUtilRepository = mockk()
        every { mockDispatcher.io } returns  testDispatcher
        useCase = ConfirmSaleUseCase(mockDispatcher, mockUtilRepository, mockProductRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `should emit loading then success when correct params are provided`() = runTest {
        // Given
        val params = ConfirmSaleUseCase.Param(
            saleId = "test1"
        )

        coEvery { mockProductRepository.confirmSale(any()) } returns 200

        // When
        val result = useCase.invoke(params).toList()

        // Then
        Truth.assertThat(result[0]).isEqualTo(Resource.Loading)
        Truth.assertThat(result[1]).isEqualTo(Resource.Success(200))
        coVerify { mockProductRepository.confirmSale(any()) }
    }

    @Test(expected = Exception::class)
    fun `should throw any error when no params are provided`() = runTest {
        // Given

        // When
        useCase.invoke(null).toList()

        // Then
    }

}
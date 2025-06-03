package com.interview.mishi.pay.paymentapp.domain.usecase

import com.interview.mishi.pay.paymentapp.domain.model.CartItem
import com.interview.mishi.pay.paymentapp.domain.repository.CartRepository
import com.interview.mishi.pay.paymentapp.utils.AppError
import com.interview.mishi.pay.paymentapp.utils.ResponseState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CartUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var repository: CartRepository
    private lateinit var useCase: CartUseCase
    private val mockCartItem =
        CartItem(id = 1, name = "Product 1", desc = "Product", image = "icon1.png", price = 100.0)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        useCase = CartUseCase(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `getCartItems should delegate to repository and emit success`() = testScope.runTest {
        // Arrange
        val mockCartItems = listOf(
            CartItem(
                id = 1,
                name = "Product 1",
                desc = "Product",
                image = "icon1.png",
                price = 100.0
            ),
            CartItem(
                id = 2,
                name = "Product 2",
                desc = "Product",
                image = "icon2.png",
                price = 80.0
            )
        )

        coEvery { repository.getCartItems() } returns flowOf(ResponseState.Success(mockCartItems))

        val emittedStates = useCase.getCartItems().first()

        // Assert
        assertTrue(emittedStates is ResponseState.Success)

        val successState = emittedStates as ResponseState.Success
        val updatedItems = successState.data
        assertEquals(2, updatedItems.size)
        assertEquals(100.0, updatedItems[0].price)
        assertEquals(1, updatedItems[0].id)
        assertEquals(80.0, updatedItems[1].price)
        assertEquals(2, updatedItems[1].id)

        coVerify { repository.getCartItems() }
    }

    @Test
    fun `getCartItems should emit error on repository failure`() = testScope.runTest {
        // Arrange
        coEvery { repository.getCartItems() } returns flowOf(ResponseState.Error(AppError.GeneralError))

        // Act
        val result = useCase.getCartItems()

        // Assert
        val emittedState = result.first()
        assertTrue(emittedState is ResponseState.Error)
        val error = (emittedState as ResponseState.Error).e
        assertEquals(AppError.GENERAL_ERROR, error?.message)
    }

    @Test
    fun `addCartItem should delegate to repository and emit success`() = testScope.runTest {
        // Arrange
        coEvery { repository.addCartItem(any()) } returns flowOf(ResponseState.Success(Unit))

        // Act
        val result = useCase.addCartItem(mockCartItem)

        // Assert
        val emittedState = result.first()
        assertTrue(emittedState is ResponseState.Success)

        coVerify { repository.addCartItem(mockCartItem) }
    }

    @Test
    fun `addCartItem should emit error on repository failure`() = testScope.runTest {
        // Arrange
        coEvery { repository.addCartItem(any()) } returns flowOf(ResponseState.Error(AppError.GeneralError))

        // Act
        val result = useCase.addCartItem(mockCartItem)

        // Assert
        val emittedState = result.first()
        assertTrue(emittedState is ResponseState.Error)
        val error = (emittedState as ResponseState.Error).e
        assertEquals(AppError.GENERAL_ERROR, error?.message)
    }

    @Test
    fun `updateCartItem should delegate to repository and emit success`() = testScope.runTest {
        // Arrange
        coEvery { repository.updateCartItem(any()) } returns flowOf(ResponseState.Success(Unit))

        // Act
        val result = useCase.updateCartItem(mockCartItem)

        // Assert
        val emittedState = result.first()
        assertTrue(emittedState is ResponseState.Success)

        coVerify { repository.updateCartItem(mockCartItem) }
    }

    @Test
    fun `updateCartItem should emit error on repository failure`() = testScope.runTest {
        // Arrange
        coEvery { repository.updateCartItem(any()) } returns flowOf(ResponseState.Error(AppError.GeneralError))

        // Act
        val result = useCase.updateCartItem(mockCartItem)

        // Assert
        val emittedState = result.first()
        assertTrue(emittedState is ResponseState.Error)
        val error = (emittedState as ResponseState.Error).e
        assertEquals(AppError.GENERAL_ERROR, error?.message)
    }

}
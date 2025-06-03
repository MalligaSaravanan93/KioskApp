package com.interview.mishi.pay.paymentapp.ui.cart

import com.interview.mishi.pay.paymentapp.domain.model.CartItem
import com.interview.mishi.pay.paymentapp.domain.usecase.CartUseCase
import com.interview.mishi.pay.paymentapp.domain.usecase.OrdersUseCase
import com.interview.mishi.pay.paymentapp.utils.AppError
import com.interview.mishi.pay.paymentapp.utils.ResponseState
import com.interview.mishi.pay.paymentapp.utils.UIState
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
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
class CartViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var viewModel: CartViewModel
    private lateinit var cartUseCase: CartUseCase
    private lateinit var ordersUseCase: OrdersUseCase
    private val mockCartItem =
        CartItem(id = 1, name = "Product 1", desc = "Product", image = "icon1.png", price = 100.0)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher) // Initialize Main dispatcher
        cartUseCase = mockk()
        ordersUseCase = mockk()
        viewModel = CartViewModel(cartUseCase, ordersUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset Main dispatcher after test
        unmockkAll() // Unmock all mockk objects
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

        coEvery { cartUseCase.getCartItems() } returns flowOf(ResponseState.Success(mockCartItems))

        val emittedStates = mutableListOf<UIState<List<CartItem>>>()

        // Act
        val job = launch {
            viewModel.cartItems.collect { emittedStates.add(it) }
        }

        viewModel.getCartItems()
        testDispatcher.scheduler.advanceUntilIdle() // Ensure all tasks complete

        // Assert
        assertEquals(2, emittedStates.size) // Expect Init and Success states
        assertEquals(UIState.Loading, emittedStates[0])
        assertTrue(emittedStates[1] is UIState.Success)

        val successState = emittedStates[1] as UIState.Success
        assertEquals(mockCartItems, successState.data)

        // Cancel flow collection
        job.cancel()
    }

    @Test
    fun `getCartItems should emit error on repository failure`() = testScope.runTest {
        // Arrange
        coEvery { cartUseCase.getCartItems() } returns flowOf(ResponseState.Error(AppError.GeneralError))

        val emittedStates = mutableListOf<UIState<List<CartItem>>>()

        // Act
        val job = launch {
            viewModel.cartItems.collect { emittedStates.add(it) }
        }

        viewModel.getCartItems()
        testDispatcher.scheduler.advanceUntilIdle() // Ensure all tasks complete

        // Assert
        assertEquals(2, emittedStates.size) // Expect Init and Success states
        assertEquals(UIState.Loading, emittedStates[0])
        assertTrue(emittedStates[1] is UIState.Error)

        // Cancel flow collection
        job.cancel()
    }

    @Test
    fun `createOrder should emit Init and Success states`() = testScope.runTest {
        coEvery { ordersUseCase.createOrder(any()) } returns flow {
            emit(ResponseState.Success(Unit))
        }

        val emittedStates = mutableListOf<UIState<Unit>>()

        // Act
        val job = launch {
            viewModel.updateCartItem.collect { emittedStates.add(it) }
        }

        viewModel.createOrder(listOf(mockCartItem))
        testDispatcher.scheduler.advanceUntilIdle() // Ensure all tasks complete

        // Assert
        assertEquals(2, emittedStates.size) // Expect Init and Success states
        assertEquals(UIState.Loading, emittedStates[0])
        assertTrue(emittedStates[1] is UIState.Success)

        val successState = emittedStates[1] as UIState.Success
        assertEquals(Unit, successState.data)

        // Cancel flow collection
        job.cancel()
    }

    @Test
    fun `updateQuantity should emit Init and Error states`() = testScope.runTest {
        // Arrange
        val mockError = AppError.GeneralError
        coEvery { cartUseCase.updateCartItem(any()) } returns flow {
            emit(ResponseState.Error(mockError))
        }

        // Act
        viewModel.updateQuantity(mockCartItem)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(UIState.Error(mockError.message ?: ""), viewModel.updateCartItem.value)
    }

    @Test
    fun `updateQuantity should emit Init and Success states`() = testScope.runTest {
        coEvery { cartUseCase.updateCartItem(mockCartItem) } returns flow {
            emit(ResponseState.Success(Unit))
        }

        val emittedStates = mutableListOf<UIState<Unit>>()

        // Act
        val job = launch {
            viewModel.updateCartItem.collect { emittedStates.add(it) }
        }

        viewModel.updateQuantity(mockCartItem)
        testDispatcher.scheduler.advanceUntilIdle() // Ensure all tasks complete

        // Assert
        assertEquals(2, emittedStates.size) // Expect Init and Success states
        assertEquals(UIState.Loading, emittedStates[0])
        assertTrue(emittedStates[1] is UIState.Success)

        val successState = emittedStates[1] as UIState.Success
        assertEquals(Unit, successState.data)

        // Act
        viewModel.resetUpdateItem()
        // Assert
        assertEquals(UIState.Init, viewModel.updateCartItem.value)

        // Cancel flow collection
        job.cancel()
    }

    @Test
    fun `createOrder should emit Init and Error states`() = testScope.runTest {
        // Arrange
        val mockError = AppError.GeneralError
        coEvery { ordersUseCase.createOrder(any()) } returns flow {
            emit(ResponseState.Error(mockError))
        }

        // Act
        viewModel.createOrder(listOf(mockCartItem))
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(UIState.Error(mockError.message ?: ""), viewModel.updateCartItem.value)
    }

    @Test
    fun `reset and loading should emit Loading and Init state`() {
        // Act
        viewModel.loading()
        // Assert
        assertEquals(UIState.Loading, viewModel.cartItems.value)

        // Act
        viewModel.resetCartItems()
        // Assert
        assertEquals(UIState.Init, viewModel.cartItems.value)
    }
}
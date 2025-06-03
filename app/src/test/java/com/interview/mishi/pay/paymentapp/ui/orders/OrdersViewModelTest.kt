package com.interview.mishi.pay.paymentapp.ui.orders

import com.interview.mishi.pay.paymentapp.domain.model.CartItem
import com.interview.mishi.pay.paymentapp.domain.model.OrderItem
import com.interview.mishi.pay.paymentapp.domain.usecase.OrdersUseCase
import com.interview.mishi.pay.paymentapp.utils.AppError
import com.interview.mishi.pay.paymentapp.utils.ResponseState
import com.interview.mishi.pay.paymentapp.utils.UIState
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class OrdersViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var viewModel: OrdersViewModel
    private lateinit var useCase: OrdersUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher) // Initialize Main dispatcher
        useCase = mockk()
        viewModel = OrdersViewModel(useCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset Main dispatcher after test
        unmockkAll() // Unmock all mockk objects
    }

    @Test
    fun `getOrders should delegate to repository and emit success`() = testScope.runTest {
        // Arrange
        val mockOrderItems = listOf(
            OrderItem(
                invoiceNo = "123456", itemsList = listOf(
                    CartItem(
                        id = 1,
                        name = "Product 1",
                        desc = "Product",
                        image = "icon1.png",
                        price = 100.0
                    )
                )
            ),
            OrderItem(
                invoiceNo = "654321", itemsList = listOf(
                    CartItem(
                        id = 1,
                        name = "Product 2",
                        desc = "Product",
                        image = "icon2.png",
                        price = 80.0
                    )
                )
            )
        )

        coEvery { useCase.getOrders() } returns flowOf(ResponseState.Success(mockOrderItems))

        val emittedStates = mutableListOf<UIState<List<OrderItem>>>()

        // Act
        val job = launch {
            viewModel.orderItems.collect { emittedStates.add(it) }
        }

        viewModel.getOrders()
        testDispatcher.scheduler.advanceUntilIdle() // Ensure all tasks complete

        // Assert
        assertEquals(2, emittedStates.size) // Expect Init and Success states
        assertEquals(UIState.Loading, emittedStates[0])
        assertTrue(emittedStates[1] is UIState.Success)

        val successState = emittedStates[1] as UIState.Success
        assertEquals(mockOrderItems, successState.data)

        // Cancel flow collection
        job.cancel()
    }

    @Test
    fun `getCartItems should emit error on repository failure`() = testScope.runTest {
        // Arrange
        coEvery { useCase.getOrders() } returns flowOf(ResponseState.Error(AppError.GeneralError))

        val emittedStates = mutableListOf<UIState<List<OrderItem>>>()

        // Act
        val job = launch {
            viewModel.orderItems.collect { emittedStates.add(it) }
        }

        viewModel.getOrders()
        testDispatcher.scheduler.advanceUntilIdle() // Ensure all tasks complete

        // Assert
        assertEquals(2, emittedStates.size) // Expect Init and Success states
        assertEquals(UIState.Loading, emittedStates[0])
        assertTrue(emittedStates[1] is UIState.Error)

        // Cancel flow collection
        job.cancel()
    }

    @Test
    fun `reset and loading should emit Loading and Init state`() {
        // Act
        viewModel.loading()
        // Assert
        assertEquals(UIState.Loading, viewModel.orderItems.value)

        // Act
        viewModel.reset()
        // Assert
        assertEquals(UIState.Init, viewModel.orderItems.value)
    }
}
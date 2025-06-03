package com.interview.mishi.pay.paymentapp.domain.usecase

import com.interview.mishi.pay.paymentapp.domain.model.CartItem
import com.interview.mishi.pay.paymentapp.domain.model.OrderItem
import com.interview.mishi.pay.paymentapp.domain.repository.OrdersRepository
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
class OrderUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var repository: OrdersRepository
    private lateinit var useCase: OrdersUseCase
    private val mockOrderItem =
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
        )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        useCase = OrdersUseCase(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `getOrders should delegate to repository and emit success`() = testScope.runTest {
        // Arrange
        val mockOrderItems = listOf(
            mockOrderItem,
            OrderItem(
                invoiceNo = "654321", itemsList = listOf(
                    CartItem(
                        id = 1,
                        name = "Product 1",
                        desc = "Product",
                        image = "icon1.png",
                        price = 100.0
                    )
                )
            )
        )

        coEvery { repository.getOrders() } returns flowOf(ResponseState.Success(mockOrderItems))

        val emittedStates = useCase.getOrders().first()

        // Assert
        assertTrue(emittedStates is ResponseState.Success)

        val successState = emittedStates as ResponseState.Success
        val updatedItems = successState.data
        assertEquals(2, updatedItems.size)
        assertEquals(1, updatedItems[0].itemsList?.size)
        assertEquals("123456", updatedItems[0].invoiceNo)
        assertEquals(1, updatedItems[1].itemsList?.size)
        assertEquals("654321", updatedItems[1].invoiceNo)

        coVerify { repository.getOrders() }
    }

    @Test
    fun `getOrders should emit error on repository failure`() = testScope.runTest {
        // Arrange
        coEvery { repository.getOrders() } returns flowOf(ResponseState.Error(AppError.GeneralError))

        // Act
        val result = useCase.getOrders()

        // Assert
        val emittedState = result.first()
        assertTrue(emittedState is ResponseState.Error)
        val error = (emittedState as ResponseState.Error).e
        assertEquals(AppError.GENERAL_ERROR, error?.message)
    }

    @Test
    fun `createOrder should delegate to repository and emit success`() = testScope.runTest {
        // Arrange
        coEvery { repository.createOrder(any()) } returns flowOf(ResponseState.Success(Unit))

        // Act
        val result = useCase.createOrder(mockOrderItem)

        // Assert
        val emittedState = result.first()
        assertTrue(emittedState is ResponseState.Success)

        coVerify { repository.createOrder(mockOrderItem) }
    }

    @Test
    fun `createOrder should emit error on repository failure`() = testScope.runTest {
        // Arrange
        coEvery { repository.createOrder(any()) } returns flowOf(ResponseState.Error(AppError.GeneralError))

        // Act
        val result = useCase.createOrder(mockOrderItem)

        // Assert
        val emittedState = result.first()
        assertTrue(emittedState is ResponseState.Error)
        val error = (emittedState as ResponseState.Error).e
        assertEquals(AppError.GENERAL_ERROR, error?.message)
    }

}
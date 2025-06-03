package com.interview.mishi.pay.paymentapp.ui.scan

import com.interview.mishi.pay.paymentapp.domain.model.CartItem
import com.interview.mishi.pay.paymentapp.domain.usecase.CartUseCase
import com.interview.mishi.pay.paymentapp.utils.AppError
import com.interview.mishi.pay.paymentapp.utils.ResponseState
import com.interview.mishi.pay.paymentapp.utils.UIState
import com.interview.mishi.pay.paymentapp.utils.value
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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
class ScanViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var viewModel: ScanViewModel
    private lateinit var useCase: CartUseCase
    private val mockCartItem =
        CartItem(id = 1, name = "Product 1", desc = "Product", image = "icon1.png", price = 100.0)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher) // Initialize Main dispatcher
        useCase = mockk()
        viewModel = ScanViewModel(useCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset Main dispatcher after test
        unmockkAll() // Unmock all mockk objects
    }

    @Test
    fun `addCartItem should emit Init and Success states`() = testScope.runTest {
        coEvery { useCase.addCartItem(mockCartItem) } returns flow {
            emit(ResponseState.Success(Unit))
        }

        val emittedStates = mutableListOf<UIState<Unit>>()

        // Act
        val job = launch {
            viewModel.addCartItem.collect { emittedStates.add(it) }
        }

        viewModel.addCartItem(mockCartItem)
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
    fun `addCartItem should emit Init and Error states`() = testScope.runTest {
        // Arrange
        val mockError = AppError.GeneralError
        coEvery { useCase.addCartItem(any()) } returns flow {
            emit(ResponseState.Error(mockError))
        }

        // Act
        viewModel.addCartItem(mockCartItem)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(UIState.Error(mockError.message ?: ""), viewModel.addCartItem.value)
    }

    @Test
    fun `addQuantity should increase scanned product quantity `() {

        viewModel.scannedProduct.value = mockCartItem.copy(quantity = 10)

        // Act
        viewModel.addQuantity()

        // Assert
        assertEquals(11, viewModel.scannedProduct.value?.quantity.value())
    }

    @Test
    fun `decreaseQuantity should decrease scanned product quantity `() {
        viewModel.scannedProduct.value = mockCartItem.copy(quantity = 0)

        // Act
        viewModel.decreaseQuantity()

        // Assert
        assertEquals(0, viewModel.scannedProduct.value?.quantity.value())

        viewModel.scannedProduct.value = mockCartItem.copy(quantity = 10)

        // Act
        viewModel.decreaseQuantity()

        // Assert
        assertEquals(9, viewModel.scannedProduct.value?.quantity.value())
    }

    @Test
    fun `reset and loading should emit Loading and Init state`() {
        // Act
        viewModel.loading()
        // Assert
        assertEquals(UIState.Loading, viewModel.addCartItem.value)

        // Act
        viewModel.reset()
        // Assert
        assertEquals(UIState.Init, viewModel.addCartItem.value)
    }
}
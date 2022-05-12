package com.imadev.foody.ui.checkout

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.imadev.foody.model.Meal
import com.imadev.foody.repository.FoodyRepoImpTest
import com.imadev.foody.utils.getOrAwaitValue
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class CheckoutViewModelTest {


    private lateinit var viewModel:CheckoutViewModel


    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        viewModel = CheckoutViewModel(FoodyRepoImpTest())
    }


    @Test
    fun `sum of the cart should be 0 if all quantities equal zero`() {
        viewModel.addToCart(Meal(price = 10.0, quantity = 0))
        viewModel.addToCart(Meal(price = 10.0, quantity = 0))
        viewModel.addToCart(Meal(price = 10.0, quantity = 0))

        val total = viewModel.getTotal()
        assertEquals("0", total)
    }


    @Test
    fun `should not calculate the sum if cartList is empty`() {
        viewModel.resetList()
        val total = viewModel.getTotal()
        assertEquals("0", total)
    }





    @Test
    fun calculateSum() {
        viewModel.addToCart(Meal(price = 10.0, quantity = 1))
        viewModel.addToCart(Meal(price = 10.5, quantity = 1))
        viewModel.addToCart(Meal(price = 10.0, quantity = 2))

        val total = viewModel.getTotal()
        assertEquals("40.5", total)
    }


    @Test
    fun removeFromCart() {

        val firstMeal = Meal(price = 10.0, quantity = 1)
        val secondMeal = Meal(price = 10.0, quantity = 1)
        val thirdMeal = Meal(price = 10.0, quantity = 2)

        viewModel.addToCart(firstMeal)
        viewModel.addToCart(secondMeal)
        viewModel.addToCart(thirdMeal)

        viewModel.removeFromCart(thirdMeal)

        val currentList = listOf(firstMeal,secondMeal)

        assertThat(viewModel.cartList).isEqualTo(currentList)

    }


    @Test
    fun canProceedToPayment() {

        val firstMeal = Meal(price = 10.0, quantity = 1)
        val secondMeal = Meal(price = 10.0, quantity = 1)
        val thirdMeal = Meal(price = 10.0, quantity = 2)

        val forthMeal = Meal(price = 10.0, quantity = 0)

        viewModel.addToCart(firstMeal)
        viewModel.addToCart(secondMeal)
        viewModel.addToCart(thirdMeal)


        viewModel.observeQuantity()

        var canProceed = viewModel.canProceedToPayment.getOrAwaitValue()

        assertThat(canProceed).isTrue()


        viewModel.addToCart(forthMeal)
        viewModel.observeQuantity()
        canProceed = viewModel.canProceedToPayment.getOrAwaitValue()


        assertThat(canProceed).isFalse()

    }

}

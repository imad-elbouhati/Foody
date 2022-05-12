package com.imadev.foody.ui.checkout

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.imadev.foody.model.Food
import com.imadev.foody.model.Meal
import com.imadev.foody.repository.FoodyRepoImpTest
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

}

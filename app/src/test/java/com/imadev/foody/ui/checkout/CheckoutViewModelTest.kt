package com.imadev.foody.ui.checkout

import com.imadev.foody.model.Food
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CheckoutViewModelTest {

    private var cartList = mutableListOf<Food>()

    private lateinit var viewModel:CheckoutViewModel

    @Before
    fun setup() {
        viewModel = CheckoutViewModel()
    }


    @Test
    fun `sum of the cart should be 0 if all quantities equal zero`() {
        cartList.add(Food(price = 10.0, quantity = 0))
        cartList.add(Food(price = 10.0, quantity = 0))
        cartList.add(Food(price = 10.0, quantity = 0))

        val total = cartList.sumOf { it.quantity * it.price }
        assertEquals(0.0, total)
    }


    @Test
    fun `should not calculate the sum if cartList is empty`() {
        cartList = mutableListOf()
        val total = cartList.sumOf { it.quantity * it.price }
        assertEquals(0.0, total)
    }


    @Test
    fun `should not calculate the sum if cartList is no empty`() {
        val cartList = mutableListOf<Food>()
        val total = cartList.sumOf { it.quantity * it.price }
        assertEquals(0.0, total)
    }


    @Test
    fun calculateSum() {
        cartList.add(Food(price = 10.0, quantity = 1))
        cartList.add(Food(price = 10.5, quantity = 1))
        cartList.add(Food(price = 10.0, quantity = 2))

        val total = cartList.sumOf { it.quantity * it.price }
        assertEquals(40.5, total)
    }

}

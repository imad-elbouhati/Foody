package com.imadev.foody.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.imadev.foody.repository.FoodyRepo
import com.imadev.foody.repository.FoodyRepoImpTest
import com.imadev.foody.utils.MainCoroutineScopeRule
import com.imadev.foody.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.rules.TestRule

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
    private lateinit var repo: FoodyRepoImpTest


    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get: Rule
    val mainCoroutineScopeRule: MainCoroutineScopeRule = MainCoroutineScopeRule()




    @Before
    fun setUp() {
        repo = FoodyRepoImpTest()
        viewModel = HomeViewModel(repo)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `should return success when the call is successful`() {

        mainCoroutineScopeRule.runBlockingTest {
            Dispatchers.setMain(StandardTestDispatcher())

            viewModel.getCategories()

            viewModel.categories.test {
                val item = awaitItem()
                assertThat(item).isInstanceOf(Resource.Success::class.java)
            }
        }

    }


    @Test
    fun `should return Error when the call isn't successful`() {

        repo.setShouldReturnError(true)
        viewModel = HomeViewModel(repo)

        mainCoroutineScopeRule.runBlockingTest {

            Dispatchers.setMain(StandardTestDispatcher())


            viewModel.getCategories()

            viewModel.categories.test {

                val item = awaitItem()
                println("AAaA ${item.data}")
                assertThat(item).isInstanceOf(Resource.Error::class.java)
            }
        }

    }

    @Test
    fun getMeals() {
    }

    @Test
    fun getMealsByCategory() {
    }

    @Test
    fun getRepository() {
    }
}
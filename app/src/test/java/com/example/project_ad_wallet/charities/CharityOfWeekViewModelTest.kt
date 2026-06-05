package com.example.project_ad_wallet.charities

import com.example.project_ad_wallet.MainCoroutineRule
import com.example.project_ad_wallet.data.local.dao.CharityOfWeekDao
import com.example.project_ad_wallet.data.local.entities.CharityOfWeekEntity
import com.example.project_ad_wallet.data.repository.CharityOfWeekRepository
import com.example.project_ad_wallet.ui.charities.CharityOfWeekViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private object DummyDao : CharityOfWeekDao {
    override fun getCharityOfWeek() = kotlinx.coroutines.flow.flowOf(null)
    override suspend fun upsertCharityOfWeek(charity: CharityOfWeekEntity) {}
    override suspend fun addDonation(amount: Double) {}
}

class FakeCharityOfWeekRepository : CharityOfWeekRepository(DummyDao) {
    private val _charityOfWeek = MutableStateFlow<CharityOfWeekEntity?>(null)
    var lastDonatedAmount: Double = 0.0

    override fun getCharityOfWeek(): Flow<CharityOfWeekEntity?> = _charityOfWeek

    override suspend fun updateCharityOfWeek(charity: CharityOfWeekEntity) {
        _charityOfWeek.value = charity
    }

    override suspend fun addDonation(amount: Double) {
        lastDonatedAmount = amount
        _charityOfWeek.value = _charityOfWeek.value?.let {
            it.copy(totalDonatedThisWeek = it.totalDonatedThisWeek + amount)
        }
    }

    override suspend fun initializeIfEmpty() {
        if (_charityOfWeek.value == null) {
            _charityOfWeek.value = CharityOfWeekEntity(
                charityId = "unicef",
                charityName = "UNICEF",
                charityDescription = "Protege os direitos das crianças em todo o mundo.",
                sdgNumber = 3,
                sdgDescription = "Boa Saúde e Bem-Estar",
                logoRes = "ic_unicef",
                weekStartTimestamp = System.currentTimeMillis()
            )
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class CharityOfWeekViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val fakeRepository = FakeCharityOfWeekRepository()
    private lateinit var viewModel: CharityOfWeekViewModel

    @Before
    fun setup() {
        viewModel = CharityOfWeekViewModel(fakeRepository)
    }

    @Test
    fun initialState_charityOfWeek_isNull() = runTest {
        assertNull(viewModel.charityOfWeek.value)
    }

    @Test
    fun afterInit_charityOfWeek_isNotNull() = runTest {
        val collectJob = launch(kotlinx.coroutines.test.UnconfinedTestDispatcher(testScheduler)) {
            viewModel.charityOfWeek.collect {}
        }
        advanceUntilIdle()
        assertNotNull(viewModel.charityOfWeek.value)
        collectJob.cancel()
    }

    @Test
    fun donate_callsRepository() = runTest {
        viewModel.donate(2.00) {}
        advanceUntilIdle()
        assertEquals(2.00, fakeRepository.lastDonatedAmount, 0.001)
    }

    @Test
    fun donate_invokesOnSuccess() = runTest {
        var successCalled = false
        viewModel.donate(1.00) { successCalled = true }
        advanceUntilIdle()
        assertTrue(successCalled)
    }
}

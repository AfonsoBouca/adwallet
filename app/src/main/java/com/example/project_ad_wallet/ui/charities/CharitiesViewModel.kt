package com.example.project_ad_wallet.ui.charities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_ad_wallet.data.model.Charity
import com.example.project_ad_wallet.data.model.CharityStatus
import com.example.project_ad_wallet.data.model.Donation
import com.example.project_ad_wallet.data.repository.CharityRepository
import com.example.project_ad_wallet.data.repository.DonationRepository
import com.example.project_ad_wallet.data.repository.WalletRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.util.UUID

data class CharitiesUiState(
    val charities: List<Charity> = emptyList(),
    val balance: Double = 0.0,
    val selectedCharity: Charity? = null,
    val donationAmount: String = "",
    val showConfirmDialog: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val confirmedDonation: Pair<String, Double>? = null,
    val charityDonations: Map<String, Double> = emptyMap(),
)

class CharitiesViewModel(
    private val charityRepository: CharityRepository = CharityRepository.instance,
    private val walletRepository: WalletRepository = WalletRepository.instance,
    private val donationRepository: DonationRepository = DonationRepository.instance,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharitiesUiState())
    val uiState: StateFlow<CharitiesUiState> = _uiState.asStateFlow()

    init {
        combine(
            charityRepository.charities,
            walletRepository.balance,
            donationRepository.donations,
        ) { charities, balance, donations ->
            val verifiedCharities = charities.filter { it.status == CharityStatus.VERIFIED }
            val donationTotals = donations.groupBy { it.charityId }
                .mapValues { entry -> entry.value.sumOf { it.amount } }
            _uiState.update {
                it.copy(
                    charities = verifiedCharities,
                    balance = balance,
                    charityDonations = donationTotals
                )
            }
        }.launchIn(viewModelScope)
    }

    fun selectCharity(charity: Charity) {
        _uiState.update { it.copy(selectedCharity = charity, donationAmount = "", errorMessage = null) }
    }

    fun dismissCharity() {
        _uiState.update { it.copy(selectedCharity = null, donationAmount = "", errorMessage = null) }
    }

    fun onAmountChanged(value: String) {
        _uiState.update { it.copy(donationAmount = value) }
    }

    fun requestDonation() {
        val state = _uiState.value
        val amount = state.donationAmount.toDoubleOrNull()
        if (amount == null || amount <= 0.0) {
            _uiState.update { it.copy(errorMessage = "Please enter a valid amount.") }
            return
        }
        if (amount > state.balance) {
            _uiState.update { it.copy(errorMessage = "Insufficient balance. Watch more ads to earn!") }
            return
        }
        _uiState.update { it.copy(showConfirmDialog = true) }
    }

    fun confirmDonation() {
        val state = _uiState.value
        val charity = state.selectedCharity ?: return
        val amount = state.donationAmount.toDoubleOrNull() ?: return

        val deducted = walletRepository.deductBalance(amount)
        if (!deducted) {
            _uiState.update {
                it.copy(
                    showConfirmDialog = false,
                    errorMessage = "Insufficient balance. Watch more ads to earn!",
                )
            }
            return
        }

        donationRepository.addDonation(
            Donation(
                id = UUID.randomUUID().toString(),
                charityId = charity.id,
                charityName = charity.name,
                amount = amount,
                sdgNumber = charity.sdgNumber,
                sdgName = charity.sdgName,
            )
        )

        _uiState.update {
            it.copy(
                showConfirmDialog = false,
                selectedCharity = null,
                donationAmount = "",
                successMessage = "Thank you for supporting ${charity.name}!",
                errorMessage = null,
                confirmedDonation = Pair(charity.id, amount),
            )
        }
    }

    fun clearConfirmedDonation() {
        _uiState.update { it.copy(confirmedDonation = null) }
    }

    fun cancelDonation() {
        _uiState.update { it.copy(showConfirmDialog = false) }
    }

    fun clearMessages() {
        _uiState.update { it.copy(successMessage = null, errorMessage = null) }
    }
}
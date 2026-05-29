package com.example.project_ad_wallet.data.repository

import com.example.project_ad_wallet.data.model.Charity
import com.example.project_ad_wallet.data.model.CharityStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CharityRepository {

    companion object {
        val instance = CharityRepository()
    }

    private val _charities = MutableStateFlow(sampleCharities())
    val charities: StateFlow<List<Charity>> = _charities.asStateFlow()

    fun getCharities(): List<Charity> = _charities.value

    fun getCharityById(id: String): Charity? = _charities.value.firstOrNull { it.id == id }

    fun approveCharity(id: String) {
        _charities.update { currentList ->
            currentList.map { charity ->
                if (charity.id == id) charity.copy(status = CharityStatus.VERIFIED) else charity
            }
        }
    }

    fun rejectCharity(id: String) {
        _charities.update { currentList ->
            currentList.map { charity ->
                if (charity.id == id) charity.copy(status = CharityStatus.REJECTED) else charity
            }
        }
    }
}

private fun sampleCharities() = listOf(
    Charity(
        id = "zero-hunger-foundation",
        name = "Zero Hunger Foundation",
        description = "Provides food to those in need across Portugal and beyond.",
        sdgNumber = 2,
        sdgName = "Zero Hunger",
        status = CharityStatus.VERIFIED,
    ),
    Charity(
        id = "green-earth",
        name = "Green Earth",
        description = "Focuses on climate action and reforestation.",
        sdgNumber = 13,
        sdgName = "Climate Action",
        status = CharityStatus.VERIFIED,
    ),
    Charity(
        id = "clean-water-initiative",
        name = "Clean Water Initiative",
        description = "Ensures access to clean water and sanitation.",
        sdgNumber = 6,
        sdgName = "Clean Water & Sanitation",
        status = CharityStatus.VERIFIED,
    ),
    Charity(
        id = "quality-education-fund",
        name = "Quality Education Fund",
        description = "Supports inclusive and equitable education for all.",
        sdgNumber = 4,
        sdgName = "Quality Education",
        status = CharityStatus.VERIFIED,
    ),
    Charity(
        id = "good-health-alliance",
        name = "Good Health Alliance",
        description = "Promotes well-being and access to healthcare.",
        sdgNumber = 3,
        sdgName = "Good Health & Well-being",
        status = CharityStatus.VERIFIED,
    ),
    Charity(
        id = "scam-foundation",
        name = "Totally Real Foundation",
        description = "We definitely give all our money to penguins.",
        sdgNumber = 14,
        sdgName = "Life Below Water",
        status = CharityStatus.PENDING,
    ),
)
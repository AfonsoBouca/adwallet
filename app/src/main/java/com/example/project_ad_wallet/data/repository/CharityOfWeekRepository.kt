package com.example.project_ad_wallet.data.repository

import com.example.project_ad_wallet.data.local.dao.CharityOfWeekDao
import com.example.project_ad_wallet.data.local.entities.CharityOfWeekEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class CharityOfWeekRepository @Inject constructor(
    private val charityOfWeekDao: CharityOfWeekDao
) {
    open fun getCharityOfWeek(): Flow<CharityOfWeekEntity?> =
        charityOfWeekDao.getCharityOfWeek()

    open suspend fun updateCharityOfWeek(charity: CharityOfWeekEntity) {
        charityOfWeekDao.upsertCharityOfWeek(charity)
    }

    open suspend fun addDonation(amount: Double) {
        charityOfWeekDao.addDonation(amount)
    }

    open suspend fun initializeIfEmpty() {
        // Inicializa com uma charity por defeito se não existir nenhuma
        val current = charityOfWeekDao.getCharityOfWeek().first()
        if (current == null) {
            val defaultCharity = CharityOfWeekEntity(
                charityId = "unicef",
                charityName = "UNICEF",
                charityDescription = "Protege os direitos das crianças em todo o mundo.",
                sdgNumber = 3,
                sdgDescription = "Boa Saúde e Bem-Estar",
                logoRes = "ic_unicef",
                weekStartTimestamp = System.currentTimeMillis()
            )
            charityOfWeekDao.upsertCharityOfWeek(defaultCharity)
        }
    }
}

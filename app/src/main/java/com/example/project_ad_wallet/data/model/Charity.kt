package com.example.project_ad_wallet.data.model

enum class CharityStatus { PENDING, VERIFIED, REJECTED }

data class Charity(
    val id: String,
    val name: String,
    val description: String,
    val sdgNumber: Int,
    val sdgName: String,
    val status: CharityStatus = CharityStatus.VERIFIED, // Definimos VERIFIED por defeito para as antigas funcionarem
)
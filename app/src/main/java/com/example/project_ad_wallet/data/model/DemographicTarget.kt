package com.example.project_ad_wallet.data.model

data class DemographicTarget(
    val minAge: Int? = null,          // idade mínima (null = sem restrição)
    val maxAge: Int? = null,          // idade máxima (null = sem restrição)
    val targetGenders: List<String> = emptyList(), // ["Male","Female","Other"] vazio = todos
    val targetRegions: List<String> = emptyList()  // ["PT","BR","EN"] vazio = todos
)

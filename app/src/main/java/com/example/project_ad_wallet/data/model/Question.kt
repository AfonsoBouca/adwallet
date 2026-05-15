package com.example.project_ad_wallet.data.model

data class QuestionOption(
    val text: String,
    val isCorrect: Boolean,
)

data class Question(
    val text: String,
    val options: List<QuestionOption>,
)

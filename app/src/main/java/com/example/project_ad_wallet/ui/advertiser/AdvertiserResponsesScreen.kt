package com.example.project_ad_wallet.ui.advertiser

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.project_ad_wallet.data.model.QuestionnaireResponse
import com.example.project_ad_wallet.data.repository.AdRepository

import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AdvertiserResponsesScreen(
    viewModel: AdvertiserResponsesViewModel = hiltViewModel(),
) {
    val responses by viewModel.responses.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Text("Questionnaire Responses", modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleMedium)

        LazyColumn(modifier = Modifier.fillMaxSize(), content = {
            items(responses) { resp: QuestionnaireResponse ->
                val adTitle = viewModel.getAdById(resp.adId)?.title ?: "Unknown Ad"
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .testTag("response_item_${resp.id}")) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(text = adTitle)
                        Text(text = "Answers: ${resp.answers.joinToString(", ")}")
                    }
                }
            }
        })
    }
}

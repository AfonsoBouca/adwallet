package com.example.project_ad_wallet.ui.navigation

object Routes {
    const val REGISTRATION = "registration"
    const val AD_FEED = "feed"
    const val CHARITIES = "charities"
    const val SETTINGS = "settings"
    const val WALLET = "wallet"
    const val AD_WATCH = "ad_watch/{adId}"
    const val AD_RATING = "ad_rating/{adId}"
    const val ADVERTISER_UPLOAD = "advertiser_upload"
    const val ADVERTISER_RESPONSES = "advertiser_responses"
    const val EARNINGS_HISTORY = "earnings_history"
    const val DONATION_CONFIRMATION = "donation_confirmation/{charityId}/{amount}"
    const val DONATION_HISTORY = "donation_history"
    const val NOTIFICATION_PREFS = "notification_prefs"
    const val WITHDRAWAL_STATUS = "withdrawal_status"
    const val ADMIN_USERS = "admin_users"
    const val ADMIN_ADS = "admin_ads"
    const val WITHDRAWAL_REQUEST = "withdrawal_request"
    const val ADMIN_CHARITIES = "admin_charities"

    fun adWatch(adId: String) = "ad_watch/$adId"
    fun withdrawalRequest() = WITHDRAWAL_REQUEST
    fun adRating(adId: String) = "ad_rating/$adId"
    fun advertiserResponses() = ADVERTISER_RESPONSES
    fun donationConfirmation(charityId: String, amount: Double) =
        "donation_confirmation/$charityId/$amount"
}
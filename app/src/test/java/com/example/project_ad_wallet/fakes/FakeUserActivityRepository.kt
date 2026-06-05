package com.example.project_ad_wallet.fakes

import com.example.project_ad_wallet.data.repository.UserActivityRepository

class FakeUserActivityRepository(
    private val dao: FakeUserActivityDao = FakeUserActivityDao()
) : UserActivityRepository(dao)

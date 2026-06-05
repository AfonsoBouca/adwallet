package com.example.project_ad_wallet.ui.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_ad_wallet.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfilePictureViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val userProfile = userRepository.user
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun updateProfilePicture(uri: Uri) {
        viewModelScope.launch {
            userRepository.updateProfilePicture(uri.toString())
        }
    }
}

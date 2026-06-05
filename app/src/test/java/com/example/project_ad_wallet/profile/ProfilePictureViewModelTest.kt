package com.example.project_ad_wallet.profile

import android.net.Uri
import com.example.project_ad_wallet.data.model.ThemeMode
import com.example.project_ad_wallet.data.model.User
import com.example.project_ad_wallet.data.repository.UserRepository
import com.example.project_ad_wallet.ui.profile.ProfilePictureViewModel
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class ProfilePictureViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private val fakeRepository = FakeUserRepository()
    private lateinit var viewModel: ProfilePictureViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ProfilePictureViewModel(fakeRepository)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun updateProfilePicture_callsRepository() = runTest {
        val uriString = "content://media/external/images/1"
        val uri = Uri.parse(uriString)
        viewModel.updateProfilePicture(uri)
        kotlinx.coroutines.yield()
        assertEquals(uriString, fakeRepository.lastUpdatedUri)
    }

    @Test
    fun profileWithNoPicture_uriIsNull() = runTest {
        backgroundScope.launch(testDispatcher) { viewModel.userProfile.collect {} }
        val profile = viewModel.userProfile.value
        assertNull(profile?.profilePictureUri)
    }

    @Test
    fun profileAfterUpdate_hasCorrectUri() = runTest {
        backgroundScope.launch(testDispatcher) { viewModel.userProfile.collect {} }
        val uriString = "content://media/external/images/42"
        val uri = Uri.parse(uriString)
        viewModel.updateProfilePicture(uri)
        kotlinx.coroutines.yield()
        
        assertEquals(uriString, viewModel.userProfile.value?.profilePictureUri)
    }
}

class FakeUserRepository : UserRepository(null) {
    private val _user = MutableStateFlow(User(name = "Test User"))
    override val user: StateFlow<User> = _user.asStateFlow()
    
    var lastUpdatedUri: String? = null

    override fun updateProfilePicture(uriString: String) {
        lastUpdatedUri = uriString
        _user.value = _user.value.copy(profilePictureUri = uriString)
    }
}

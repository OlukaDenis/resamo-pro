package com.dennytech.resamopro.ui.screen.auth.onboarding

import androidx.lifecycle.ViewModel
import com.dennytech.domain.repository.PreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository
) :ViewModel(){

    val expiryTime = runBlocking { preferenceRepository.getTokenExpiry().first() }
}
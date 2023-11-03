package com.mahmoudibrahem.wordoftheday.presentation.composables.onboarding

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudibrahem.wordoftheday.R
import com.mahmoudibrahem.wordoftheday.core.util.Constants.NOTIFICATION_PERMISSION_REQ_CODE
import com.mahmoudibrahem.wordoftheday.domain.usecase.SaveOnboardingStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val saveOnboardingStateUseCase: SaveOnboardingStateUseCase
) : ViewModel() {

    fun onGetStartedBtnClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            saveOnboardingStateUseCase(true)
        }
    }

    private fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= 33) {
            EasyPermissions.hasPermissions(context, Manifest.permission.POST_NOTIFICATIONS)
        } else {
            true
        }
    }

    fun requestNotificationPermission(host: Activity) {
        if (Build.VERSION.SDK_INT >= 33) {
            if (
                hasNotificationPermission(host)
            ) {
                EasyPermissions.requestPermissions(
                    host,
                    host.getString(R.string.permission_rational),
                    NOTIFICATION_PERMISSION_REQ_CODE,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
        }
    }
}
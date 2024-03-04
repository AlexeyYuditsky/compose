package com.alexeyyuditsky.vkclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import com.alexeyyuditsky.vkclient.ui.ActivityResultTest
import com.alexeyyuditsky.vkclient.ui.MainScreen
import com.alexeyyuditsky.vkclient.ui.theme.VkClientTheme
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VkClientTheme {
                val launcher = rememberLauncherForActivityResult(
                    contract = VK.getVKAuthActivityResultContract()
                ) {
                    when (it) {
                        is VKAuthenticationResult.Success -> {
                            // User passed authorization
                        }

                        is VKAuthenticationResult.Failed -> {
                            // User didn't pass authorization
                        }
                    }
                }
                launcher.launch(listOf(VKScope.WALL))
                MainScreen()
            }
        }
    }
}
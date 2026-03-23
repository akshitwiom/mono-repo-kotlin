package com.monorepo.rohit.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.monorepo.core.designsystem.theme.MonoRepoTheme
import com.monorepo.rohit.navigation.RohitNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RohitActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MonoRepoTheme {
                RohitNavHost()
            }
        }
    }
}

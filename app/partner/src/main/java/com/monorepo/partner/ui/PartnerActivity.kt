package com.monorepo.partner.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.monorepo.core.designsystem.theme.MonoRepoTheme
import com.monorepo.partner.navigation.PartnerNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PartnerActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MonoRepoTheme {
                PartnerNavHost()
            }
        }
    }
}

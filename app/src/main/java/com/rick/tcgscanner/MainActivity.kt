package com.rick.tcgscanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.rick.tcgscanner.ui.MainScreen
import com.rick.tcgscanner.ui.theme.TcgScannerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TcgScannerTheme {
                MainScreen()
            }
        }
    }
}

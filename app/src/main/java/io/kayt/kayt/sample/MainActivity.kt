package io.kayt.kayt.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.kayt.kayt.sample.ui.screen.main.MainScreen
import io.kayt.kayt.sample.ui.theme.KatalysisTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KatalysisTheme {
                // MainScreenOld.UsersScreen()  <- Old approach ->
                MainScreen.UsersScreen()
            }
        }
    }
}
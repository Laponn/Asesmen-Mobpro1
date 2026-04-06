package com.naufalsulthanfakhry0092.mobpro1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.naufalsulthanfakhry0092.asesmenmobpro1.navigation.SetupNavGraph
import com.naufalsulthanfakhry0092.asesmenmobpro1.ui.theme.AsesmenMobpro1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AsesmenMobpro1Theme() {
                SetupNavGraph()
            }
        }
    }
}



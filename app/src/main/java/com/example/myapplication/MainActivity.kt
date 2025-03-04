package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

        }
    }
}
class mainViewModel():ViewModel(){
    private val screens= mutableStateListOf<@Composable () -> Unit>({ RHL(this) })
    val screen get() = screens.lastOrNull()
    var text by mutableStateOf("")
    var size by mutableStateOf(16f)
    var speed by mutableStateOf(1f)
    var ColorLs = mutableListOf(
        Color.Red,
        Color.Black,
        Color.Green,
        Color.Cyan,
        Color.Blue
    )
    var Colors by mutableStateOf(Color.Black)
    fun push(value:@Composable () -> Unit){
        screens +=value
    }
    fun pop(){
        screens.removeLastOrNull()
    }
    fun reset(){
        text=""
    }
}
@Composable
fun RHL(viewModel: mainViewModel){
    val configuration = LocalConfiguration.current
    val screenWD= with(LocalDensity.current)
}

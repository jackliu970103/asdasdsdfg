package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Animatable
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    private  val viewModel:mainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            viewModel.text= getText(LocalContext.current).toString()
            Crossfade(viewModel.screen) {
                it?.invoke() ?: finish()
            }
            BackHandler {
                viewModel.pop()
            }
        }
    }
}
fun getText(context: Context): String? {
    val sharedPreferences:SharedPreferences =context.getSharedPreferences("app_sharedpref",Context.MODE_PRIVATE)
    return  sharedPreferences.getString("text","")
}
fun putText(context: Context,text:String){
    val sharedPreferences:SharedPreferences =context.getSharedPreferences("app_sharedpref",Context.MODE_PRIVATE)
    val meneger=sharedPreferences.edit()
    meneger.putString("text",text)
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RHL(viewModel: mainViewModel){
    val context=LocalContext.current
    val BottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val configuration = LocalConfiguration.current
    val screenWD= with(LocalDensity.current){configuration.screenWidthDp.dp.toPx()}
    val offset = remember { androidx.compose.animation.core.Animatable(-screenWD) }
    LaunchedEffect (viewModel.speed){
        while (true){
            offset.snapTo(-screenWD)
            offset.animateTo(
                targetValue = screenWD,
                animationSpec = tween(durationMillis = (10000f/ viewModel.speed).toInt(), easing = LinearEasing)
            )
        }
    }
    BottomSheetScaffold(
        scaffoldState = BottomSheetScaffoldState,
        sheetContent = {
            Column (modifier = Modifier.height(300.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                TextField(value = viewModel.text, onValueChange = { viewModel.text = it
                putText(context,it)
                })
                Slider(
                    value = viewModel.speed,
                    onValueChange = { viewModel.speed = it },
                    valueRange = 1f..10f
                )
                Slider(
                    value = viewModel.size,
                    onValueChange = { viewModel.size = it },
                    valueRange = 1f..100f
                )
                LazyRow {
                    itemsIndexed(
                        viewModel.ColorLs
                    ) { index, item ->
                        Box(modifier = Modifier.background(item).clickable {
                            viewModel.Colors = item
                        }.size(60.dp))
                    }
                }
            }
        }
    ) { paddingValues ->
            Column (modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){
                Spacer(modifier = Modifier.height(100.dp))

                Text(text = if ( viewModel.text=="")SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
                    Date()
                ) else viewModel.text , modifier = Modifier.offset(offset.value.dp), fontSize = viewModel.size.sp, color = viewModel.Colors)
            }
        }
    }

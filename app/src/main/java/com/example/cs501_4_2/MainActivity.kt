package com.example.cs501_4_2

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.cs501_4_2.ui.theme.CS501_4_2Theme
import org.xmlpull.v1.XmlPullParser

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CS501_4_2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HangMan(
                        modifier = Modifier.padding(innerPadding).windowInsetsPadding(WindowInsets.systemBars)
                    )
                }
            }
        }
    }
}

@Composable
fun HangMan(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val progressMap = imageSet()
    var progression = 0
    var hint = 0
    val wordList = parseWords(context)
    Text(
        text = "$wordList",
        modifier = modifier
    )

}
@Composable
fun HangManImage(progression: Int, ){

}
fun imageSet() : HashMap<Int,Int>{
    return hashMapOf(

    )
}


fun parseWords(context : Context) : Set<String>{
    val retList = mutableSetOf<String>()
    val parser = context.resources.getXml(R.xml.wordsforhangman)
    var eventType = parser.eventType
    while(eventType != XmlPullParser.END_DOCUMENT){
        when(eventType){
            XmlPullParser.START_TAG ->{
                when(parser.name){
                    "word" -> retList.add(parser.nextText())
                }
            }
        }
        eventType = parser.next()
    }
    return retList
}
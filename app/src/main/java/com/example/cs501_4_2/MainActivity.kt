package com.example.cs501_4_2

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.formatWithSkeleton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cs501_4_2.ui.theme.CS501_4_2Theme
import kotlinx.parcelize.Parcelize
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import org.xmlpull.v1.XmlPullParser

@Parcelize
data class GameWord(
    val word : String,
    val hint : String
) : Parcelable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CS501_4_2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HangMan(
                        modifier = Modifier
                            .padding(innerPadding)
                            .windowInsetsPadding(WindowInsets.systemBars)
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
    val progression = rememberSaveable() { mutableStateOf(0)}
    val hintMessage = rememberSaveable() { mutableStateOf("NO HINT")}
    val buttonTrueOrFalse = rememberSaveable() { mutableStateOf(List(26){true})}
    val wordList = parseWords(context).toList()
    val gameMessage = rememberSaveable() { mutableStateOf("HINT")}
    val hintNum = rememberSaveable() { mutableStateOf(0)}
    val (chosenWord, chosenWordFound, displayWord) = generateWord(wordList,Modifier)
    Box (modifier = modifier.fillMaxSize()){
        Column(
        ) {
            HangManImage(
                progression,
                progressMap,
                modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
            )
            DisplayAlphabetButton(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.45f),
                buttonTrueOrFalse
            )
            LazyRow(
                modifier = modifier.fillMaxWidth().fillMaxHeight(0.2f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(displayWord){ display ->
                    Text(
                        text = display,
                        fontSize = 20.sp
                    )
                }
            }
            DisplayButton(
                Modifier.fillMaxSize().fillMaxHeight(1f),
                hintMessage,
                hintNum,
                chosenWord,
                gameMessage,
                context,
                progression,
                buttonTrueOrFalse
            )
        }
    }

}
@Composable
fun DisplayButton(
        modifier : Modifier,
        hintMessage : MutableState<String>,
        hintNum: MutableState<Int>,
        chosenWord: GameWord,
        gameMessage : MutableState<String>,
        context: Context,
        progression: MutableState<Int>,
        buttonTrueOrFalse: MutableState<List<Boolean>>
){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                GetHint(
                    hintNum,
                    chosenWord,
                    hintMessage,
                    progression,
                    context,
                    buttonTrueOrFalse
                )
            },
            enabled = if (hintNum.value > 2) false else true
        ){
            Text(text = gameMessage.value)
        }
        Text(
            text = hintMessage.value
        )
        Button(
            onClick = {}
        ){
            Text(text = "New game")
        }
    }
}

fun GetHint(
        hintNum : MutableState<Int>,
        chosenWord: GameWord,
        hintMessage: MutableState<String>,
        progression : MutableState<Int>,
        context: Context,
        buttonTrueOrFalse: MutableState<List<Boolean>>
){
    val currNum = hintNum.value
    when(hintNum.value){
        0 ->
            {
                hintMessage.value = chosenWord.hint
                hintNum.value = currNum + 1
            }
        1 -> {
                if (progression.value < 6){
                    DisableHalfLetter(buttonTrueOrFalse,chosenWord)
                    hintNum.value = currNum + 1
                    progression.value += 1
                }else{
                    val toast = Toast.makeText(context,"CANT GIVE HINT", Toast.LENGTH_SHORT)
                    toast.show()
                }
            }
        2 -> {
            if (progression.value < 6){
                //TODO
                hintNum.value = currNum + 1
                progression.value += 1
            }else{
                val toast = Toast.makeText(context,"CANT GIVE HINT", Toast.LENGTH_SHORT)
                toast.show()
            }
            }
    }
}

@Composable
fun generateWord(wordList : List<GameWord>,modifier: Modifier): Triple<GameWord,List<Boolean>,List<String>>
{
    val randomNum = (0 until wordList.size-1).random()
    val chosenWord = wordList[randomNum]
    val chosenWordFound = rememberSaveable() { List(chosenWord.word.length){false} }
    val displayWord = rememberSaveable() { List(chosenWord.word.length){" _ "} }
    return Triple(chosenWord,chosenWordFound,displayWord)
}

@Composable
fun HangManImage(progression: MutableState<Int>,progressMap : HashMap<Int, Int>, modifier: Modifier){
    if(progression.value <= 7){
        Image(
            painter = painterResource(progressMap[progression.value]!!),
            contentDescription = null,
            modifier = modifier
        )
    }
}

fun DisableHalfLetter(buttonTrueOrFalse: MutableState<List<Boolean>>,chosenWord: GameWord){
    val remaining = buttonTrueOrFalse.value.count{ it }
    var count = 0
    var alphaStart = 65
    var index = 0
    for(i in 0 until  26){
        if (buttonTrueOrFalse.value[alphaStart - 65]){
            if(!(chosenWord.word.contains(alphaStart.toChar()))){
                changeButtonList(buttonTrueOrFalse,alphaStart)
                count++
                if(count >= remaining/2){
                    break
                }
            }
        }
        alphaStart++
    }
}

@Composable
fun DisplayAlphabetButton(
    modifier : Modifier,
    buttonTrueOrFalse : MutableState<List<Boolean>>
){
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Fixed(7),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
        (65..90).forEach(){ num ->
            item{
                Button(
                    onClick = {
                        changeButtonList(buttonTrueOrFalse,num)

                    },
                    modifier = Modifier.size(40.dp),
                    enabled = buttonTrueOrFalse.value[num-65]
                ) {
                    Text(text = "${num.toChar()}")
                }
            }
        }
    }
}

fun revealCharacter(progression: MutableState<Int>, num : Int){

}


fun changeButtonList(buttonTrueOrFalse: MutableState<List<Boolean>>, num: Int){
    val newList = buttonTrueOrFalse.value.toMutableList()
    newList[num-65] = false
    buttonTrueOrFalse.value = newList
}
fun imageSet() : HashMap<Int,Int>{
    return hashMapOf(
        0 to R.drawable.start,
        1 to R.drawable.head,
        2 to R.drawable.body,
        3 to R.drawable.left_leg,
        4 to R.drawable.right_leg,
        5 to R.drawable.left_arm,
        6 to R.drawable.right_arm,
        7 to R.drawable.dead
    )
}
fun parseWords(context : Context) : Set<GameWord>{
    val retList = mutableSetOf<GameWord>()
    val parser = context.resources.getXml(R.xml.wordsforhangman)
    var eventType = parser.eventType
    var word = ""
    var hint = ""
    while(eventType != XmlPullParser.END_DOCUMENT){
        when(eventType){
            XmlPullParser.START_TAG ->{
                when(parser.name){
                    "word" -> word = parser.nextText()
                    "hint" -> hint =  parser.nextText()
                }
            }
            XmlPullParser.END_TAG -> {
                when(parser.name) {
                    "wordInfo" ->  retList.add(GameWord(word,hint))
                }
            }
        }
        eventType = parser.next()
    }
    return retList
}


package com.example.game2048


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.dataStore
import com.example.game2048.destinations.GameScreenDestination
import com.example.game2048.destinations.MainMenuDestination
import com.example.game2048.ui.theme.Bebas
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

val Context.dataStore by dataStore("saved-data.json", GameRepositorySerializer)


@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DestinationsNavHost(navGraph = NavGraphs.root)
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Destination(start = true)
@Composable
fun MainMenu(
    navigator: DestinationsNavigator
) {
    val mainColor = Color(0xffE1C2F6)
    val secondColor = Color(0xffA73EED)
    val fontColor = Color(0xff8CA10A)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val savedData = context.dataStore.data.collectAsState(initial = GameRepository()).value

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(color = mainColor)
            .fillMaxSize()
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(0.dp, 350.dp)
        ) {
            Button(
                onClick = {
                    scope.launch {
                        updateField(emptyList(), 0, context)
                    }
                    navigator.navigate(GameScreenDestination)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = secondColor,
                    contentColor = fontColor
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .padding(10.dp)
                    .size(250.dp, 95.dp)
            ) {
                Text("New game", fontSize = 60.sp, fontFamily = Bebas)
            }

            Button(
                onClick = {
                    if (savedData.savedField.isNotEmpty()) {
                        navigator.navigate(GameScreenDestination)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = secondColor,
                    contentColor = fontColor
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .padding(10.dp)
                    .size(250.dp, 95.dp)
            ) {
                Text("Continue", fontSize = 60.sp, fontFamily = Bebas)
            }
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset(0.dp, 100.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            "2048",
            style = TextStyle(fontSize = 80.sp, color = fontColor, fontFamily = Bebas),
        )
    }
}


@SuppressLint("CoroutineCreationDuringComposition")
@Destination()
@ExperimentalMaterialApi
@Composable
fun GameScreen(navigator: DestinationsNavigator) {
    val context = LocalContext.current
    val savedData = context.dataStore.data.collectAsState(initial = GameRepository()).value
    val mainColor = Color(0xffE1C2F6)
    val secondColor = Color(0xffA73EED)
    val fontColor = Color(0xff8CA10A)
    val best = savedData.bestScore
    val currPos = remember { mutableStateOf(gameStart()) }
    val rememberedCells: Array<Array<MutableState<Int>>> =
        Array(4) { Array(4) { mutableStateOf(0) } }
    val score = remember { mutableStateOf(0) }
    val updateBestScope = rememberCoroutineScope()
    val updateFieldScope = rememberCoroutineScope()

    if (savedData.savedField.isNotEmpty()) {
        currPos.value = savedData.savedField
        score.value = savedData.savedScore
    }

    Box(
        Modifier
            .background(mainColor)
            .fillMaxSize()
    )
    Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
        Box(
            Modifier
                .offset(0.dp, 20.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .size(150.dp, 90.dp)
                .background(secondColor),
            Alignment.Center
        )
        {
            Text(
                text = "score:\n${score.value}",
                modifier = Modifier
                    .padding(5.dp),
                style = TextStyle(
                    fontFamily = Bebas,
                    fontSize = 40.sp,
                    color = fontColor,
                    textAlign = TextAlign.Center
                )
            )
        }
        Box(
            Modifier
                .offset(0.dp, 20.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .size(150.dp, 90.dp)
                .background(secondColor),
            Alignment.Center
        )
        {
            Text(
                text = "Best:\n$best",
                modifier = Modifier
                    .padding(5.dp),
                style = TextStyle(
                    fontFamily = Bebas,
                    fontSize = 40.sp,
                    color = fontColor,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        Box(
            Modifier
                .padding(0.dp, 132.dp)
                .border(10.dp, color = Color.DarkGray)
                .size(380.dp, 380.dp)
        )
        {
            BackField()
            GamingProcess(currPos, rememberedCells)
            if (score.value > best) {
                updateBestScope.launch {
                    updateBest(score.value, context)
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.offset(0.dp, 25.dp)
        ) {
            Button(
                onClick = {
                    if (isMoveAvailable(currPos.value, "Left")) {
                        score.value += makeTurn(currPos.value, "Left").second
                        currPos.value = makeTurn(currPos.value, "Left").first
                        updateFieldScope.launch {
                            updateField(currPos.value, score.value, context)
                        }
                    }
                },
                modifier = Modifier
                    .width(75.dp)
                    .height(250.dp)
                    .offset(0.dp, (-100).dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
            )
            {}
            Column {
                Button(
                    onClick = {
                        if (isMoveAvailable(currPos.value, "Up")) {
                            score.value += makeTurn(currPos.value, "Up").second
                            currPos.value = makeTurn(currPos.value, "Up").first
                            updateFieldScope.launch {
                                updateField(currPos.value, score.value, context)
                            }
                        }
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .height(75.dp)
                        .offset(0.dp, (-150).dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
                ) {}
                Button(
                    onClick = {
                        if (isMoveAvailable(currPos.value, "Down")) {
                            score.value += makeTurn(currPos.value, "Down").second
                            currPos.value = makeTurn(currPos.value, "Down").first
                        }
                        updateFieldScope.launch {
                            updateField(currPos.value, score.value, context)
                        }
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .height(75.dp)
                        .offset(0.dp, (-25).dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
                ) {}
            }
            Button(
                onClick = {
                    if (isMoveAvailable(currPos.value, "Right")) {
                        score.value += makeTurn(currPos.value, "Right").second
                        currPos.value = makeTurn(currPos.value, "Right").first
                        updateFieldScope.launch {
                            updateField(currPos.value, score.value, context)
                        }
                    }
                },
                modifier = Modifier
                    .width(75.dp)
                    .height(300.dp)
                    .offset(0.dp, (-100).dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
            )
            {}
        }
    }
    if (isGameOver(currPos.value)) {
        updateFieldScope.launch {
            updateField(emptyList(), 0, context)
        }
        GameOverDialog(score.value, best, navigator = navigator)
    }
}


@ExperimentalMaterialApi
@Composable
fun GamingProcess(
    currPos: MutableState<List<List<Int>>>,
    rememberedCells: Array<Array<MutableState<Int>>>
) {
    for (i in 0..3) {
        for (j in 0..3) {
            rememberedCells[i][j].value = currPos.value[i][j]
        }
    }
    Row()
    {
        for (i in 0..3) {
            Column() {
                for (j in 0..3) {
                    Cell(rememberedCells[j][i])
                }
            }
        }
    }
}


@ExperimentalMaterialApi
@Composable
fun BackField() {
    Row()
    {
        repeat(4) {
            Column()
            {
                repeat(4) {
                    Box(
                        modifier = Modifier
                            .size(95.dp, 95.dp)
                            .background(Color.Gray)
                    )
                }
            }
        }
    }
}


@ExperimentalMaterialApi
@Composable
fun Cell(num: MutableState<Int>) {
    val color = remember { mutableStateOf(Color.Gray) }
    when (num.value) {
        2 -> color.value = Color(0xFFFFB300)
        4 -> color.value = Color(0xFFFF7500)
        8 -> color.value = Color(0xFFFF4B00)
        16 -> color.value = Color(0xFFFF1301)
        32 -> color.value = Color(0xFFFF0068)
        64 -> color.value = Color(0xFFFF0AED)
        128 -> color.value = Color(0xFF8B00FF)
        256 -> color.value = Color(0xFF6F01FF)
        512 -> color.value = Color(0xFF2014FF)
        1024 -> color.value = Color(0xFF1CF4FF)
        2048 -> color.value = Color(0xFF32FF0F)
        4056 -> color.value = Color(0xFFFFFA00)

    }
    Box(
        modifier = Modifier
            .border(width = 5.dp, color = Color.DarkGray)
            .size(95.dp, 95.dp),
        contentAlignment = Alignment.Center

        )
    {
        if (num.value != 0) {
            Text(
                modifier = Modifier
                    .background(color = color.value)
                    .size(85.dp)
                    .offset(0.dp,10.dp),
                text = "${num.value}",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    color = Color.White,
                    fontFamily = Bebas,
                    fontSize = 48.sp
                )
            )
        }
    }
}

suspend fun updateField(curr: List<List<Int>>, score: Int, context: Context) {
    context.dataStore.updateData {
        it.copy(savedField = curr, savedScore = score)
    }
}

suspend fun updateBest(curr: Int, context: Context) {
    context.dataStore.updateData {
        it.copy(bestScore = curr)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Destination()
@Composable
fun GameOverDialog(score: Int, best: Int, navigator: DestinationsNavigator) {
    val mainColor = Color(0xffE1C2F6)
    val secondColor = Color(0xffA73EED)
    val fontColor = Color(0xff8CA10A)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = mainColor)
    )
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            Modifier
                .offset(0.dp, 20.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .size(150.dp, 90.dp)
                .background(secondColor),
            Alignment.Center
        )
        {
            Text(
                text = "score:\n${score}",
                modifier = Modifier
                    .padding(5.dp),
                style = TextStyle(
                    fontFamily = Bebas,
                    fontSize = 40.sp,
                    color = fontColor,
                    textAlign = TextAlign.Center
                )
            )
        }
        Box(
            Modifier
                .offset(0.dp, 20.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .size(150.dp, 90.dp)
                .background(secondColor),
            Alignment.Center
        )
        {
            Text(
                text = "Best:\n$best",
                modifier = Modifier.padding(5.dp),
                style = TextStyle(
                    fontFamily = Bebas,
                    fontSize = 40.sp,
                    color = fontColor,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxHeight()
        ) {
            Box(
                modifier = Modifier
                    .padding(50.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .size(250.dp, 95.dp),
                Alignment.Center
            ) {
                Text(
                    text = "Game over!",
                    fontSize = 68.sp,
                    fontFamily = Bebas,
                    color = fontColor,
                    textAlign = TextAlign.Center
                )
            }
            Button(
                onClick = {
                    navigator.navigate(GameScreenDestination)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = secondColor,
                    contentColor = fontColor
                ),
                modifier = Modifier
                    .padding(50.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .size(250.dp, 95.dp)
            ) {
                Text("New game", fontSize = 60.sp, fontFamily = Bebas)
            }
            Button(
                onClick = {
                    navigator.navigate(MainMenuDestination)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = secondColor,
                    contentColor = fontColor
                ),
                modifier = Modifier
                    .padding(50.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .size(250.dp, 95.dp)
            ) {
                Text("Main menu", fontSize = 60.sp, fontFamily = Bebas)
            }
        }
    }
}




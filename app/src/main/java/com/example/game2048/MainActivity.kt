package com.example.game2048


import android.os.Bundle
import android.util.Log
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.game2048.destinations.GameScreenDestination
import com.example.game2048.ui.theme.Bebas
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlin.random.Random


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

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(color = mainColor)
            .fillMaxSize()
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(0.dp,350.dp)
        ) {
            Button(onClick = {
                navigator.navigate(GameScreenDestination)
            },
                colors = ButtonDefaults.buttonColors(backgroundColor = secondColor, contentColor = fontColor),
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .padding(10.dp)
                    .size(250.dp, 95.dp)
            ) {
                Text("New game", fontSize = 60.sp, fontFamily = Bebas)
            }

            Button(onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(backgroundColor = secondColor, contentColor = fontColor),
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .padding(10.dp)
                    .size(250.dp, 95.dp)
            ) {
                Text("Continue", fontSize = 60.sp, fontFamily = Bebas)
            }
        }
    }
    Row(modifier = Modifier
        .fillMaxWidth()
        .offset(0.dp, 100.dp),
        horizontalArrangement = Arrangement.Center) {
        Text("2048",style = TextStyle(fontSize = 80.sp, color = fontColor, fontFamily = Bebas),
        )
    }
}


@Destination()
@ExperimentalMaterialApi
@Composable
fun GameScreen(navigator: DestinationsNavigator) {
    val mainColor = Color(0xffE1C2F6)
    val secondColor = Color(0xffA73EED)
    val fontColor = Color(0xff8CA10A)
    val score = 0
    val best = 0
    var currPos = remember { mutableStateOf(gameStart())}
    Box(
        Modifier
            .background(mainColor)
            .fillMaxSize()
    )
    Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "score: $score",
            modifier = Modifier
                .padding(10.dp)
                .offset(0.dp, 20.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(color = secondColor)
                .padding(5.dp),
            style = TextStyle(fontFamily = Bebas, fontSize = 50.sp, color = fontColor)
        )
    }
    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Best: $best",
            modifier = Modifier
                .padding(10.dp)
                .offset(0.dp, 20.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(color = secondColor)
                .padding(5.dp),
            style = TextStyle(fontFamily = Bebas, fontSize = 50.sp, color = fontColor)
        )
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        Box(
            Modifier
                .padding(0.dp, 132.dp)
                .size(380.dp, 380.dp)
                .border(10.dp, color = Color.DarkGray)
        )
        {
            fontOfField()
            GamingProcess(currPos)
            Log.e("Currpos", "${currPos.value}")
        }
        Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.offset(0.dp, 25.dp)) {
            Button(
                onClick = {
                    currPos.value = (makeTurn(currPos.value, "Left"))
                    Log.e("field", "${currPos.value}")
                },
                modifier = Modifier
                    .width(75.dp)
                    .height(250.dp)
                    .offset(0.dp, (-100).dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray))
            {}
            Column {
                Button(
                    onClick = { currPos.value = (makeTurn(currPos.value, "Up")) },
                    modifier = Modifier
                        .width(200.dp)
                        .height(75.dp)
                        .offset(0.dp, (-150).dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
                ) {}
                Button(
                    onClick = { currPos.value = (makeTurn(currPos.value, "Down")) },
                    modifier = Modifier
                        .width(200.dp)
                        .height(75.dp)
                        .offset(0.dp, (-25).dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
                ) {}
            }
            Button(
                onClick = { currPos.value = (makeTurn(currPos.value, "Right")) },
                modifier = Modifier
                    .width(75.dp)
                    .height(300.dp)
                    .offset(0.dp, (-100).dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray))
            {}
        }
    }
}


@ExperimentalMaterialApi
@Composable
fun GamingProcess(currPos: MutableState<List<List<Int>>>) {
    Log.e("FIELD", "${currPos.value}")
    var rememberedCells: Array<Array<MutableState<Int>>> =
        Array(4) { Array(4) { mutableStateOf(0) } }
    for (i in 0..3) {
        for (j in 0..3) {
            rememberedCells[i][j] = remember { mutableStateOf(currPos.value[i][j]) }
        }
    }
    Row()
    {
        Column()
        {
            cell(rememberedCells[0][0])
            cell(rememberedCells[1][0])
            cell(rememberedCells[2][0])
            cell(rememberedCells[3][0])
        }
        Column()
        {
            cell(rememberedCells[0][1])
            cell(rememberedCells[1][1])
            cell(rememberedCells[2][1])
            cell(rememberedCells[3][1])
        }
        Column()
        {
            cell(rememberedCells[0][2])
            cell(rememberedCells[1][2])
            cell(rememberedCells[2][2])
            cell(rememberedCells[3][2])
        }
        Column()
        {
            cell(rememberedCells[0][3])
            cell(rememberedCells[1][3])
            cell(rememberedCells[2][3])
            cell(rememberedCells[3][3])
        }
    }
}


@ExperimentalMaterialApi
@Composable
fun fontOfField() {
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
                            .border(5.dp, color = Color.DarkGray)
                    )
                }
            }
        }
    }
}


fun gameStart(): List<List<Int>> {
    val field = Array(4) { Array(4) { 0 } }
    val start: Pair<Int, Int> = Random.nextInt(0, 3) to Random.nextInt(0, 3)
    field[start.first][start.second] = 2
    return listOf(
        listOf(field[0][0], field[0][1], field[0][2], field[0][3]),
        listOf(field[1][0], field[1][1], field[1][2], field[1][3]),
        listOf(field[2][0], field[2][1], field[2][2], field[2][3]),
        listOf(field[3][0], field[3][1], field[3][2], field[3][3]),
    )
}


@ExperimentalMaterialApi
@Composable
fun cell(num: MutableState<Int>) {
    var color = remember { mutableStateOf(Color.Gray) }
    when (num.value) {
        2 -> color.value = Color(0xFFFFB300)
        4 -> color.value = Color.Blue
        8 -> color.value = Color.Cyan
    }
    Box(
        modifier = Modifier
            .size(95.dp, 95.dp)
            .border(width = 5.dp, color = Color.DarkGray)
    )
    {
        if (num.value != 0) {
            Text(
                modifier = Modifier
                    .background(color = color.value)
                    .size(95.dp, 95.dp),
                text = "${num.value}",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    color = Color.White,
                    fontFamily = Bebas,
                    fontSize = 70.sp
                )
            )
        }
    }
}




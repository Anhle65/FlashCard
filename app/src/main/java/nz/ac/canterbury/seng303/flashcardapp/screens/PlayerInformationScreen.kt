package nz.ac.canterbury.seng303.flashcardapp.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.FlashCardViewModel

@Composable
fun PlayerInformationScreen(navController: NavController, cardViewModel: FlashCardViewModel,
                            name: String,
                            onPlayerNameChange: (String) -> Unit){
    val context = LocalContext.current
    Column (
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
            .drawBehind {
                drawRoundRect(
                    color = Color(0xFFADD8E6),
                    cornerRadius = CornerRadius(16.dp.toPx()),
                )
            }
            .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(16.dp)),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Who will play?",
            textAlign = TextAlign.Center,
            style = TextStyle(
                color = Color.Blue,
                fontSize = MaterialTheme.typography.headlineLarge.fontSize
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
        )
        OutlinedTextField(
            value = name,
            onValueChange = {onPlayerNameChange(it)},
            placeholder = {Text(text = "Your name here")},
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .drawBehind {
                    drawRoundRect(
                        color = Color.LightGray,
                        cornerRadius = CornerRadius(8.dp.toPx()),
                    )
                }
        )
        Button(
            onClick = {
                if (cardViewModel.playerName.isBlank()) {
                    Log.d("PLAYER_INFOR", "Please enter your name")
                    Toast.makeText(
                        context,
                        "Please enter your name",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    navController.navigate("PlayFlashCard")
                }
            }
        ){
            Text(text = "Start")
        }
    }
}
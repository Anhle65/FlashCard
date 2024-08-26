package nz.ac.canterbury.seng303.flashcardapp.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.FlashCardViewModel

@Composable
fun SummaryScreen (navController: NavController, cardViewModel: FlashCardViewModel) {
    val flashCards = cardViewModel.results.keys.toList()
    val answers = cardViewModel.results.values.toList()
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .fillMaxHeight()
            .fillMaxSize()
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
        item {
            Text(
                text = "${cardViewModel.playerName}'s Summary",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    color = Color.Blue,
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )
        }
        item {
            Text(
                text = "Score: ${cardViewModel.counterCorrect}/${cardViewModel.totalQuestion}",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    color = Color.Black,
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
        }

        Log.d("SUMMARY", "$flashCards wwith $answers")
        items(flashCards.size) { index->
            Card (modifier = Modifier
                .padding(horizontal = 25.dp, vertical = 5.dp)
                .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = flashCards[index].question,
                        style = TextStyle(
                            color = Color.Black
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    )
                    if (!answers[index]) {
                        Icon(
                            modifier = Modifier
                                .size(48.dp)
                                .padding(horizontal = 5.dp),
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Wrong Answer",
                            tint = Color.Red
                        )
                    } else {
                        Icon(
                            modifier = Modifier
                                .size(48.dp)
                                .padding(horizontal = 5.dp),
                            imageVector = Icons.Outlined.Check,
                            contentDescription = "Correct Answer",
                            tint = Color.Green
                        )
                    }
                }
            }
        }
        item {
            Button(
                onClick = { navController.navigate("Home") }
            ){
                Text(text = "Play again")
            }
        }

    }
}
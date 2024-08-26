package nz.ac.canterbury.seng303.flashcardapp.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.flashcardapp.models.FlashCard
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.FlashCardViewModel

@Composable
fun PlayFlashCardScreen(navController: NavController, cardViewModel: FlashCardViewModel){
    cardViewModel.getCards()
    val listCards: List<FlashCard> by cardViewModel.cards.collectAsState(emptyList())
    cardViewModel.setTotalQuestion(listCards.size)
    var currentQuestion by rememberSaveable { mutableIntStateOf(0) }
    var selectedAnswer by rememberSaveable { mutableStateOf("")}
    val context = LocalContext.current

    if (listCards.isNotEmpty()) {
        val shuffledCards = rememberSaveable (listCards) {
            listCards.shuffled()
        }
        val currentFlashCard = shuffledCards[currentQuestion]
        val shuffledAnswers = rememberSaveable (currentFlashCard) {
            currentFlashCard.listAnswer.filter { it!= "" }.shuffled()
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .drawBehind {
                    drawRoundRect(
                        color = Color(0xFFADD8E6),
                        cornerRadius = CornerRadius(16.dp.toPx()),
                    )
                }
                .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(16.dp)),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Play flash cards",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
            item {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .drawBehind {
                        drawRoundRect(
                            color = Color.LightGray,
                            cornerRadius = CornerRadius(10.dp.toPx())
                        )
                    }
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(10.dp)
                    )
                ) {
                    Text(
                        text = currentFlashCard.question,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
            items(shuffledAnswers.size) { index ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    RadioButton(
                        selected = selectedAnswer == shuffledAnswers[index],
                        onClick = { selectedAnswer = shuffledAnswers[index] },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary,
                            unselectedColor = Color.Gray
                        )
                    )
                    Text(text = shuffledAnswers[index])
                    Log.d("Flash card screen", "selected answer: $selectedAnswer, Correct answer is: ${currentFlashCard.correctAnswer}")
                }
            }
            item {
                Row(modifier = Modifier
                    .padding(25.dp)
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.Bottom
                )
                {
                    Text(text = "${currentQuestion + 1}/${cardViewModel.totalQuestion}",
                        modifier = Modifier
                            .padding(vertical = 16.dp))
                    Button(
                        onClick = {
                            if (selectedAnswer == "") {
                                Log.d("PLAY_CARDS", "You need to choose 1 answer to submit")
                                Toast.makeText(
                                    context,
                                    "You need to choose 1 correct answer to submit",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                if (selectedAnswer == currentFlashCard.correctAnswer) {
                                    cardViewModel.incrementCorrectCounter()
                                    cardViewModel.addResultCard(currentFlashCard, true)
                                    Log.e("CARD_SCREEN", "${cardViewModel.results.size}")
                                    Log.e("CARD_SCREEN", "YOU ARE CORRECT")
                                    Toast.makeText(
                                        context,
                                        "Your answer is correct",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Log.e("CARD_SCREEN", "YOU ARE WRONG")
                                    Toast.makeText(
                                        context,
                                        "Your answer is incorrect",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    cardViewModel.addResultCard(currentFlashCard, false)
                                }
                                if (currentQuestion < cardViewModel.totalQuestion - 1)
                                    currentQuestion += 1
                                else {
                                    cardViewModel.sortedRanking(cardViewModel.playerName, cardViewModel.counterCorrect)
                                    Log.e(
                                        "CARD_SCREEN",
                                        "You have ${cardViewModel.counterCorrect} correct card in total, " +
                                                "list ranking: ${cardViewModel.sortedRanking}"
                                    )
                                    navController.navigate("LoadingScreen")
                                }
                            }
                            selectedAnswer = ""
                        }) {
                        Text(text = "Submit")
                    }
                }
            }
        }
    } else {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .fillMaxSize()
                .drawBehind {
                    drawRoundRect(
                        color = Color.Cyan,
                        cornerRadius = CornerRadius(16.dp.toPx()),
                    )
                }
                .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(16.dp)),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "You need to have at least 1 card to play",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(20.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}
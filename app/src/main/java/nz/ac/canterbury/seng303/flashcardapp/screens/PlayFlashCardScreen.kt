package nz.ac.canterbury.seng303.flashcardapp.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.flashcardapp.models.FlashCard
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.FlashCardViewModel

@Composable
fun PlayFlashCardScreen(navController: NavController, cardViewModel: FlashCardViewModel){
    cardViewModel.getCards()
    val listCards: List<FlashCard> by cardViewModel.cards.collectAsState(emptyList())
    val totalQuestion = listCards.size
    var currentQuestion by rememberSaveable { mutableStateOf(1)}
//    cardViewModel.getCardById(cardId = cardId.toIntOrNull())
//    val selectedCardState by cardViewModel.selectedCard.collectAsState(null)
//    val card: FlashCard? = selectedCardState
    if (listCards.size > 0) {
        for (card in listCards) {
            navController.navigate("FlashCard/${card.id}}")
//            var listAnswers by remember { mutableStateOf(card.listAnswer.toMutableList()) }
//            val listAnswers = card.listAnswer
//            LazyColumn(
//                modifier = Modifier
//                    .padding(16.dp)
//                    .fillMaxWidth()
//                    .fillMaxHeight()
//                    .fillMaxSize()
//                    .drawBehind {
//                        drawRoundRect(
//                            color = Color.Cyan,
//                            cornerRadius = CornerRadius(16.dp.toPx()),
//                        )
//                    }
//                    .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(16.dp)),
//                verticalArrangement = Arrangement.spacedBy(10.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                item {
//                    Text(
//                        text = "Play flash cards",
//                        style = MaterialTheme.typography.headlineLarge,
//                        modifier = Modifier
//                            .padding(16.dp)
//                    )
//                }
//                item {
//                    Box(modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp)
//                        .drawBehind {
//                            drawRoundRect(
//                                color = Color.LightGray,
//                                cornerRadius = CornerRadius(10.dp.toPx())
//                            )
//                        }
//                        .border(
//                            width = 1.dp,
//                            color = Color.Black,
//                            shape = RoundedCornerShape(10.dp)
//                        )
//                    ) {
//                        Text(
//                            text = "${card.question}",
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(16.dp)
//                        )
//                    }
////                    var listAnswers by remember { mutableStateOf(card.listAnswer.toMutableList()) }
//                }
//                items(listAnswers.size) { index ->
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier
//                            .padding(horizontal = 16.dp)
//                            .fillMaxWidth(),
//                        horizontalArrangement = Arrangement.spacedBy(20.dp),
//                    ) {
//                        var selectedAnswer by rememberSaveable { mutableStateOf("") }
//                        RadioButton(
//                            selected = selectedAnswer == listAnswers[index],
//                            onClick = { selectedAnswer = listAnswers[index] },
//                            colors = RadioButtonDefaults.colors(
//                                selectedColor = MaterialTheme.colorScheme.primary,
//                                unselectedColor = Color.Gray
//                            )
//                        )
//                        Text(text = card.listAnswer[index])
//                        Log.d("Flash card screen", "selected answer: $selectedAnswer")
//                    }
//                }
//                item {
                    Row(modifier = Modifier
                        .padding(16.dp)
                        .fillMaxHeight()
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically.apply { Alignment.BottomEnd }
                    )
                    {
                        Text(text = "$currentQuestion/$totalQuestion")
                        Button(
                            onClick = {
                                currentQuestion += 1
                            }) {
                            Text(text = "Submit")
                        }
                    }
//                }
//            }
        }
    } else {
        Text(text = "You need to have cards to play", style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(16.dp).fillMaxWidth().fillMaxHeight())
    }
}
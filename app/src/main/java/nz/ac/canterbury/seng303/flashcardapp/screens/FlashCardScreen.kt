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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import nz.ac.canterbury.seng303.flashcardapp.models.FlashCard
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.FlashCardViewModel

@Composable
fun FlashCardScreen(cardId: String, cardViewModel: FlashCardViewModel){
    cardViewModel.getCards()
    val listCards: List<FlashCard> by cardViewModel.cards.collectAsState(emptyList())
    val totalQuestion = listCards.size
    val currentQuestion by rememberSaveable { mutableStateOf(1)}
    cardViewModel.getCardById(cardId = cardId.toIntOrNull())
    val selectedCardState by cardViewModel.selectedCard.collectAsState(null)
    val card: FlashCard? = selectedCardState
    if (card != null) {
        val listAnswers by rememberSaveable { mutableStateOf(card.listAnswer) }
        val (selectedAnswer, onOptionSelected) = rememberSaveable { mutableStateOf("") }
        LazyColumn(
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
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { Text(text = "Play flash cards",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(16.dp)) }
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
                    .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(10.dp))
                ){
                    Text(text = "${card.question}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
            items (listAnswers.size) { index ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    RadioButton(
                        selected = (listAnswers[index] == selectedAnswer),   //Set the sellected answer
                        onClick = { onOptionSelected(listAnswers[index]) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary,
                            unselectedColor = Color.Gray
                        )
                    )
                    Text(text = listAnswers[index])
                    Log.d("Flash card screen", "selected answer: $selectedAnswer correct answer is ${card.correctAnswer}")
                }
            }
            item {
                Row (modifier = Modifier
                    .padding(16.dp)
                    .fillMaxHeight()
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                )
                {
                    Text(text = "$currentQuestion/$totalQuestion")
                    Button(
                        onClick = {
                            if (selectedAnswer == card.correctAnswer) {
                                Log.d("CARD_SCREEN", "YOU ARE CORRECT")
                            } else {
                                Log.d("CARD_SCREEN", "YOU ARE WRONG")
                            }
                        }) {
                        Text(text = "Submit")
                    }
                }
            }
        }
    } else {
        Text(text = "Could not find card: $cardId", style = MaterialTheme.typography.headlineMedium)
    }
}
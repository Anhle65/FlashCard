package nz.ac.canterbury.seng303.flashcardapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import nz.ac.canterbury.seng303.flashcardapp.models.FlashCard
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.FlashCardViewModel

@Composable
fun FlashCardScreen(cardId: String, cardViewModel: FlashCardViewModel){

    cardViewModel.getCardById(cardId = cardId.toIntOrNull())
    val selectedCardState by cardViewModel.selectedCard.collectAsState(null)
    val card: FlashCard? = selectedCardState
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .drawBehind {
                drawRoundRect(
                    color = Color.Cyan,
                    cornerRadius = CornerRadius(15.dp.toPx()),
                )
            }
    ) {
        if (card != null) {
            Text(text = "Question: ${card.question}", style = MaterialTheme.typography.headlineMedium)
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            )
//            Text(
//                text = "Content:",
//                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
//            )
            Text(
                text = card.question,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(4.dp, 0.dp, 0.dp, 0.dp)
            )
            var listAnswers by remember { mutableStateOf(card.listAnswer.toMutableList()) }
            LazyColumn {
                items (listAnswers.size) { index ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    ) {
                        var selectedAnswer by rememberSaveable { mutableStateOf("") }
                        RadioButton(
                            selected = selectedAnswer == listAnswers[index],
                            onClick = { selectedAnswer = listAnswers[index] },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary,
                                unselectedColor = Color.Gray
                            )
                        )
                    }
                    Divider() // Add a divider between items
                }
            }
        } else {
            Text(text = "Could not find card: $cardId", style = MaterialTheme.typography.headlineMedium)
        }
    }
}
package nz.ac.canterbury.seng303.flashcardapp.screens

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import nz.ac.canterbury.seng303.flashcardapp.models.FlashCard
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.FlashCardViewModel


@Composable
fun FlashCardListScreen(navController: NavController, cardViewModel: FlashCardViewModel) {
    cardViewModel.getCards()
    val cards: List<FlashCard> by cardViewModel.cards.collectAsState(emptyList())
    Column (
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .padding(16.dp)
            .drawBehind {
                drawRoundRect(
                    color = Color.Cyan,
                    cornerRadius = CornerRadius(16.dp.toPx()),
                )
            }
            .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(16.dp)),
        Arrangement.spacedBy(10.dp)
    ){
        Text(text = "Flash Cards",
            textAlign = TextAlign.Center,
            style = TextStyle(
                color = Color.Black,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )
        LazyColumn {
            items(cards.size) { index ->
                CardItem(navController = navController, card = cards[index],
                    deleteCardFn = { id: Int -> cardViewModel.deleteCard(id) })
                Divider() // Add a divider between items
            }
        }
    }
}

@Composable
fun CardItem(navController: NavController, card: FlashCard, deleteCardFn: (id: Int) -> Unit) {
    val context = LocalContext.current
    Column (
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { navController.navigate("FlashCard/${card.id}") } //go into the flash card when click
            .drawBehind {
                drawRoundRect(
                    color = Color.White,
                    cornerRadius = CornerRadius(15.dp.toPx()),
                )
            },
//        Arrangement.spacedBy(20.dp)
    ){
        Text(text = card.question,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth())
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(50.dp)
        ) {
            IconButton(
                modifier = Modifier
                    .size(50.dp)
                    .drawBehind {
                    drawRoundRect(
                        color = Color.Blue,
                        cornerRadius = CornerRadius(25.dp.toPx()),
                    )
                },
                // When click on the search button, it goes to browser and search for the question on flash card
                onClick = {
                    val searchQuery = "${card.question}"    //Extract the query that will be parsed to the search intent
                    val searchIntent = Intent(Intent.ACTION_WEB_SEARCH).apply {
                        putExtra(SearchManager.QUERY, searchQuery)  // Pass the string to search bar for searching
                    }
                    context.startActivity(searchIntent)     // Launch the search activity
                    Log.d("LIST_CARD", "Click on search button and search for ${card.question}")
                })
            {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Search",
                    tint = Color.White,
                    modifier = Modifier
                        .width(30.dp)
                )
            }
            IconButton(
                modifier = Modifier
                    .size(50.dp)
                    .drawBehind {
                        drawRoundRect(
                            color = Color.Blue,
                            cornerRadius = CornerRadius(25.dp.toPx()),
                    )
                },
                onClick = {
                navController.navigate("EditCard/${card.id}")
            })
            {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Edit",
                    tint = Color.White
                )
            }
            IconButton(
                modifier = Modifier
                    .size(50.dp)
                    .drawBehind {
                        drawRoundRect(
                        color = Color.Blue,
                        cornerRadius = CornerRadius(25.dp.toPx()),
                    )
                },
                onClick = {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Delete card: \"${card.question}\"?")
                    .setCancelable(false)
                    .setPositiveButton("Delete") { dialog, id ->
                        deleteCardFn(card.id)
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialog, id ->
                        // Dismiss the dialog
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
                }) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        }
    }

}
package nz.ac.canterbury.seng303.flashcardapp.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun RankingScreen(navController: NavController, cardViewModel: FlashCardViewModel) {
    val players = cardViewModel.sortedRanking.keys.toList()
    val scores = cardViewModel.sortedRanking.values.toList()
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
                text = "Ranking Table",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    color = Color.Red,
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )
        }
        if (players.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No data to show",
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = MaterialTheme.typography.headlineLarge.fontSize
                        )
                    )
                }
            }
        } else {
            items(players.size) { index ->
                Card(
                    modifier = Modifier
                        .height(70.dp)
                        .padding(horizontal = 25.dp, vertical = 5.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    )
                    {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                        ) {
                            Text(
                                text = "${index + 1}. ${players[index]}",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier
                                    .weight(20f)
                                    .fillMaxWidth()
                                    .align(Alignment.CenterVertically)
                                    .padding(horizontal = 16.dp)
                            )
                            Text(
                                text = "${scores[index]}/${cardViewModel.totalQuestion}",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier
                                    .weight(20f)
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                            )
                        }
                    }
                }
            }
        }
        item {
            Row(
                horizontalArrangement = Arrangement.Absolute.Right,
                verticalAlignment = Alignment.CenterVertically.apply { Alignment.BottomEnd }
            ) {
                Button(
                    onClick = { navController.navigate("Home") }
                ) {
                    Text(text = "Back")
                }
            }
        }
    }
}
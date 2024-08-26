package nz.ac.canterbury.seng303.flashcardapp

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.animation.doOnEnd
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import nz.ac.canterbury.seng303.flashcardapp.models.FlashCard
import nz.ac.canterbury.seng303.flashcardapp.screens.CreateFlashCardScreen
import nz.ac.canterbury.seng303.flashcardapp.screens.EditFlashCard
import nz.ac.canterbury.seng303.flashcardapp.screens.FlashCardListScreen
import nz.ac.canterbury.seng303.flashcardapp.screens.FlashCardScreen
import nz.ac.canterbury.seng303.flashcardapp.screens.PlayFlashCardScreen
import nz.ac.canterbury.seng303.flashcardapp.screens.PlayerInformationScreen
import nz.ac.canterbury.seng303.flashcardapp.screens.RankingScreen
import nz.ac.canterbury.seng303.flashcardapp.screens.SummaryScreen
import nz.ac.canterbury.seng303.flashcardapp.ui.theme.FlashcardappTheme
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.CreateCardViewModel
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.EditCardViewModel
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.FlashCardViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel as koinViewModel

class MainActivity : ComponentActivity() {
    private val cardViewModel: FlashCardViewModel by koinViewModel()
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            // Create custom animation.
            val slideUp = ObjectAnimator.ofFloat(
                splashScreenView,
                View.TRANSLATION_Y,
                0f,
                -splashScreenView.height.toFloat()
            )
            slideUp.interpolator = AnticipateInterpolator()
            slideUp.duration = 1500L
            // Call SplashScreenView.remove at the end of the custom animation.
            slideUp.doOnEnd { splashScreenView.remove() }
            // Run animation.
            slideUp.start()
        }
        enableEdgeToEdge()
        setContent {
            val editFlashCardViewModel: EditCardViewModel = viewModel()
            FlashcardappTheme {
                val navController = rememberNavController()
                Scaffold(
                    topBar = {
                        // Add your AppBar content here
                        TopAppBar(
                            title = { Text("Flash Card App") },
                            navigationIcon = {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
                            }
                        )
                    }
                ){
                    Box(modifier = Modifier.padding(it)) {
                        NavHost(navController = navController, startDestination = "Home") {
                            composable("Home") {
                                cardViewModel.setName("")
                                FlashCard(navController = navController)
                            }
                            composable(
                                "FlashCard/{cardId}",
                                arguments = listOf(navArgument("cardId") {
                                    type = NavType.StringType
                                })
                            ) { backStackEntry ->
                                val cardId = backStackEntry.arguments?.getString("cardId")
                                cardId?.let { cardIdParam: String -> FlashCardScreen(cardIdParam, cardViewModel) }
                            }
                            composable("PlayerInformation"){
                                cardViewModel.resetCounter()
                                cardViewModel.resetResult()
                                cardViewModel.setTotalQuestion(0)
                                PlayerInformationScreen(navController = navController, cardViewModel = cardViewModel,
                                    name = cardViewModel.playerName,
                                    onPlayerNameChange = {newPlayer -> cardViewModel.setName(newPlayer)})
                            }
                            composable("PlayFlashCard"){
                                PlayFlashCardScreen(navController, cardViewModel = cardViewModel)
                            }
                            composable("CreateFlashCards") {
                                val createCardViewModel: CreateCardViewModel = viewModel()
                                CreateFlashCardScreen(navController = navController, question = createCardViewModel.question,
                                    onQuestionChange = {newQuestion -> createCardViewModel.updateQuestion(newQuestion)},
                                    createCardViewModel = createCardViewModel,
                                    inputAnswers = createCardViewModel.listAns.toMutableList(),
                                    onListAnswerChange = {
                                            newListAnswer -> createCardViewModel.setAnswers(newListAnswer)},
                                    onCrrAnswerChange = {corrAns -> createCardViewModel.updateCorrectAnswer(corrAns)},
                                    createCardFn = {question, listAnswer, correctAns -> cardViewModel.createCard(question, listAnswer, correctAns)})
                            }
                            composable("CardList") {
                                val listCards: List<FlashCard> by cardViewModel.cards.collectAsState(emptyList())
                                Log.d("MAIN_ACTIVITY", "Number of question in storage is: ${listCards.size}")
                                FlashCardListScreen(navController, cardViewModel)
                            }
                            composable(
                                "EditCard/{cardId}",
                                arguments = listOf(navArgument("cardId") {
                                    type = NavType.StringType
                                })
                            ) {
                                    backStackEntry ->
                                val cardId = backStackEntry.arguments?.getString("cardId")
                                cardId?.let {
                                    EditFlashCard(navController = navController,
                                        cardId = cardId,
                                        editCardViewModel = editFlashCardViewModel,
                                        cardViewModel = cardViewModel
                                    )
                                }
                            }
                            composable("SummaryResult") {
                                SummaryScreen(navController, cardViewModel)
                            }
                            composable("Ranking") {
                                RankingScreen(navController, cardViewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FlashCard(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .drawBehind {
                drawRoundRect(
                    color = Color(0xFFADD8E6),
                    cornerRadius = CornerRadius(15.dp.toPx()),
                )
            }
            .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(16.dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier.padding(10.dp),
            onClick = { navController.navigate("CardList") }) {
            Text("View Flash Cards")
        }
        Button(
            modifier = Modifier.padding(10.dp),
            onClick = { navController.navigate("CreateFlashCards") }) {
            Text("Create Flash Cards")
        }
        Button(
            modifier = Modifier.padding(10.dp),
            onClick = { navController.navigate("PlayerInformation") }) {
            Text("Play Flash Card")
        }
        Button(
            modifier = Modifier.padding(10.dp),
            onClick = { navController.navigate("Ranking") }) {
            Text("Ranking table")
        }
    }
}
package nz.ac.canterbury.seng303.flashcardapp.screens

import android.annotation.SuppressLint
import android.app.AlertDialog
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
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
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.EditCardViewModel
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.FlashCardViewModel
//
//
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFlashCard(
    cardId: String,
    editCardViewModel: EditCardViewModel,
    cardViewModel: FlashCardViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val selectedCardState by cardViewModel.selectedCard.collectAsState(null)
    val flashCard: FlashCard? =
        selectedCardState // we explicitly assign to card to help the compilers smart cast out
    var listAnswers by rememberSaveable {
        mutableStateOf(
            listOf(
                mutableStateOf(""),
                mutableStateOf(""),
                mutableStateOf(""),
                mutableStateOf("")
            )
        )
    }
    var isInitialized by rememberSaveable { mutableStateOf(false)}
    var checked by rememberSaveable {
        mutableStateOf(
            listOf(
                mutableStateOf(false),
                mutableStateOf(false),
                mutableStateOf(false),
                mutableStateOf(false)
            )
        )
    }
    LaunchedEffect(flashCard) {  // Get the default values for the flash card properties
        if (flashCard == null) {
            cardViewModel.getCardById(cardId.toIntOrNull())
        } else {
            if(!isInitialized) {
                editCardViewModel.setDefaultValues(flashCard)
                while (listAnswers.size < editCardViewModel.listAns.size) {
                    listAnswers = listAnswers.toMutableList().apply { add(mutableStateOf("")) }
                    checked = checked.toMutableList().apply { add(mutableStateOf(false)) }
                }
                while (listAnswers.size > editCardViewModel.listAns.size) {
                    checked = checked.toMutableList().apply { removeLast() }
                    listAnswers = listAnswers.toMutableList().apply { removeLast() }
                }
//                for (index in 0..editCardViewModel.listAns.size - 1) {
//                    listAnswers[index].value = editCardViewModel.listAns[index]
//                    if (listAnswers[index].value == editCardViewModel.correctAns) {
//                        checked[index].value = true
//                    } else {
//                        checked[index].value = false
//                    }
//                }
                isInitialized = true
            }
//            Log.e("EDIT_CARD", "New crrAnswe: ${editCardViewModel.correctAns}")
//            for (index in 0..editCardViewModel.listAns.size - 1) {
//                listAnswers[index].value = editCardViewModel.listAns[index]
//                if (listAnswers[index].value == editCardViewModel.correctAns) {
//                    checked[index].value = true
//                } else {
//                    checked[index].value = false
//                }
//                Log.e("EDIT_CARD", "New crrAnswe: ${editCardViewModel.correctAns}, the checkbox state in that index is ${checked[index]}")
//            }
        }
    }


//    var checked by rememberSaveable {
//        mutableStateOf(
//            editCardViewModel.listAns.map { answer ->
//                mutableStateOf(answer == editCardViewModel.correctAns)
//            }
//        )
//    }
//    var listAnswers by rememberSaveable {
//        mutableStateOf(
//            editCardViewModel.listAns.map { mutableStateOf(it) }
//        )
//    }

//    checked[indexCorrectAnswer].value = true

//    Log.d("EDIT_SCREEN", "before change state List of check states ${checked.size} each states is: $checked")
//    Log.d("EDIT_SCREEN", "List of check states ${checked.size} each states is: $checked")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .fillMaxWidth()
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
        item {
            Text(text = "Edit flash card",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    color = Color.Black,
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )
        }
        item {
            OutlinedTextField(
                value = editCardViewModel.question,
                onValueChange = {},
                enabled = false,
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
        }
        items(listAnswers.size) { index ->
            Log.d(
                "Edit Card Screen",
                "Number answer of card ${editCardViewModel.listAns.size}, each element is ${editCardViewModel.listAns}\n" +
                        "Number edited answer ${listAnswers.size}, each element is ${listAnswers}"
            )
            listAnswers[index].value = editCardViewModel.listAns[index]
            if (listAnswers[index].value == editCardViewModel.correctAns) {
                checked[index].value = true
            } else {
                checked[index].value = false
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    Checkbox(
                        checked = checked[index].value,
                        onCheckedChange = {
                            checked[index].value = it
                            if (checked[index].value) {
                                editCardViewModel.updateCorrectAnswer(listAnswers[index].value)
                            } else {
                                if(checked.all { !it.value }) {
                                    editCardViewModel.updateCorrectAnswer("")
                                }
                            }
                        }
                    )
                    Log.d("Card Screen", "Correct ans is ${editCardViewModel.correctAns}")
                    OutlinedTextField(
                        value = listAnswers[index].value,
                        onValueChange = {
                            listAnswers[index].value = it
                            Log.e("EDIT_SCREEN", "New value ${listAnswers[index].value}, in edit view model ${listAnswers[index].value}")
                            editCardViewModel.updateAnswer(index, it)
                        },
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .fillMaxWidth()
                            .drawBehind {
                                drawRoundRect(
                                    color = Color.LightGray,
                                    cornerRadius = CornerRadius(8.dp.toPx()),
                                )
                            }
                    )
                    Log.e("Edit screen", "changed value $listAnswers")
                }
            }
        }
        item {
            Button(
                onClick = {
                    editCardViewModel.addOptionAnswer()
                    listAnswers = listAnswers.toMutableList().apply { add(mutableStateOf("")) }
                    checked = checked.toMutableList().apply { add(mutableStateOf(false)) }
                },
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(4.dp)
                )
            }
        }
        item {
            Button(
                modifier = Modifier
                    .padding(8.dp),
                onClick = {
                    cardViewModel.editFlashCardById(
                        cardId.toIntOrNull(),
                        card = FlashCard(
                            cardId.toInt(),
                            editCardViewModel.question,
                            listAnswers.filter { it.value != "" }.map { it.value }.toMutableList(),
                            editCardViewModel.correctAns
                        )
                    )
                    val builder = AlertDialog.Builder(context)
//                    if (question == "") {
//                        Toast.makeText(
//                            context,
//                            "Could not create a flash card without question",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    } else if (listAnswer.all { it.isEmpty() }) {
//                        Toast.makeText(
//                            context,
//                            "Could not create a flash card without answers",
//                            Toast.LENGTH_SHORT
//                        ).show()
//
//                    if (checked.all { false }) {
////                        crrAns = ""
//                        editCardViewModel.updateCorrectAnswer("")
//                    }
                    if (editCardViewModel.correctAns == "") {
                        Toast.makeText(
                            context,
                            "You need to choose 1 correct answer",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (editCardViewModel.listAns.filter { it != "" }.size < 2) {
                        Toast.makeText(
                            context,
                            "You need to have at least 2 answers",
                            Toast.LENGTH_SHORT
                        ).show()
//                        builder.setMessage("Edit Card: ${flashCard.question}")
//                            .setPositiveButton("Ok") { dialog, id -> /* Run some code on click */
//                                createCardFn(question, listAnswer, crrAns)
//                                onQuestionChange("")
//                                onCrrAnswerChange("")
//                                onListAnswerChange(emptyList<String>().toMutableList())
//                                navController.navigate("cardList")
//                            }.setNegativeButton("Cancel") { dialog, id ->
//                                dialog.dismiss()
//                    }
//                    builder.setMessage("Edited flash card!")
//                        .setPositiveButton("Ok") { dialog, id ->
////                            editCardViewModel.setAnswers(listAnswers)
//                            navController.navigate("CardList")
//                        }
//                        .setNegativeButton("Cancel") { dialog, id ->
//                            dialog.dismiss()
//                        }
                    } else {
                        navController.navigate("cardList")
                    }
//                    val alert = builder.create()
//                    alert.show()
                    }
                ) {
                    Text(text = "Save and return")
                }
            }
        }
    }

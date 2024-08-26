package nz.ac.canterbury.seng303.flashcardapp.screens

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
import androidx.compose.material3.Icon
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
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.CreateCardViewModel

@Composable
fun CreateFlashCardScreen(navController: NavController,
                          question: String,
                          createCardViewModel: CreateCardViewModel,
                          onQuestionChange: (String) -> Unit,
                          inputAnswers: MutableList<String>,
                          onListAnswerChange: (MutableList<String>) -> Unit,
                          onCrrAnswerChange: (String) -> Unit,
                          createCardFn: (String, MutableList<String>, String) -> Unit
) {
    val context = LocalContext.current
    var crrAns by rememberSaveable { mutableStateOf(" ")}
    var listAnswers by rememberSaveable { mutableStateOf(listOf(mutableStateOf(""), mutableStateOf(""), mutableStateOf(""), mutableStateOf("")))}
    var checked by rememberSaveable { mutableStateOf(listOf(mutableStateOf(false), mutableStateOf(false), mutableStateOf(false), mutableStateOf(false))) }
    LazyColumn (
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
            for (index in listAnswers.indices) {
                checked[index].value = listAnswers[index].value == crrAns
            }
            item {
                Text(
                    text = "Add a new flash card",
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
                    value = question,
                    onValueChange = { onQuestionChange(it) },
                    placeholder = { Text(text = "Input question here") },
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
                Log.d("Card Screen", "Question change to is $question")
            }
            items(listAnswers.size) { index ->
                Log.d(
                    "Card Screen",
                    "Answer ${listAnswers.size}, size of input list from createViewModel ${inputAnswers.size}, each element is $inputAnswers"
                )
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
                            onCheckedChange = { it ->
                                checked[index].value = it
                                if (checked[index].value) {
                                    crrAns = listAnswers[index].value
                                } else {
                                    if (checked.all { !it.value }) {
                                        crrAns = " "
                                    }
                                }
                            }
                        )
                        Log.d("Card Screen", "Correct ans is $crrAns")
                        OutlinedTextField(
                            value = listAnswers[index].value,
                            placeholder = { Text(text = "Answer here") },
                            onValueChange = { answer ->
                                listAnswers[index].value = answer
                                inputAnswers[index] = answer
                                Log.d("Card Screen", "Input list answers is $inputAnswers")
                                createCardViewModel.updateAnswer(index, answer)
                                Log.d(
                                    "Card Screen",
                                    "Input list answers after call the update method is $inputAnswers"
                                )
                                onListAnswerChange(inputAnswers)
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
                        Log.e("Create", "changed value $listAnswers")
                    }
                }
            }
        item {
            Button(
                onClick = {
                    inputAnswers.add("")
                    listAnswers = listAnswers.toMutableList().apply { add(mutableStateOf("")) }
                    checked = checked.toMutableList().apply { add(mutableStateOf(false)) }},
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
                    val builder = AlertDialog.Builder(context)
                    if (question == "") {
                        Toast.makeText(
                            context,
                            "Could not create a flash card without question",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (inputAnswers.filter { it != "" }.map { it }.size < 2) {
                        Toast.makeText(
                            context,
                            "A flash card needs at least 2 answers",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else if (crrAns == " ") {
                        Toast.makeText(
                            context,
                            "You need to choose 1 correct answer",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        builder.setMessage("Created Card: $question")
                            .setPositiveButton("Ok") { _, _ ->
                                createCardFn(question, inputAnswers, crrAns)
                                onQuestionChange("")
                                onCrrAnswerChange("")
                                onListAnswerChange(emptyList<String>().toMutableList())
                                navController.navigate("Home")
                            }.setNegativeButton("Cancel") { dialog, _ ->
                                dialog.dismiss()
                            }
                        val alert = builder.create()
                        alert.show()
                    }
                },
            ){
                Text(text = "Save and return")
            }
        }
    }
}
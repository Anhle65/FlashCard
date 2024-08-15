package nz.ac.canterbury.seng303.flashcardapp.screens

import android.app.AlertDialog
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
import androidx.compose.runtime.remember
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

@Composable
fun CreateFlashCardScreen(navController: NavController,
                          question: String,
                          onQuestionChange: (String) -> Unit,
//                          listAnswer: MutableList<String>,
                          onListAnswerChange: (MutableList<String>) -> Unit,
                          onCrrAnswerChange: (String) -> Unit,
                          createCardFn: (String, MutableList<String>, String) -> Unit
) {
    val context = LocalContext.current
    var crrAns = ""
    var listAns by rememberSaveable { mutableStateOf(listOf(mutableStateOf(""), mutableStateOf(""), mutableStateOf(""), mutableStateOf("")))}
    var checked by remember { mutableStateOf(listOf(mutableStateOf(false), mutableStateOf(false), mutableStateOf(false), mutableStateOf(false))) }
    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .padding(16.dp)
//            .verticalScroll(rememberScrollState())
            .drawBehind {
                drawRoundRect(
                    color = Color.Cyan,
                    cornerRadius = CornerRadius(16.dp.toPx()),
                )
            }
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(16.dp)),
//            .verticalScroll(true),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        item {
            Text(text = "Add a new flash card",
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
                onValueChange = {onQuestionChange(it)},
                label = { Text("Input question here") },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .drawBehind {
                        drawRoundRect(
                            color = Color.LightGray,
                            cornerRadius = CornerRadius(16.dp.toPx()),
                        )
                    }
            )
        }
        items(listAns.size) { index ->
            Log.d("Card Screen", "Answer $(listAns[index])")
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Row (
                    modifier = Modifier
                        .fillMaxSize()
                        .fillMaxWidth()
                        .padding(8.dp),
                ){
                    Checkbox(
                        checked = checked[index].value,
                        onCheckedChange = { checked[index].value = it
                        crrAns = listAns[index].value}
                    )
                    Log.d("Card Screen", "Correct ans is $crrAns")
                    OutlinedTextField(
                        value = listAns[index].value,
                        onValueChange = {listAns[index].value = it},
//                                        listAnswer[index] = it},
                        label = { Text("Empty answer now") },
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .drawBehind {
                                drawRoundRect(
                                    color = Color.LightGray,
                                    cornerRadius = CornerRadius(16.dp.toPx()),
                                )
                            }
                        )
                    Log.e("Create", "changed value $listAns")
                }
            }
        }
        item {
            Button(
                onClick = {
                    listAns = listAns.toMutableList().apply { add(mutableStateOf("")) }
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
                    } else if (listAns == emptyList<String>()) {
                        Toast.makeText(
                            context,
                            "Could not create a flash card without answer",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else if (crrAns == "") {
                        Toast.makeText(
                            context,
                            "Could not create a flash card without correct answer",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        builder.setMessage("Created Card: $question")
                            .setPositiveButton("Ok") { dialog, id -> /* Run some code on click */
                                createCardFn(question,listAns.toMutableList(), crrAns)
                                onQuestionChange("")
                                onCrrAnswerChange("")
                                onListAnswerChange(emptyList<String>().toMutableList())
                                navController.navigate("cardList")
                            }.setNegativeButton("Cancel") { dialog, id ->
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
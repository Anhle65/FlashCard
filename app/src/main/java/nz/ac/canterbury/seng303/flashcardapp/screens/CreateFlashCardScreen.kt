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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
                          listAnswer: MutableList<String>,
                          onListAnswerChange: (MutableList<String>) -> Unit,
                          onCrrAnswerChange: (String) -> Unit,
                          createCardFn: (String, MutableList<String>, String) -> Unit
) {
    val context = LocalContext.current
    var crrAns = ""
    var listAns by rememberSaveable { mutableStateOf(listOf(mutableStateOf(""), mutableStateOf(""), mutableStateOf(""), mutableStateOf("")))}
    Column (
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .padding(16.dp)
            .drawBehind {
                drawRoundRect(
                    color = Color.Cyan,
                    cornerRadius = CornerRadius(15.dp.toPx()),
                )
            }
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(16.dp)),
        Arrangement.spacedBy(20.dp)
    ){
        Text(text = "Add a new flash card",
            textAlign = TextAlign.Center,
            style = TextStyle(
                color = Color.Black,
                fontSize = MaterialTheme.typography.headlineLarge.fontSize
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp)
        )
        OutlinedTextField(
            value = question,
            onValueChange = {onQuestionChange(it)},
            label = { Text("Input question here") },
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .drawBehind {
                    drawRoundRect(
                        color = Color.LightGray,
                        cornerRadius = CornerRadius(15.dp.toPx()),
                    )
                }
        )
        var checked by remember { mutableStateOf(listOf(mutableStateOf(false), mutableStateOf(false), mutableStateOf(false), mutableStateOf(false))) }
        LazyColumn(modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(5.dp)) {
            items(listAns.size) { index ->
                Log.d("Card Screen", "Answer $(listAns[index])")

//                var changedAns by rememberSaveable { mutableStateOf(listAns[index]) }
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
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
                            label = { Text("Empty answer now") },
                            modifier = Modifier
                                .padding(horizontal = 5.dp)
                                .drawBehind {
                                    drawRoundRect(
                                        color = Color.LightGray,
                                        cornerRadius = CornerRadius(15.dp.toPx()),
                                    )
                                }
                        )
                        Log.e("Create", "changed value $listAns")
                    }

                }

            }
        }
        Button(
            onClick = {
                val builder = AlertDialog.Builder(context)
                if (question != "" && listAnswer != emptyList<String>() && crrAns != "") {
                    builder.setMessage("Created Card: $question")
                        .setPositiveButton("Ok") { dialog, id -> /* Run some code on click */
                            createCardFn(question, listAnswer, crrAns)
                            onQuestionChange("")
                            onCrrAnswerChange("")
                            onListAnswerChange(emptyList<String>().toMutableList())
                            navController.navigate("cardList")
                        }.setNegativeButton("Cancel") { dialog, id ->
                            dialog.dismiss()
                        }
                    val alert = builder.create()
                    alert.show()
                } else {
                    Toast.makeText(context,"Could not create a flash card without question or content or an correct answer", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth(),
        ) {
//            Icon(imageVector = Icons.Outlined.Create,
//                contentDescription = "Save",
//                tint = Color.Green
//            )
            Text(text = "Save")
        }
    }
}
package nz.ac.canterbury.seng303.flashcardapp.viewmodels
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
class CreateCardViewModel: ViewModel() {
    var question by mutableStateOf("")
        private set
    fun updateQuestion(newQuestion: String) {
        question = newQuestion
    }

    var listAns =  mutableListOf("","","","")
//    var listAns by mutableStateOf(listOf("", "", "", "" ))
        private set

    fun setAnswers(newAnswers: MutableList<String>) {
        listAns.toMutableList().clear()
        listAns.toMutableList().addAll(newAnswers)
    }

    var correctAns by mutableStateOf("")
        private set

    fun updateCorrectAnswer(newAnswer: String) {
        correctAns = newAnswer
    }
}
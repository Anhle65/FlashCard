package nz.ac.canterbury.seng303.flashcardapp.viewmodels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
class CreateCardViewModel: ViewModel() {
    var question by mutableStateOf("")
        private set
    fun updateQuestion(newQuestion: String) {
        question = newQuestion
    }

    var listAns = mutableStateListOf("","","","")
        private set

    fun setAnswers(newAnswers: MutableList<String>) {
        listAns.clear()
        listAns.addAll(newAnswers)
    }

    var correctAns by mutableStateOf("")
        private set

    fun updateCorrectAnswer(newAnswer: String) {
        correctAns = newAnswer
    }
}
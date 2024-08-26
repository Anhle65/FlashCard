package nz.ac.canterbury.seng303.flashcardapp.viewmodels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
class CreateCardViewModel: ViewModel() {
    var question by mutableStateOf("")
        private set
    fun updateQuestion(newQuestion: String) {
        question = newQuestion
    }

    var listAns =  mutableListOf("","","","")
        private set

    fun setAnswers(newAnswers: MutableList<String>) {
        listAns.clear()
        listAns.addAll(newAnswers)
    }

    fun updateAnswer(index: Int, newAnswer: String) {
        if (index < listAns.size) {
            listAns[index] = newAnswer
        }
    }

    var correctAns by mutableStateOf("")
        private set

    fun updateCorrectAnswer(newAnswer: String) {
        correctAns = newAnswer
    }
}
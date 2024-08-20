package nz.ac.canterbury.seng303.flashcardapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import nz.ac.canterbury.seng303.flashcardapp.models.FlashCard

class EditCardViewModel:ViewModel() {
    var question by mutableStateOf("")
        private set
    fun updateQuestion(newQuestion: String) {
        question = newQuestion
    }

//    var listAns = mutableListOf("","","","")
    var listAns by mutableStateOf(listOf("", "", "", "" ))
        private set

    fun updateAnswer(index: Int, newAnswer: String) {
        if (index < listAns.size) {
            listAns.toMutableList()[index] = newAnswer
        }
    }

    var correctAns by mutableStateOf("")
        private set

    fun updateCorrectAnswer(newAnswer: String) {
        correctAns = newAnswer
    }

    fun setAnswers(newAnswers: MutableList<String>) {
        listAns.toMutableList().clear()
        listAns.toMutableList().addAll(newAnswers)
    }

    fun setDefaultValues(selectedCard: FlashCard?) {
        selectedCard?.let {
            question = it.question
            listAns = it.listAnswer
            correctAns = it.correctAnswer
        }
    }
}
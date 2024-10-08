package nz.ac.canterbury.seng303.flashcardapp.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng303.flashcardapp.datastore.Storage
import nz.ac.canterbury.seng303.flashcardapp.models.FlashCard
import kotlin.random.Random

class FlashCardViewModel(private val cardStorage: Storage<FlashCard>): ViewModel(){
    private val _cards = MutableStateFlow<List<FlashCard>>(emptyList())
    val cards: StateFlow<List<FlashCard>> get() = _cards
    private val _selectedCard = MutableStateFlow<FlashCard?>(null)
    val selectedCard: StateFlow<FlashCard?> = _selectedCard
    var results = mutableMapOf<FlashCard, Boolean>()
        private set

    fun addResultCard(flashCard: FlashCard, isCorrect: Boolean) {
        results[flashCard] = isCorrect
        Log.d("VIEW_MODEL", "${flashCard.question}, is correct $isCorrect")
    }

    var ranking = mutableMapOf<String, Int>()
        private set

    var sortedRanking = mutableMapOf<String, Int>()
        private set

    fun sortedRanking(playerName: String, score: Int) {
        if (ranking.keys.size < 11) {
            ranking[playerName] = score
        } else {
            if (ranking.keys.size > 0) {
                val minScore = ranking.entries.sortedWith(
                    compareByDescending<Map.Entry<String, Int>> { it.value}
                        .thenBy { it.key }).last()
                if (score >= minScore.value) {
                    if (minScore.key.compareTo(playerName) > 0) {
                        ranking.remove(minScore.key)
                        ranking[playerName] = score
                    }
                }
            }
        }
        val sorted = ranking.entries.sortedWith(
            compareByDescending<Map.Entry<String, Int>> { it.value}
                .thenBy { it.key })
        sortedRanking.clear()
        for (item in sorted) {
            sortedRanking[item.key] = item.value
            Log.d("VIEW_MODEL", "Print sorted map $sortedRanking")
        }
        Log.d("VIEW_MODEL", "Print map after sorted $sortedRanking and ranking $ranking")
    }

    var totalQuestion = 0
        private set

    fun setTotalQuestion(total: Int) {
        totalQuestion = total
    }
    fun resetResult(){
        results.clear()
    }
    var playerName by mutableStateOf("")
        private set

    fun setName(name: String) {
        playerName = name
    }

    var counterCorrect by mutableIntStateOf(0)
        private set

    fun resetCounter() {
        counterCorrect = 0
    }
    fun incrementCorrectCounter() {
        counterCorrect += 1
    }

    fun getCardById(cardId: Int?) = viewModelScope.launch {
        if (cardId != null) {
            _selectedCard.value = cardStorage.get { it.getIdentifier() == cardId }.first()
        } else {
            _selectedCard.value = null
        }
    }

    fun getCards() = viewModelScope.launch {
        cardStorage.getAll()
            .catch { Log.e("NOTE_VIEW_MODEL", it.toString()) }
            .collect { _cards.emit(it) }
    }

    fun createCard(question: String, listAnswer: MutableList<String>, correctAns: String) =
        viewModelScope.launch {
            val card = FlashCard(
                id = Random.nextInt(0, Int.MAX_VALUE),
                question = question,
                listAnswer = listAnswer,
                correctAnswer = correctAns,
            )
            cardStorage.insert(card).catch { Log.e("CARD_VIEW_MODEL", "Could not create card") }
                .collect()
            cardStorage.getAll().catch { Log.e("NOTE_VIEW_MODEL", it.toString()) }
                .collect { _cards.emit(it) }
        }

    fun deleteCard(cardId: Int) = viewModelScope.launch {
        Log.d("CARD_VIEW_MODEL", "Edit card: $cardId")
        cardStorage.delete(cardId).catch { Log.e("CARD_VIEW_MODEL", "Could not delete card") }.collect()
        cardStorage.getAll().catch { Log.e("CARD_VIEW_MODEL", it.toString()) }
            .collect { _cards.emit(it) }
    }

    fun editFlashCardById(cardId: Int?, card: FlashCard) = viewModelScope.launch {
        Log.d("CARD_VIEW_MODEL", "Editing flash card: $cardId")
        if (cardId != null) {
            cardStorage.edit(cardId, card).collect()
            cardStorage.getAll().catch { Log.e("CARD_VIEW_MODEL", it.toString()) }
                .collect { _cards.emit(it) }
        }
    }
}
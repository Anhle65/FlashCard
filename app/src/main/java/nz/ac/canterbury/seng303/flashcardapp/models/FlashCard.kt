package nz.ac.canterbury.seng303.flashcardapp.models

data class FlashCard (
    val id: Int,
    val question: String,
    val listAnswer: MutableList<String>,
    val correctAnswer: String): Identifiable {
    override fun getIdentifier(): Int {
        return id
    }
}

package nz.ac.canterbury.seng303.flashcardapp.models

data class FlashCard (
    val id: Int,
    val question: String,
    val listAnswer: MutableList<String>,
    val correctAnswer: String): Identifiable {
    companion object {
        fun getCards(): List<FlashCard> {
            return listOf(
                FlashCard(
                    1,
                    "Is today wednesday",
                    mutableListOf("True", "False"," Not sure"),
                    "true",
                ),
                FlashCard(
                    2,
                    "What is the color",
                    mutableListOf("Blue", "Green", "Yellow"),
                    "Green"
                ),
                FlashCard(
                    3,
                    "Where is the cat",
                    mutableListOf("Sofa", "Bedroom", "Don't know", "Kitchen"),
                    "Sofa"
                )
            )
        }
    }
    override fun getIdentifier(): Int {
        return id
    }
}

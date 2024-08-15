package nz.ac.canterbury.seng303.flashcardapp.models;

data class FlashCard (
    val id: Int,
    val question: String,
    val listAnswer: MutableList<String>,
    val correctAnswer: String,
    val isChosen: Boolean): Identifiable {
    companion object {
        fun getCards(): List<FlashCard> {
            return listOf(
                FlashCard(
                    1,
                    "Is today wednesday",
                    mutableListOf("True", "False"," Not sure"),
                    "true",
                    false
                ),
                FlashCard(
                    2,
                    "What is the color",
                    mutableListOf("Blue", "Green", "Yellow"),
                    "Green",
                    false
                ),
                FlashCard(
                    3,
                    "Where is the cat",
                    mutableListOf("Sofa", "Bedroom", "Don't know", "Kitchen"),
                    "Sofa",
                    false
                )
            )
        }
    }
    override fun getIdentifier(): Int {
        return id;
    }
}

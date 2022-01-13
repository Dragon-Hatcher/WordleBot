import java.io.File
import java.util.regex.Pattern

class Dictionary(private val wordsString: String) {
    val words = wordsString.split("\n").map { it.trim() }.filter { it.length == 5 }

    fun countWithGuesses(guesses: Guesses) =
        Pattern.compile(guesses.toRegexString()).matcher(wordsString).results().count()

    fun constructWithGuesses(guesses: Guesses): Dictionary {
        val matches = guesses.toRegexString().toRegex().findAll(wordsString)
        return Dictionary(matches.joinToString(separator = "\n") { it.value })
    }

    companion object {
        fun fromFile(name: String): Dictionary = Dictionary(File(name).readText())
    }
}
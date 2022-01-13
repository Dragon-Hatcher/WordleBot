import kotlin.math.min

fun makeNextGuess(guesses: Guesses, dictionary: Dictionary): String {
    var bestWord = ""
    var bestScore = Double.MAX_VALUE
    for ((i, word) in dictionary.words.withIndex()) {
        val score = when (dictionary.words.size > 500) {
            true -> {
                val fuzzy = fuzzyEvalWord(guesses, word, dictionary)
                if (fuzzy < bestScore) strongEvalWord(guesses, word, dictionary) else fuzzy
            }
            else -> strongEvalWord(guesses, word, dictionary)
        }
        if (score < bestScore) {
            println("New Best: $word @ $score")
            bestWord = word
            bestScore = score
        }
        if (dictionary.words.size > 500 && i % 50 == 0) println("$i / ${dictionary.words.size}")
    }
    return bestWord
}

private fun strongEvalWord(guesses: Guesses, nextGuess: String, dictionary: Dictionary) =
    dictionary.words.map { answer ->
        val newGuesses = guesses.plus(Guess.fromWordAndAnswer(nextGuess, answer))
        dictionary.countWithGuesses(newGuesses)
    }.average()


private fun fuzzyEvalWord(guesses: Guesses, nextGuess: String, dictionary: Dictionary) =
    dictionary.words.shuffled().subList(0, min(100, dictionary.words.size)).map { answer ->
        val newGuesses = guesses.plus(Guess.fromWordAndAnswer(nextGuess, answer))
        dictionary.countWithGuesses(newGuesses)
    }.average()

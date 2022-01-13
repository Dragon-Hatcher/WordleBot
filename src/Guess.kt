class Guesses(private val guesses: List<Guess> = listOf()) {
    fun toRegexString(): String {
        val columnLetters = MutableList<Char?>(LETTER_COUNT) { null }
        val universalExclusions = mutableSetOf<Char>()
        val columnExclusions = List(LETTER_COUNT) { mutableSetOf<Char>() }
        val requiredLetters = mutableSetOf<Char>()
        for (guess in guesses) {
            for (i in 0 until LETTER_COUNT) {
                val letter = guess.word[i]
                when (guess.results[i]) {
                    LetterResult.NONEXISTENT -> universalExclusions.add(letter)
                    LetterResult.EXISTS -> {
                        requiredLetters.add(letter)
                        columnExclusions[i].add(letter)
                    }
                    LetterResult.CORRECT -> columnLetters[i] = letter
                }
            }
        }

        val ensureHasLetter = when {
            requiredLetters.isEmpty() -> ""
            else -> requiredLetters.joinToString(separator = "") { "(?=\\w*$it\\w*)" }
        }
        val lettersPortion = (0 until LETTER_COUNT).joinToString(separator = "") {
            when {
                columnLetters[it] != null -> columnLetters[it].toString()
                columnExclusions[it].isEmpty() && universalExclusions.isEmpty() -> "."
                else -> "[^${(columnExclusions[it]+universalExclusions).joinToString(separator = "")}]"
            }
        }
        return "(?m)^$ensureHasLetter$lettersPortion$"
    }

    fun plus(new: Guess): Guesses = Guesses(guesses + new)

    fun expandFromTerminal(word: String): Guesses = plus(Guess.fromTerminal(word))

    fun expandFromTerminal(): Guesses = plus(Guess.fromTerminal())
}

data class Guess(val word: String, val results: List<LetterResult>) {
    companion object {
        fun fromTerminal(word: String): Guess {
            val input = readLine()!!.trim()
            val results = input.map {
                when (it) {
                    '#' -> LetterResult.CORRECT
                    '-' -> LetterResult.EXISTS
                    else -> LetterResult.NONEXISTENT
                }
            }
            return Guess(word, results)
        }

        fun fromTerminal(): Guess {
            val input = readLine()!!.trim()
            val parts = input.split(" ")
            val word = parts[0]
            val results = parts[1].map {
                when (it) {
                    '#' -> LetterResult.CORRECT
                    '-' -> LetterResult.EXISTS
                    else -> LetterResult.NONEXISTENT
                }
            }
            return Guess(word, results)
        }

        fun fromWordAndAnswer(word: String, answer: String): Guess {
            val results = word.withIndex().map { (index, char) ->
                when {
                    answer[index] == char -> LetterResult.CORRECT
                    answer.contains(char) -> LetterResult.EXISTS
                    else -> LetterResult.NONEXISTENT
                }
            }
            return Guess(word, results)
        }
    }
}

enum class LetterResult {
    NONEXISTENT, EXISTS, CORRECT
}
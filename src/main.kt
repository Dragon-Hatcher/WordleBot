fun main() {
    var dict = Dictionary.fromFile("src/dict.txt")
    var guesses = Guesses()
    var word = "rates"
    do {
        println("Guess: $word")
        guesses = guesses.expandFromTerminal(word)
        dict = dict.constructWithGuesses(guesses)
        println("Remaining words: ${dict.words.size} ${dict.words}")
        word = makeNextGuess(guesses, dict)
        if (dict.words.size == 1) {
            println("The answer is $word.")
            break
        }
    } while (true)
}
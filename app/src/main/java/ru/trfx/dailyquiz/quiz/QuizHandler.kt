package ru.trfx.dailyquiz.quiz

object QuizHandler {
    var currentQuiz: Quiz? = null
        set(value) {
            field = value
            answers.clear()
        }

    val questionCount get() = currentQuiz!!.questions.size

    var answers = mutableListOf<Int>()

    fun recordAnswer(index: Int) {
        answers += index
    }

    fun getQuestion(number: Int) = currentQuiz!!.questions[number - 1]
}

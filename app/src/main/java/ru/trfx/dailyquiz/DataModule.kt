package ru.trfx.dailyquiz

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.trfx.dailyquiz.quiz.repository.MockQuizRepository
import ru.trfx.dailyquiz.quiz.repository.QuizRepository

internal val dataModule = module {
    singleOf(::MockQuizRepository) bind QuizRepository::class
}

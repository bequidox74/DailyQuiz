package ru.trfx.dailyquiz.ui

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.trfx.dailyquiz.quiz.screen.QuizScreenViewModel

internal val uiModule = module {
    viewModelOf(::QuizScreenViewModel)
}

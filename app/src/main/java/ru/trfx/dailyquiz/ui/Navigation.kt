package ru.trfx.dailyquiz.ui

import com.ramcosta.composedestinations.generated.navgraphs.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

fun DestinationsNavigator.navigateToHome() {
    navigate(RootNavGraph.defaultStartDirection) {
        popUpTo(RootNavGraph.defaultStartDirection) {
            inclusive = true
        }
    }
}

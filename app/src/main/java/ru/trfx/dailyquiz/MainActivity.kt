package ru.trfx.dailyquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import org.koin.compose.KoinApplication
import ru.trfx.dailyquiz.ui.theme.DailyQuizTheme
import ru.trfx.dailyquiz.ui.uiModule
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        enableEdgeToEdge()
        setContent {
            KoinApplication(application = {
                modules(uiModule)
            }) {
                DailyQuizTheme {
                    DestinationsNavHost(NavGraphs.root)
                }
            }
        }
    }
}

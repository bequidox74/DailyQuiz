Задание по Android-разработке для летней школы Surf (Surf Summer School 2025).

[YouTube](https://www.youtube.com/watch?v=jZ_2T-PYPik) - [APK](https://github.com/AlexanderLaptev/DailyQuiz/releases/tag/release)
(YouTube загружает видео как short -_-)

# Общая информация
- Android 9+
- Все дополнительные задания
- "All warnings are errors" (см. `build.gradle.kts` в модуле `app`)
- Без вайбкода (не пригодился)

# Стек
- Jetpack Compose — UI
- Retrofit — запросы к API
- KotlinX Serialization — парсинг JSON
- Compose Destinations — адекватная навигация
- Room — SQLite-база данных
- Timber — удобные логи
- Koin — DI-фреймворк

# Замечания
- 90% времени приложение разрабатывалось с моком репозитория викторин. Добавление сети и базы данных стало тривиальным благодаря DI.
- Скорость написания кода была в приоритете (например, где-то нет вьюмодели, ибо логики и UI state'а достаточно мало).
- Попап с удалением не реализован, вместо него стандартный диалог. *Очень большие* сложности с затемнением фона, кроме одного элемента, когда composable в разных layout'ах. Времени и желания писать свой layout не было.

package com.Zomfaka3.Tokpor.presentation.app

import androidx.lifecycle.ViewModel
import com.Zomfaka3.Tokpor.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val backStack = mutableListOf<Screen>()

    private val current = MutableStateFlow<Screen>(Screen.Splash)

    val screen: StateFlow<Screen> = current.asStateFlow()

    fun navigate(target: Screen) {
        backStack.add(current.value)
        current.value = target
    }

    fun replaceCurrent(target: Screen) {
        current.value = target
    }

    fun finishSplash() {
        backStack.clear()
        current.value = Screen.Menu
    }

    fun home() {
        backStack.clear()
        current.value = Screen.Menu
    }

    fun back() {
        val previous = backStack.removeLastOrNull()
        current.value = previous ?: Screen.Menu
    }
}

package org.example.project.adapters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import org.example.project.extensions.mapToStateFlow
import org.example.project.interfaces.IStepperItem

sealed class StepperNavigationTransition {
    data class Forward(val step: IStepperItem) : StepperNavigationTransition()
    data class Back(val step: IStepperItem) : StepperNavigationTransition()
}

class StepperAdapter(
    private val totalSteps: List<IStepperItem>
) : ViewModel() {

    private val _currentStepItem = MutableStateFlow(totalSteps.firstOrNull()) // Item текущего шага
    private val _currentStepIndex: Int get() = totalSteps.indexOf(_currentStepItem.value) // Индекс текущего шага
    private val _currentStepNumber: MutableStateFlow<Int> = _currentStepItem.mapToStateFlow(1, viewModelScope) { totalSteps.indexOf(it) + 1 } // номер текущего шага
    private val _transitionActions = Channel<StepperNavigationTransition>(Channel.BUFFERED)

    val currentStepItem: StateFlow<IStepperItem?> = _currentStepItem
    val currentStepNumber: StateFlow<Int> = _currentStepNumber
    val transitionActions = _transitionActions.receiveAsFlow() // События переходов
    val canGoBack: Boolean get() = _currentStepIndex > 0
    val stepCount: Int = totalSteps.size

    fun next() = moveToIndex(_currentStepIndex + 1)  // на следующий шаг
    fun back() = moveToIndex(_currentStepIndex - 1)  // на предыдущий шаг
    fun doMoveToRoot() = moveToIndex(0)    // на первый шаг

    fun doMoveToStep(step: IStepperItem) = moveToIndex(totalSteps.indexOf(step)) // на конкретный шаг

    private fun moveToIndex(target: Int) {
        if (target !in totalSteps.indices || target == _currentStepIndex) return   // вне списка или тот же шаг
        val targetStep = totalSteps[target]
        _transitionActions.trySend(
            if (target > _currentStepIndex) StepperNavigationTransition.Forward(targetStep)
            else StepperNavigationTransition.Back(targetStep)
        )
        _currentStepItem.value = targetStep
    }
}

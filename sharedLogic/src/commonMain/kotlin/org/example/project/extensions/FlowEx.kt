package org.example.project.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

inline fun <T, R> Flow<T>.mapToStateFlow(
    initialValue:R,
    scope: CoroutineScope,
    crossinline transform: suspend (value:T) ->R
): MutableStateFlow<R>{
    val stateFlow = MutableStateFlow(initialValue)

    scope.launch {
        collect{value ->
            stateFlow.value = transform(value)
        }
    }

    return stateFlow
}

fun <T1, T2, R> combineStateFlow(
    initialValue:R,
    scope: CoroutineScope,
    flow1:MutableStateFlow<T1>,
    flow2:MutableStateFlow<T2>,
    transform: (T1, T2) -> R,
):MutableStateFlow<R>{
    val stateFlow = MutableStateFlow(initialValue)

    scope.launch {
        flow1.collectLatest { value1 ->
            stateFlow.value = transform(value1, flow2.value)
        }
    }

    scope.launch {
        flow2.collectLatest { value2 ->
            stateFlow.value = transform(flow1.value, value2)
        }
    }
    return stateFlow
}

fun <T1, T2, T3, R> combineStateFlow(
    initialValue: R,
    scope: CoroutineScope,
    flow1: MutableStateFlow<T1>,
    flow2: MutableStateFlow<T2>,
    flow3: MutableStateFlow<T3>,
    transform: (T1, T2, T3) -> R,
): MutableStateFlow<R> {
    val stateFlow = MutableStateFlow(initialValue)

    scope.launch {
        flow1.collectLatest { value1 ->
            stateFlow.value = transform(value1, flow2.value, flow3.value)
        }
    }

    scope.launch {
        flow2.collectLatest { value2 ->
            stateFlow.value = transform(flow1.value, value2, flow3.value)
        }
    }

    scope.launch {
        flow3.collectLatest { value3 ->
            stateFlow.value = transform(flow1.value, flow2.value, value3)
        }
    }
    return stateFlow
}

fun <T1, T2, T3, T4, R> combineStateFlow(
    initialValue: R,
    scope: CoroutineScope,
    flow1: MutableStateFlow<T1>,
    flow2: MutableStateFlow<T2>,
    flow3: MutableStateFlow<T3>,
    flow4: MutableStateFlow<T4>,
    transform: (T1, T2, T3, T4) -> R,
): MutableStateFlow<R> {
    val stateFlow = MutableStateFlow(initialValue)

    scope.launch {
        flow1.collectLatest { value1 ->
            stateFlow.value = transform(value1, flow2.value, flow3.value, flow4.value)
        }
    }

    scope.launch {
        flow2.collectLatest { value2 ->
            stateFlow.value = transform(flow1.value, value2, flow3.value, flow4.value)
        }
    }

    scope.launch {
        flow3.collectLatest { value3 ->
            stateFlow.value = transform(flow1.value, flow2.value, value3, flow4.value)
        }
    }

    scope.launch {
        flow4.collectLatest { value4 ->
            stateFlow.value = transform(flow1.value, flow2.value, flow3.value, value4)
        }
    }

    return stateFlow
}

fun <T1, T2, T3, T4, T5, R> combineStateFlow(
    initialValue: R,
    scope: CoroutineScope,
    flow1: MutableStateFlow<T1>,
    flow2: MutableStateFlow<T2>,
    flow3: MutableStateFlow<T3>,
    flow4: MutableStateFlow<T4>,
    flow5: MutableStateFlow<T5>,
    transform: (T1, T2, T3, T4, T5) -> R,
): MutableStateFlow<R> {
    val stateFlow = MutableStateFlow(initialValue)

    scope.launch {
        flow1.collectLatest { value1 ->
            stateFlow.value = transform(value1, flow2.value, flow3.value, flow4.value, flow5.value)
        }
    }

    scope.launch {
        flow2.collectLatest { value2 ->
            stateFlow.value = transform(flow1.value, value2, flow3.value, flow4.value, flow5.value)
        }
    }

    scope.launch {
        flow3.collectLatest { value3 ->
            stateFlow.value = transform(flow1.value, flow2.value, value3, flow4.value, flow5.value)
        }
    }

    scope.launch {
        flow4.collectLatest { value4 ->
            stateFlow.value = transform(flow1.value, flow2.value, flow3.value, value4, flow5.value)
        }
    }

    scope.launch {
        flow5.collectLatest { value5 ->
            stateFlow.value = transform(flow1.value, flow2.value, flow3.value, flow4.value, value5)
        }
    }
    return stateFlow
}

fun <T1, T2, T3, T4, T5, T6, R> combineStateFlow(
    initialValue: R,
    scope: CoroutineScope,
    flow1: MutableStateFlow<T1>,
    flow2: MutableStateFlow<T2>,
    flow3: MutableStateFlow<T3>,
    flow4: MutableStateFlow<T4>,
    flow5: MutableStateFlow<T5>,
    flow6: MutableStateFlow<T6>,
    transform: (T1, T2, T3, T4, T5, T6) -> R,
): MutableStateFlow<R> {
    val stateFlow = MutableStateFlow(initialValue)

    scope.launch {
        flow1.collectLatest { value1 ->
            stateFlow.value =
                transform(value1, flow2.value, flow3.value, flow4.value, flow5.value, flow6.value)
        }
    }

    scope.launch {
        flow2.collectLatest { value2 ->
            stateFlow.value =
                transform(flow1.value, value2, flow3.value, flow4.value, flow5.value, flow6.value)
        }
    }

    scope.launch {
        flow3.collectLatest { value3 ->
            stateFlow.value =
                transform(flow1.value, flow2.value, value3, flow4.value, flow5.value, flow6.value)
        }
    }

    scope.launch {
        flow4.collectLatest { value4 ->
            stateFlow.value =
                transform(flow1.value, flow2.value, flow3.value, value4, flow5.value, flow6.value)
        }
    }

    scope.launch {
        flow5.collectLatest { value5 ->
            stateFlow.value =
                transform(flow1.value, flow2.value, flow3.value, flow4.value, value5, flow6.value)
        }
    }

    scope.launch {
        flow6.collectLatest { value6 ->
            stateFlow.value =
                transform(flow1.value, flow2.value, flow3.value, flow4.value, flow5.value, value6)
        }
    }
    return stateFlow
}

fun <T1, T2, T3, T4, T5, T6, T7, R> combineStateFlow(
    initialValue: R,
    scope: CoroutineScope,
    flow1: MutableStateFlow<T1>,
    flow2: MutableStateFlow<T2>,
    flow3: MutableStateFlow<T3>,
    flow4: MutableStateFlow<T4>,
    flow5: MutableStateFlow<T5>,
    flow6: MutableStateFlow<T6>,
    flow7: MutableStateFlow<T7>,
    transform: (T1, T2, T3, T4, T5, T6, T7) -> R,
): MutableStateFlow<R> {
    val stateFlow = MutableStateFlow(initialValue)

    scope.launch {
        flow1.collectLatest { value1 ->
            stateFlow.value =
                transform(
                    value1,
                    flow2.value,
                    flow3.value,
                    flow4.value,
                    flow5.value,
                    flow6.value,
                    flow7.value
                )
        }
    }

    scope.launch {
        flow2.collectLatest { value2 ->
            stateFlow.value =
                transform(
                    flow1.value,
                    value2,
                    flow3.value,
                    flow4.value,
                    flow5.value,
                    flow6.value,
                    flow7.value
                )
        }
    }

    scope.launch {
        flow3.collectLatest { value3 ->
            stateFlow.value =
                transform(
                    flow1.value,
                    flow2.value,
                    value3,
                    flow4.value,
                    flow5.value,
                    flow6.value,
                    flow7.value
                )
        }
    }

    scope.launch {
        flow4.collectLatest { value4 ->
            stateFlow.value =
                transform(
                    flow1.value,
                    flow2.value,
                    flow3.value,
                    value4,
                    flow5.value,
                    flow6.value,
                    flow7.value
                )
        }
    }

    scope.launch {
        flow5.collectLatest { value5 ->
            stateFlow.value =
                transform(
                    flow1.value,
                    flow2.value,
                    flow3.value,
                    flow4.value,
                    value5,
                    flow6.value,
                    flow7.value
                )
        }
    }

    scope.launch {
        flow6.collectLatest { value6 ->
            stateFlow.value =
                transform(
                    flow1.value,
                    flow2.value,
                    flow3.value,
                    flow4.value,
                    flow5.value,
                    value6,
                    flow7.value
                )
        }
    }

    scope.launch {
        flow7.collectLatest { value7 ->
            stateFlow.value =
                transform(
                    flow1.value,
                    flow2.value,
                    flow3.value,
                    flow4.value,
                    flow5.value,
                    flow6.value,
                    value7
                )
        }
    }
    return stateFlow
}

fun <T1, T2, T3, T4, T5, T6, T7, T8, R> combineStateFlow(
    initialValue: R,
    scope: CoroutineScope,
    flow1: MutableStateFlow<T1>,
    flow2: MutableStateFlow<T2>,
    flow3: MutableStateFlow<T3>,
    flow4: MutableStateFlow<T4>,
    flow5: MutableStateFlow<T5>,
    flow6: MutableStateFlow<T6>,
    flow7: MutableStateFlow<T7>,
    flow8: MutableStateFlow<T8>,
    transform: (T1, T2, T3, T4, T5, T6, T7, T8) -> R,
): MutableStateFlow<R> {
    val stateFlow = MutableStateFlow(initialValue)

    scope.launch {
        flow1.collectLatest { value1 ->
            stateFlow.value =
                transform(
                    value1,
                    flow2.value,
                    flow3.value,
                    flow4.value,
                    flow5.value,
                    flow6.value,
                    flow7.value,
                    flow8.value
                )
        }
    }

    scope.launch {
        flow2.collectLatest { value2 ->
            stateFlow.value =
                transform(
                    flow1.value,
                    value2,
                    flow3.value,
                    flow4.value,
                    flow5.value,
                    flow6.value,
                    flow7.value,
                    flow8.value
                )
        }
    }

    scope.launch {
        flow3.collectLatest { value3 ->
            stateFlow.value =
                transform(
                    flow1.value,
                    flow2.value,
                    value3,
                    flow4.value,
                    flow5.value,
                    flow6.value,
                    flow7.value,
                    flow8.value
                )
        }
    }

    scope.launch {
        flow4.collectLatest { value4 ->
            stateFlow.value =
                transform(
                    flow1.value,
                    flow2.value,
                    flow3.value,
                    value4,
                    flow5.value,
                    flow6.value,
                    flow7.value,
                    flow8.value
                )
        }
    }

    scope.launch {
        flow5.collectLatest { value5 ->
            stateFlow.value =
                transform(
                    flow1.value,
                    flow2.value,
                    flow3.value,
                    flow4.value,
                    value5,
                    flow6.value,
                    flow7.value,
                    flow8.value
                )
        }
    }

    scope.launch {
        flow6.collectLatest { value6 ->
            stateFlow.value =
                transform(
                    flow1.value,
                    flow2.value,
                    flow3.value,
                    flow4.value,
                    flow5.value,
                    value6,
                    flow7.value,
                    flow8.value
                )
        }
    }

    scope.launch {
        flow7.collectLatest { value7 ->
            stateFlow.value =
                transform(
                    flow1.value,
                    flow2.value,
                    flow3.value,
                    flow4.value,
                    flow5.value,
                    flow6.value,
                    value7,
                    flow8.value
                )
        }
    }

    scope.launch {
        flow8.collectLatest { value8 ->
            stateFlow.value =
                transform(
                    flow1.value,
                    flow2.value,
                    flow3.value,
                    flow4.value,
                    flow5.value,
                    flow6.value,
                    flow7.value,
                    value8
                )
        }
    }
    return stateFlow
}

fun <T> Flow<T>.asMutableStateFlow(initialValue: T, scope:CoroutineScope):MutableStateFlow<T>{
    val stateFlow = MutableStateFlow(initialValue)
    this.onEach { value->
        stateFlow.value = value
    }.launchIn(scope)
    return stateFlow
}
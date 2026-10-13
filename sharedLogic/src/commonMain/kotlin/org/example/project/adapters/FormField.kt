package org.example.project.adapters

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import org.example.project.extensions.combineStateFlow
import org.example.project.extensions.mapToStateFlow

open class FormField<D>(
    private var initialValue: D,
    private var scope: CoroutineScope,
    private var validation: (D) -> String?
) {

    val data = MutableStateFlow(initialValue)

    private val validationError: MutableStateFlow<String?> = data.mapToStateFlow(null, scope) { validation(it) }
    private val showValidationError = MutableStateFlow(false)

    val error: MutableStateFlow<String?> = combineStateFlow(
        null,
        scope,
        validationError,
        showValidationError
    ) { error, show ->
        if (show) error else null
    }

    val isValid: MutableStateFlow<Boolean> = validationError.mapToStateFlow(true, scope) { it == null }

    fun setData(value: D) {
        data.value = value
    }

    fun value(): D {
        return data.value
    }

    fun validate(): Boolean {
        showValidationError.value = true
        return isValid.value
    }

    fun resetValidation() {
        showValidationError.value = false
    }

    open fun copy(initialValue: D = this.initialValue, scope: CoroutineScope = this.scope, validation: (D) -> String? = this.validation):FormField<D>{
        return FormField(initialValue = initialValue, scope = scope, validation = validation)
    }
}

fun <D> validationBlock(block: (D) -> String?): ((D) -> String?) {
    return { block(it) }
}

fun List<FormField<*>>.validate(): Boolean =
    all { it.validate() }

fun List<FormField<*>>.validateAll(): Boolean =
    map{ it.validate()}.all { it }


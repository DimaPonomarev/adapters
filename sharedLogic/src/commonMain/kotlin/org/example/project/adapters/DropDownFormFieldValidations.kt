package org.example.project.adapters

import ru.europlan.mws.api.interfaces.IEupCheckboxCell


interface DropDownFormFieldValidations<T : IEupCheckboxCell> {
    fun value(value: T?): String?
}
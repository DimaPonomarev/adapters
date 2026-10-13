package org.example.project.adapters

import ru.europlan.mws.api.interfaces.IEupCheckboxCell
import ru.europlan.mws.api.interfaces.IEupGroup

interface GroupedDropDownFormFieldValidations<T : IEupGroup<IEupCheckboxCell>> {
    fun value(value: T?): String?
}
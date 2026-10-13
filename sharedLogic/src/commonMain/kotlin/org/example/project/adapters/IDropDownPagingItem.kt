package org.example.project.adapters

import kotlinx.coroutines.flow.MutableStateFlow
import org.example.project.interfaces.IEntity

interface IDropDownPagingItem : IEntity {
    val title: String
    val isSelected: MutableStateFlow<Boolean>

}
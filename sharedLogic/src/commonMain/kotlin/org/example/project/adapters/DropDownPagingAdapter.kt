package org.example.project.adapters

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import org.example.project.enums.SearchTypeEnum
import org.example.project.extensions.mapToStateFlow

class DropDownPagingAdapter<T : IDropDownPagingItem>(
    pageSize: Int = 15,
    searchType: SearchTypeEnum = SearchTypeEnum.NONE,
    title: String = "",
    searchTitle: String? = null,
    val isMultiSelect: Boolean = false,
    val textForButton: String = "Применить",
    loadData: (success: ((List<T>) -> Unit)?) -> Unit
) : PagingAdapter<T>(searchType, title, searchTitle, pageSize, loadData) {

    private val _isVisibleBottomSheet = MutableStateFlow(false) // Флаг для отображения BottomSheet
    private val _preselectedItems =  MutableStateFlow<List<T>>(emptyList()) // Список для работы на BottomSheet
    private val _applySelectedItems = MutableStateFlow<List<T>>(emptyList())  // Список выбранных элементов после нажатия кнопки принять

    val isVisibleBottomSheet: MutableStateFlow<Boolean> = _isVisibleBottomSheet
    val preselectedItems: MutableStateFlow<List<T>> = _preselectedItems
    val applySelectedItems: MutableStateFlow<List<T>> = _applySelectedItems
    val applySelectedItem: MutableStateFlow<T?> = _applySelectedItems.mapToStateFlow(null, viewModelScope) { it.firstOrNull() } // Выбранный элемент после нажатия кнопки принять
    val selectedValueString: MutableStateFlow<String> = _applySelectedItems.mapToStateFlow("", viewModelScope) {
        it.map { it.title }.joinToString(", ")
    } // Строка из выбранных и принятых элементов, отображаемая в DropDownText

    fun setVisibleBottomSheet(isVisible: Boolean) {
        if (isVisible) _preselectedItems.value = _applySelectedItems.value
        _isVisibleBottomSheet.value = isVisible
    }

    fun doClearSelected() {
        _preselectedItems.value = emptyList()
        _applySelectedItems.value = emptyList()
    }

    fun doSelectApply() {
        _applySelectedItems.value = _preselectedItems.value
        setVisibleBottomSheet(false)
    }

    fun applySelectedItems(): List<T> = _applySelectedItems.value

    fun doCheckedItem(item: T) {
        if (isMultiSelect) {
            _preselectedItems.value = if (_preselectedItems.value.contains(item)) _preselectedItems.value.minus(item)
            else _preselectedItems.value.plus(item)
        } else {
            _preselectedItems.value = listOf(item)
        }
    }
}
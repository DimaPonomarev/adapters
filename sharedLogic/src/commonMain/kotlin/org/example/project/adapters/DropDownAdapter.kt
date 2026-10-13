package org.example.project.adapters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import org.example.project.enums.SearchTypeEnum
import org.example.project.extensions.collectAsStateFlow
import org.example.project.extensions.combineStateFlow
import org.example.project.extensions.mapToStateFlow
import ru.europlan.mws.api.interfaces.IEupCheckboxCell

class DropDownAdapter<T : IEupCheckboxCell>(
    var title: String,
    val isMultiSelect: Boolean = false,
    val searchType: SearchTypeEnum = SearchTypeEnum.NONE,
    val validationValue: String? = null,
    val isCanClear: Boolean = true,
    val textForButton: String = "Применить"
) : ViewModel() {

    private val validations = object : DropDownFormFieldValidations<T> {
        override fun value(value: T?): String? = if (value == null) validationValue else null
    }

    private val _allItems = MutableStateFlow<List<T>>(emptyList()) // Полный список, который можно отобразить в BottomSheet
    private val _preselectedItems = MutableStateFlow<List<T>>(emptyList()) // Список для работы на BottomSheet
    private val _applySelectedItems = MutableStateFlow<List<T>>(emptyList()) // Список выбранных элементов после нажатия кнопки принять
    private val _search = MutableStateFlow("") // Строка в поле для поиска
    private val _isVisibleBottomSheet = MutableStateFlow(false) // Флаг для отображения BottomSheet

    val allItemsSize: MutableStateFlow<Int> = _allItems.mapToStateFlow(0, viewModelScope) { it.size }
    val filteredItems: MutableStateFlow<List<T>> = combineStateFlow(emptyList(), viewModelScope, _allItems, _search) { allItems, search ->
        if (search.isEmpty()) allItems else allItems.filter { it.titleCell.contains(search, true) }
    }  // Список для отображения BottomSheet с учетом поля search
    val preselectedItems: MutableStateFlow<List<T>> = _preselectedItems // Список для работы на BottomSheet
    val applySelectedItems: MutableStateFlow<List<T>> = _applySelectedItems // Список выбранных элементов после нажатия кнопки принять
    val applySelectedItem: MutableStateFlow<T?> = _applySelectedItems.mapToStateFlow(null, viewModelScope) { it.firstOrNull() } // Выбранный элемент после нажатия кнопки принять
    val search: MutableStateFlow<String> = _search // Строка в поле для поиска
    val isVisibleBottomSheet: MutableStateFlow<Boolean> = _isVisibleBottomSheet // Флаг для отображения BottomSheet
    val selectedValueString: MutableStateFlow<String> = _applySelectedItems.mapToStateFlow("", viewModelScope) {
        it.map { it.titleCell }.joinToString(", ")
    } // Строка из выбранных и принятых элементов, отображаемая в DropDownText

    val value: FormField<T?> = FormField(null, viewModelScope, validationBlock(validations::value))

    init {
        _allItems.collectAsStateFlow(viewModelScope) {
            if (searchType != SearchTypeEnum.SEARCH_FOR_QUERY) {
                _isVisibleBottomSheet.value = false
                _applySelectedItems.value = it.filter { it.isSelectedCell }
            }
        }

        _isVisibleBottomSheet.collectAsStateFlow(viewModelScope) {
            if (it) {
                if (searchType != SearchTypeEnum.SEARCH_FOR_QUERY || _applySelectedItems.value.isEmpty()) _search.value = ""
                _preselectedItems.value = _applySelectedItems.value
            }
        }

        _applySelectedItems.collectAsStateFlow(viewModelScope) {
            if (it.isNotEmpty()) _isVisibleBottomSheet.value = false
        }

        applySelectedItem.collectAsStateFlow(viewModelScope) {
            value.data.value = it
        }
    }

    fun doCheckedItem(item: T) {
        val isContains = _preselectedItems.value.find { it.idCell == item.idCell } != null

        when (isMultiSelect) {
            true -> {
                _preselectedItems.value = if (isContains) _preselectedItems.value.minus(item)
                else _preselectedItems.value.plus(item)
            }

            false -> if (isContains.not()) _preselectedItems.value = listOf(item)
        }
        filteredItems.value.map {  it.isSelectedCell = _preselectedItems.value.contains(it) }
    }

    fun setItems(items: List<T>) {
        _allItems.value = items
    }

    fun setItemsAndSelectFirst(items: List<T>) {
        _allItems.value = items
        items.firstOrNull()?.let { doSelectApply(it) }
    }

    fun setVisibleBottomSheet(isVisible: Boolean) {
        _isVisibleBottomSheet.value = isVisible
    }

    fun setSearch(text: String) {
        _search.value = text
    }

    fun doSelectApply(items: List<T>) {
        doClearSelected()
        items.onEach { it.isSelectedCell = true }
        _applySelectedItems.value = items
    }

    fun doSelectApply(item: T) {
        doSelectApply(listOf(item))
    }

    fun doClearSelected() {
        _allItems.value.forEach { it.isSelectedCell = false }
        _applySelectedItems.value = emptyList()
    }

    fun getAll(): List<T> {
        return _allItems.value
    }

    fun setNewTitle(value: String) {
        title = value
    }

    fun clearAll() {
        _allItems.value = emptyList()
        _preselectedItems.value = emptyList()
        _applySelectedItems.value = emptyList()
        _search.value = ""
        _isVisibleBottomSheet.value = false
    }
}
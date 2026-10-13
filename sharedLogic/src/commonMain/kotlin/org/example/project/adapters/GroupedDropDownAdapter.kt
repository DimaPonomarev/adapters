package org.example.project.adapters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.example.project.enums.SearchTypeEnum
import org.example.project.extensions.collectAsStateFlow
import org.example.project.extensions.combineStateFlow
import org.example.project.extensions.mapToStateFlow
import ru.europlan.mws.api.interfaces.IEupCheckboxCell
import ru.europlan.mws.api.interfaces.IEupGroup

class GroupedDropDownAdapter<T : IEupGroup<IEupCheckboxCell>>(
    val title: String,
    val isMultiSelect: Boolean = false,
    val searchType: SearchTypeEnum = SearchTypeEnum.NONE,
    val validationValue: String? = null,
    val adapterHasNonSpecifiedItem: Boolean = false,
    val textForButton: String = "Применить"
) : ViewModel() {

    private val validations = object : GroupedDropDownFormFieldValidations<T> {
        override fun value(value: T?): String? = if (value == null) validationValue else null
    }

    private val _allItems = MutableStateFlow<List<T>>(emptyList()) // Полный список, который можно отобразить в BottomSheet
    private val _preselectedItems = MutableStateFlow<List<T>>(emptyList()) // Список для работы на BottomSheet
    private val _preselectedItemsChildren = MutableStateFlow<List<IEupCheckboxCell>>(emptyList()) // Список для работы на BottomSheet
    private val _applySelectedItems = MutableStateFlow<List<T>>(emptyList()) // Список выбранных элементов после нажатия кнопки принять
    private val _search = MutableStateFlow("") // Строка в поле для поиска
    private val _isVisibleBottomSheet = MutableStateFlow(false) // Флаг для отображения BottomSheet

    val allItemsSize: MutableStateFlow<Int> = _allItems.mapToStateFlow(0, viewModelScope) { it.size }
    val filteredItems: MutableStateFlow<List<T>> = combineStateFlow(
        emptyList(),
        viewModelScope,
        _allItems,
        _search
    ) { allItems, search ->
        if(searchType == SearchTypeEnum.SEARCH_IN_LIST && search.isNotEmpty()) allItems.filter { it.titleGroup.contains(search, true) }
        else allItems
    }  // Список для отображения BottomSheet с учетом поля search
    val preselectedItems: MutableStateFlow<List<T>> = _preselectedItems // Список для работы на BottomSheet
    val preselectedItemsChildren = _preselectedItemsChildren // Список для работы на BottomSheet
    val applySelectedItems: MutableStateFlow<List<T>> = _applySelectedItems // Список выбранных элементов после нажатия кнопки принять
    private val _applySelectedItemsChildren = MutableStateFlow<List<IEupCheckboxCell>>(emptyList()) // Снэпшот отмеченных детей после нажатия "Применить"
    val applySelectedItemsChildren: MutableStateFlow<List<IEupCheckboxCell>> = _applySelectedItemsChildren
    val applySelectedItem: MutableStateFlow<T?> = _applySelectedItems.mapToStateFlow(null, viewModelScope) { it.firstOrNull() } // Выбранный элемент после нажатия кнопки принять
    val search: MutableStateFlow<String> = _search // Строка в поле для поиска
    val isVisibleBottomSheet: MutableStateFlow<Boolean> = _isVisibleBottomSheet // Флаг для отображения BottomSheet
    val selectedValueString: MutableStateFlow<String> = combineStateFlow(
        "",
        viewModelScope,
        _applySelectedItems,
        _applySelectedItemsChildren
    ) { groups, children ->
        // id всех children, которые входят в целиком выбранные группы —
        // их не нужно показывать отдельно, они уже "покрыты" лейблом группы
        val groupChildIds = groups.flatMap { group -> group.children.map { it.idCell } }.toSet()

        val groupLabels = groups.map { group ->
            "${group.titleGroup} +${group.children.size}"
        }

        val standaloneChildLabels = children
            .filter { it.idCell !in groupChildIds }
            .map { it.titleCell }

        (groupLabels + standaloneChildLabels).joinToString(", ")
    } // Строка из выбранных и принятых элементов, отображаемая в DropDownText

    val value: FormField<T?> = FormField(null, viewModelScope, validationBlock(validations::value))

    init {
        _allItems.collectAsStateFlow(viewModelScope) {
            if (searchType != SearchTypeEnum.SEARCH_FOR_QUERY) {
//                _isVisibleBottomSheet.value = false
                _applySelectedItems.value = it.filter { it.isSelectedGroup }
            }
        }

        _isVisibleBottomSheet.collectAsStateFlow(viewModelScope) {
            if (it) {
                if (searchType != SearchTypeEnum.SEARCH_FOR_QUERY || _applySelectedItems.value.isEmpty()) _search.value = ""
                _preselectedItems.value = _applySelectedItems.value
            }
        }

        applySelectedItem.collectAsStateFlow(viewModelScope) {
            value.data.value = it
        }
    }

    fun doCheckedGroup(item: T) {
        // Was selected full group
        val isContains = _preselectedItems.value.contains(item)

        when (isMultiSelect) {
            true -> {
                _preselectedItems.value = if (isContains) _preselectedItems.value.minus(item)
                else _preselectedItems.value.plus(item)
            }

            false -> if (isContains.not()) _preselectedItems.value = listOf(item)
        }

        _preselectedItemsChildren.value = if (isContains) _preselectedItemsChildren.value.minus(item.children)
            else _preselectedItemsChildren.value.plus(item.children)

        val updated = _allItems.value.toMutableList()
        val index = updated.indexOfFirst { it == item }.takeIf { it >= 0 } ?: return
        updated[index].isSelectedGroup = _preselectedItems.value.contains(item)
        updated[index].children.forEach {
            it.isSelectedCell = true
        }

        viewModelScope.launch {
            _allItems.value = emptyList()
            delay(1)
            _allItems.value = updated
        }
    }

    fun doCheckedItem(
        group: T,
        children: IEupCheckboxCell
    ) {
        val isContains = _preselectedItemsChildren.value.find { it.idCell == children.idCell } != null

        val currentItemInGroup = group.children.find {
            it.idCell == children.idCell
        }

        currentItemInGroup?.let { item ->
            when (isMultiSelect) {
                true -> {
                    _preselectedItemsChildren.value = if (isContains) _preselectedItemsChildren.value.minus(item)
                    else _preselectedItemsChildren.value.plus(item)
                }

                false -> if (isContains.not()) _preselectedItemsChildren.value = listOf(item)
            }

            val updated = _allItems.value.toMutableList()
            val index = updated.indexOfFirst { it == group }.takeIf { it >= 0 } ?: return
            updated[index].children.forEach {
                it.isSelectedCell = _preselectedItemsChildren.value.contains(item)
            }

            viewModelScope.launch {
                _allItems.value = emptyList()
                delay(1)
                _allItems.value = updated
            }
        }
    }

    fun setItems(items: List<T>) {
        _allItems.value = items
    }

    fun updateExpandedItem(idGroup: Int, expanded: Boolean) {
        val updated = _allItems.value.toMutableList()
        val index = updated.indexOfFirst { it.idGroup == idGroup }.takeIf { it >= 0 } ?: return
        updated[index].isExpandedGroup = expanded

        viewModelScope.launch {
            _allItems.value = emptyList()
            delay(1) // yield to let StateFlow emit the empty list first
            _allItems.value = updated
        }
    }

    fun setVisibleBottomSheet(isVisible: Boolean) {
        _isVisibleBottomSheet.value = isVisible
    }

    fun setSearch(text: String) {
        _search.value = text
    }

    fun doSelectApply(
        items: List<T>,
        isEdit: Boolean = false
    ) {
        val result: MutableList<T> = items.toMutableList()

        doClearSelected()
        result.onEach {
            it.isSelectedGroup = _preselectedItems.value.contains(it)
            it.children.onEach { ch ->
                ch.isSelectedCell = _preselectedItemsChildren.value.contains(ch)
            }
        }

        _applySelectedItems.value = result

        if (isEdit) {
            _preselectedItems.value = result
            _preselectedItemsChildren.value = result.flatMap { it.children }
            _applySelectedItemsChildren.value = _preselectedItemsChildren.value
        } else {
            _applySelectedItemsChildren.value = _preselectedItemsChildren.value
        }

        _isVisibleBottomSheet.value = false
    }

    fun doSelectApply(item: T) {
        doSelectApply(listOf(item))
    }

    fun doSelectApplySpecifiedItem(item: T) {
        val items = listOf(item)
        doClearSelected()
        items.onEach {
            it.isSelectedGroup = true
            it.children.onEach { ch ->
                ch.isSelectedCell = true
            }
        }
        _applySelectedItems.value = items
    }

    fun doClearSelected() {
        _allItems.value.forEach {
            it.isSelectedGroup = false
            it.children.onEach { ch ->
                ch.isSelectedCell = false
            }
        }
        _applySelectedItems.value = emptyList()
    }
}
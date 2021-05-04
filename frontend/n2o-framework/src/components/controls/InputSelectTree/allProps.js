import PropTypes from 'prop-types'

export const defaultProps = {
    children: null,
    hasChildrenFieldId: 'hasChildren',
    disabled: false,
    loading: false,
    parentFieldId: 'parentId',
    valueFieldId: 'id',
    labelFieldId: 'name',
    iconFieldId: 'icon',
    imageFieldId: 'image',
    badgeFieldId: 'badge',
    badgeColorFieldId: 'color',
    filter: '',
    sortFieldId: 'name',
    hasCheckboxes: false,
    multiSelect: false,
    closePopupOnSelect: false,
    data: [],
    searchPlaceholder: '',
    transitionName: 'slide-up',
    choiceTransitionName: 'zoom',
    showCheckedStrategy: 'all',
    allowClear: true,
    placeholder: '',
    showSearch: true,
    dropdownPopupAlign: {
        points: ['tl', 'bl'],
        overflow: {
            adjustY: true,
        },
    },
    onSearch: () => {},
    onSelect: () => {},
    onChange: () => {},
    onClose: () => {},
    onToggle: () => {},
    onOpen: () => {},
    onFocus: () => {},
    onBlur: () => {},
}

export const propTypes = {
    children: PropTypes.node,
    /**
   * Значение ключа hasChildren в данных
   */
    hasChildrenFieldId: PropTypes.string,
    /**
   * Значение ключа parent в данных
   */
    parentFieldId: PropTypes.string,
    /**
   * Флаг анимации загрузки
   */
    loading: PropTypes.bool,
    /**
   * Данные для построения дерева
   */
    data: PropTypes.array,
    /**
   * Значение ключа value в данных
   */
    valueFieldId: PropTypes.string,
    /**
   * Значение ключа label в данных
   */
    labelFieldId: PropTypes.string,
    /**
   * Значение ключа icon в данных
   */
    iconFieldId: PropTypes.string,
    /**
   *  Значение ключа image в данных
   */
    imageFieldId: PropTypes.string,
    /**
   * Значение ключа badge в данных
   */
    badgeFieldId: PropTypes.string,
    /**
   * Значение ключа badgeColor в данных
   */
    badgeColorFieldId: PropTypes.string,
    /**
   * Значение ключа сортировки в данных
   */
    sortFieldId: PropTypes.string,
    groupFieldId: PropTypes.string,
    /**
   * Флаг неактивности
   */
    disabled: PropTypes.bool,
    /**
   * Неактивные данные
   */
    disabledValues: PropTypes.array,
    /**
   * Варианты фильтрации
   */
    filter: PropTypes.oneOf(['includes', 'startsWith', 'endsWith', 'server']),
    /**
   * Значение
   */
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    onInput: PropTypes.func,
    /**
   * Calback изменения
   */
    onChange: PropTypes.func,
    /**
   * Placeholder контрола
   */
    placeholder: PropTypes.string,
    /**
   * Callback на открытие
   */
    onOpen: PropTypes.func,
    /**
   * Callback на закрытие
   */
    onClose: PropTypes.func,
    /**
   * Мульти выбор значений
   */
    multiSelect: PropTypes.bool,
    /**
   * Флаг закрытия попапа при выборе элемента
   */
    closePopupOnSelect: PropTypes.bool,
    /**
   * Флаг для показа чекбоксов в элементах дерева. Переводит InputSelectTree в мульти режим
   */
    hasCheckboxes: PropTypes.bool,
    /**
   * Формат
   */
    format: PropTypes.string,
    /**
   * Флаг сжатия выбранных элементов
   */
    collapseSelected: PropTypes.bool,
    /**
   * От скольки элементов сжимать выбранные элементы
   */
    lengthToGroup: PropTypes.number,
    /**
   * Callback на поиск
   */
    onSearch: PropTypes.func,
    expandPopUp: PropTypes.bool,
    /**
   * Флаг динамичексой подгрузки данных. В данных обязательно указывать параметр hasChildrens
   */
    ajax: PropTypes.bool,
    /**
   * Сallback функция вызываемая ajax true. Передает value открывающего элемента дерева
   */
    handleItemOpen: PropTypes.func,
    /**
   * Выравнивание попапа
   */
    dropdownPopupAlign: PropTypes.object,
    showCheckedStrategy: PropTypes.string,
}

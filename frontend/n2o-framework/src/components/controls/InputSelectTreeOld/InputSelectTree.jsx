import React from 'react'
import ReactDOM from 'react-dom'
import PropTypes from 'prop-types'
import onClickOutside from 'react-onclickoutside'
import Dropdown from 'reactstrap/lib/Dropdown'
import DropdownToggle from 'reactstrap/lib/DropdownToggle'
import isEqual from 'lodash/isEqual'
import get from 'lodash/get'
import unionBy from 'lodash/unionBy'
import find from 'lodash/find'
import merge from 'lodash/merge'
import isNil from 'lodash/isNil'
import { SHOW_CHILD } from 'rc-tree-select'

import Popup from '../InputSelect/Popup'
import InputContent from '../InputSelect/InputContent'
import InputSelectGroup from '../InputSelect/InputSelectGroup'

import NODE_SELECTED from './nodeSelected.js'
import TreeItems from './TreeItems'

/**
 * InputSelectTree
 * @reactProps {boolean} hasChildrenFieldId
 * @reactProps {boolean} loading - флаг анимации загрузки
 * @reactProps {array} options - данные
 * @reactProps {string} valueFieldId - значение ключа value в данных
 * @reactProps {string} labelFieldId - значение ключа label в данных
 * @reactProps {string} iconFieldId - поле для иконки
 * @reactProps {string} imageFieldId - поле для картинки
 * @reactProps {string} badgeFieldId - поле для баджа
 * @reactProps {string} badgeColorFieldId - поле для цвета баджа
 * @reactProps {boolean} disabled - флаг неактивности
 * @reactProps {array} disabledValues - неактивные данные
 * @reactProps {string} filter - варианты фильтрации
 * @reactProps {string} value - текущее значение
 * @reactProps {function} onInput - callback при вводе в инпут
 * @reactProps {function} onChange - callback при выборе значения или вводе
 * @reactProps {function} onScrollENd - callback при прокрутке скролла popup
 * @reactProps {string} placeHolder - подсказка в инпуте
 * @reactProps {boolean} resetOnBlur - фича, при которой сбрасывается значение контрола, если оно не выбрано из popup
 * @reactProps {function} onOpen - callback на открытие попапа
 * @reactProps {function} onClose - callback на закрытие попапа
 * @reactProps {function} onSelect
 * @reactProps {boolean} multiSelect - флаг мульти выбора
 * @reactProps {string} groupFieldId - поле для группировки
 * @reactProps {boolean} closePopupOnSelect - флаг закрытия попапа при выборе
 * @reactProps {boolean} hasCheckboxes - флаг наличия чекбоксов
 * @reactProps {string} format - формат
 * @reactProps {boolean} collapseSelected - флаг сжатия выбранных элементов
 * @reactProps {number} lengthToGroup - от скольки элементов сжимать выбранные элементы
 * @reactProps {boolean} ajax - загрузка элементов при раскрытии
 */

class InputSelectTree extends React.Component {
    constructor(props) {
        super(props)
        const {
            value,
            options,
            valueFieldId,
            labelFieldId,
            multiSelect,
        } = this.props
        const valueArray = Array.isArray(value) ? value : value ? [value] : []
        const input = value && !multiSelect ? value[labelFieldId] : ''
        this.state = {
            isExpanded: false,
            value: value || [],
            selected: [],
            options,
            active: null,
            treeStates: this.getTreeItems(this.props.options),
            input,
        }

        this._handleButtonClick = this._handleButtonClick.bind(this)
        this._handleInputChange = this._handleInputChange.bind(this)
        this._handleInputFocus = this._handleInputFocus.bind(this)
        this._hideOptionsList = this._hideOptionsList.bind(this)
        this._handleItemSelect = this._handleItemSelect.bind(this)
        this._handleScrollEnd = this._handleScrollEnd.bind(this)
        this._removeSelectedItem = this._removeSelectedItem.bind(this)
        this._clearSelected = this._clearSelected.bind(this)
        this._handleElementCreate = this._handleElementCreate.bind(this)
        this._handleElementClear = this._handleElementClear.bind(this)
        this.onExpandClick = this.onExpandClick.bind(this)
        this._handleRemove = this._handleRemove.bind(this)
        this.handleKeyPress = this.handleKeyPress.bind(this)
        this.handleFocus = this.handleFocus.bind(this)
        this._handleResetOnBlur = this._handleResetOnBlur.bind(this)
        this._toggle = this._toggle.bind(this)
        this._handleArrowDown = this._handleArrowDown.bind(this)
        this._handleArrowUp = this._handleArrowUp.bind(this)
        this._handleArrowRight = this._handleArrowRight.bind(this)
        this._handleArrowLeft = this._handleArrowLeft.bind(this)
        this._handleEnter = this._handleEnter.bind(this)
        this._setIsExpanded = this._setIsExpanded.bind(this)
        this._handleClick = this._handleClick.bind(this)
        this._clearSelected = this._clearSelected.bind(this)
        this._setNewInputValue = this._setNewInputValue.bind(this)
        this._setInputFocus = this._setInputFocus.bind(this)

        this.KEY_MAPPER = {
            ArrowDown: this._handleArrowDown,
            ArrowUp: this._handleArrowUp,
            ArrowRight: this._handleArrowRight,
            ArrowLeft: this._handleArrowLeft,
            Enter: this._handleEnter,
        }
    }

    componentWillReceiveProps(nextProps) {
        const {
            multiSelect,
            value,
            valueFieldId,
            labelFieldId,
            options,
            loading,
        } = nextProps
        if (!isEqual(nextProps.options, this.state.options)) {
            this.setState({ options, treeStates: this.getTreeItems(options) })
        }
        if (!isEqual(nextProps.value, this.props.value)) {
            const valueArray = Array.isArray(value) ? value : value ? [value] : []
            const input = value && !multiSelect ? value[labelFieldId] : ''

            this.setState({ value: valueArray, input })
        }
    }

    _handleChange() {
        const { selected } = this.state
        const { onChange, multiSelect } = this.props

        if (!onChange) {
            return false
        }

        if (selected.length) {
            multiSelect ? onChange(selected) : onChange(selected[0])
        } else {
            onChange(null)
        }
    }

    /**
   * Обработчик события по удалению элемента из выбранных
   * @param item - элемент для удаления
   * @private
   */

    _handleRemove(item) {
        let items = []

        if (this.props.multiSelect) {
            items = [...this._retrieveChilds(item), item]
            this.changeSelected(items, NODE_SELECTED.NOT_SELECTED)
            this.unselectParent(item)
        } else {
            items.push(item)
        }

        items.map(node => this._removeSelectedItem(node))
    }

    /**
   * Удаляет элемент из списка выбранных
   * @param item - элемент
   * @private
   */

    // _removeSelectedItem(item) {
    //   this.setState(
    //     prevState => ({ selected: prevState.selected.filter(i => i.id !== item.id) }),
    //     () => this._unselectNode(item)
    //   );
    // }

    _removeSelectedItem(item) {
        const { onChange } = this.props
        const value = this.state.value.filter(i => i.id !== item.id)
        this.setState({ value }, onChange(this._getValue()))
    }

    /**
   * Обрабатывает конец скролла popUp
   * @private
   */

    _handleScrollEnd() {
        if (this.props.onScrollEnd) {
            this.props.onScrollEnd()
        }
    }

    /**
   * новое значение инпута search)
   * @param input
   * @private
   */
    _setNewInputValue(input) {
        const { onInput, resetOnBlur, multiSelect } = this.props
        const { value } = this.state
        const onSetNewInputValue = (input) => {
            onInput(input)
            this._handleDataSearch(input)
        }

        if (this.state.input !== input) {
            this._setSelected(false)
            this.setState({ input }, () => onSetNewInputValue(input))
        }
    }

    /**
   * Изменение видимости попапа
   * @param newState - новое значение видимости
   * @private
   */

    _changePopUpVision(newState) {
        if (this.state.isExpanded === newState) {
            return
        }

        if (this.state.isExpanded && this.props.onClose) {
            this.props.onClose()
        }

        this.setState({
            isExpanded: newState,
        })
    }

    /**
   * Обрабатывает нажатие на кнопку
   * @private
   */

    _handleButtonClick() {
        this._changePopUpVision(!this.state.isExpanded)
    }

    /**
   * Обрабатывает форкус на инпуте
   * @private
   */

    _handleInputFocus() {
        this._changePopUpVision(true)
    }

    /**
   * Скрывает popUp
   * @private
   */

    _hideOptionsList() {
        this._changePopUpVision(false)
    }

    /**
   * Уставнавливает новое значение инпута
   * @param newValue - новое значение
   * @private
   */

    _setNewValue(newValue) {
        this.setState({
            value: newValue,
        })
    }

    /**
   * Выполняет поиск по дереву
   * @param options - элементы по которым искать
   * @param searchValue - поисковый запрос
   * @returns {*}
   * @private
   */

    _makeSearch(options, searchValue) {
        const filter = item => String.prototype[this.props.filter].call(item, searchValue)

        const { valueFieldId, parentFieldId } = this.props

        return options.filter((item) => {
            const childs = this.state.options.filter(
                node => node[parentFieldId] === item[valueFieldId],
            )

            return (
                filter(item[this.props.labelFieldId].toString()) ||
        this._makeSearch(childs, searchValue).length > 0
            )
        })
    }

    /**
   * Выполняет поиск элементов для popUp, если установлен фильтр
   * @param newValue - значение для поиска
   * @private
   */

    _handleDataSearch(input, delay = true, callback) {
        const { onSearch, filter, labelFieldId, options } = this.props
        if (filter && ['includes', 'startsWith', 'endsWith'].includes(filter)) {
            const filterFunc = item => String.prototype[filter].call(item, input)
            const filteredData = options.filter(item => filterFunc(item[labelFieldId]))
            this.setState({ options: filteredData })
        } else {
            // серверная фильтрация
            const labels = this.state.value.map(item => item[labelFieldId])
            if (labels.some(label => label === input)) {
                onSearch('', delay, callback)
            } else {
                onSearch(input, delay, callback)
            }
        }
    }

    /**
   * Устанавливает выбранный элемент
   * @param items - элемент массива options
   * @private
   */

    _insertSelected(items) {
        const { valueFieldId } = this.props

        if (this.props.multiSelect) {
            this.setState(prevState => ({
                selected: unionBy(prevState.selected, items, valueFieldId),
            }))
        } else {
            this.setState({
                selected: items,
            })
        }
    }

    /**
   * Обрабатывает выбор элемента
   * @param item
   * @private
   */

    _handleSelect(item) {
        const { multiSelect } = this.props
        let items = []

        if (multiSelect) {
            items = [item, ...this._retrieveChilds(item)]
            this.selectParent(item)
        } else {
            items.push(item)
            this._unselectAll()
        }

        this._insertSelected(items)
        this.changeSelected(items, NODE_SELECTED.SELECTED)
    }

    /**
   * Изменяет стейт выбора всех элементов
   * @private
   */

    _unselectAll() {
        const options = this.state.treeStates

        Object.entries(options).forEach(([_, item]) => {
            if (item.selected === NODE_SELECTED.SELECTED) {
                item.selected = NODE_SELECTED.NOT_SELECTED
            }
        })

        this.setState({ treeStates: options })
    }

    /**
   * Изменяет стейт выбора для одного элемент
   * @param node - элемент дерева
   * @private
   */

    _unselectNode(node) {
        this.setState((prevState) => {
            const { treeStates } = prevState
            const { valueFieldId } = this.props
            treeStates[node[valueFieldId]].selected = NODE_SELECTED.NOT_SELECTED

            return { treeStates }
        })
    }

    /**
   * Ищет все элементы поддерева
   * @param item - корень поддерева
   * @returns {*[]}
   * @private
   */

    _retrieveChilds(item) {
        const { valueFieldId, parentFieldId } = this.props
        const childs = this.state.options.filter(
            node => node[parentFieldId] === item[valueFieldId],
        )

        childs.length > 0 &&
      childs.map((child) => {
          childs.concat(this._retrieveChilds(child))
      })

        return childs
    }

    /**
   * Удаляет все элементы поддерева из выбранных
   * @param item - корень поддерева
   * @private
   */

    _removeChild(item) {
        const { valueFieldId, parentFieldId } = this.props
        if (this.props.multiSelect) {
            const childs = this.state.options.filter(
                node => node[parentFieldId] === item[valueFieldId],
            )

            childs.length > 0 &&
        childs.map((child) => {
            this._removeSelectedItem(child)
            this._removeChild(child)
        })
        }
    }

    /**
   * Очищает выбранный элемент
   * @private
   */

    _clearSelected() {
        const { onChange } = this.props
        this.setState({ value: [], input: '' }, () => onChange(this._getValue()))
    }

    /**
   * Обрабатывает изменение инпута
   * @param newValue - новое значение
   * @private
   */

    _handleInputChange(newValue) {
        this._setNewValue(newValue)
        this._handleDataSearch(newValue)

        // if (!this.props.multiSelect) {
        //   this._clearSelected();
        // }

        if (!this.props.resetOnBlur && this.props.onChange) {
            this.props.onChange(newValue)
        }

        if (this.props.onInput) {
            this.props.onInput(newValue)
        }
    }

    /**
   * Возвращает текущее значение (массив - если ипут селект, объект - если нет)
   * или null если пусто
   * @returns {*}
   * @private
   */
    _getValue() {
        const { multiSelect } = this.props
        const { value } = this.state
        const rObj = multiSelect ? value : value[0]
        return rObj || null
    }

    /**
   * Обрабатывает выбор элемента из popUp
   * @param item - элемент массива options
   * @private
   */

    _handleItemSelect(item) {
        const {
            multiSelect,
            closePopupOnSelect,
            labelFieldId,
            options,
            onSelect,
            onChange,
        } = this.props
        const selectCallback = () => {
            closePopupOnSelect && this._hideOptionsList()
            onSelect(item)
            onChange(this._getValue())
            this._setSelected(true)
        }

        this.setState(
            prevState => ({
                value: multiSelect ? [...prevState.value, item] : [item],
                input: multiSelect ? '' : item[labelFieldId],
                options,
            }),
            selectCallback,
        )
    }

    /**
   * Очищает инпут и результаты поиска
   * @private
   */

    _clearSearchField() {
        this.setState({
            value: '',
            options: this.props.options,
        })
    }

    /**
   * Обрабатывает поведение инпута при потери фокуса, если есть resetOnBlur
   * @private
   */

    _handleResetOnBlur() {
        const { value, input, options } = this.state
        const { onChange, multiSelect, resetOnBlur, labelFieldId } = this.props
        const newValue = find(options, { [labelFieldId]: input })
        if (!newValue) {
            if (!resetOnBlur) {
                if (input) {
                    const createdValue = { [labelFieldId]: input }
                    this.setState(
                        {
                            value: multiSelect ? [...value, createdValue] : [createdValue],
                            input: multiSelect ? '' : input,
                        },
                        () => onChange(this._getValue()),
                    )
                    onChange(multiSelect ? [...value, createdValue] : createdValue)
                }
            } else if (input) {
                this.setState(
                    {
                        input: multiSelect
                            ? ''
                            : (value[0] && value[0][labelFieldId]) || '',
                        value,
                    },
                    () => onChange(this._getValue()),
                )
            } else {
                this.setState(
                    {
                        input: '',
                        value: multiSelect ? value : [],
                    },
                    () => onChange(this._getValue()),
                )
            }
        } else {
            this.setState(
                {
                    value: multiSelect ? [...value, newValue] : [newValue],
                    input: multiSelect ? '' : input,
                },
                () => onChange(this._getValue()),
            )
        }
    }

    _handleElementClear() {
        this._clearSearchField()
        this._clearSelected()
    }

    /**
   * Обрабатывает клик за пределы компонента
   * @param evt
   */

    handleClickOutside(evt) {
        this._hideOptionsList()
        this._handleResetOnBlur()
    }

    /**
   * Обрабатывает создание элемента
   * @private
   */

    _handleElementCreate() {
        const { valueFieldId } = this.props
        const newElement = {
            [this.props.labelFieldId]: this.state.value,
        }

        if (this.props.onElementCreate) {
            this.props.onElementCreate(newElement)
        }

        newElement[valueFieldId] = `${newElement.value}_${new Date().getTime()}`

        this.setState({
            selected: [...this.state.selected, newElement],
        })

        this._setNewValue('')
    }

    inArray(array, item) {
        const { valueFieldId } = this.props
        return (
            array.filter(
                disabledItem => disabledItem[valueFieldId] === item[valueFieldId],
            ).length > 0
        )
    }

    /**
   * Обработчик нажатия на кнопку раскрытия элемента
   * @param item
   */

    onExpandClick(item) {
        const { valueFieldId } = this.props
        const itemState = this.state.treeStates[item[valueFieldId]]
        this._handleExpandClick(itemState)
    }

    /**
   * Получает state для новых элементов дерева
   * @param options
   */

    getTreeItems(options, mergeData = null) {
        const result = {}
        const { valueFieldId, parentFieldId, hasChildrenFieldId } = this.props
        const treeStates = get(this, 'state.treeStates', false)

        options.map(
            item => (result[item[valueFieldId]] = {
                [valueFieldId]: item[valueFieldId],
                parentId: item[parentFieldId],
                expanded: false,
                loaded: false,
                selected: NODE_SELECTED.NOT_SELECTED,
                ref: React.createRef(),
                [hasChildrenFieldId]: isNil(item[hasChildrenFieldId])
                    ? true
                    : item[hasChildrenFieldId],
            }),
        )

        return treeStates ? merge(result, treeStates) : result
    }

    /**
   * Добавляет новым элементам дерева их state
   * @param elements - новые элементы
   */

    addNewElements(elements) {
        const { valueFieldId } = this.props
        const newElements = elements.filter(
            item => !this.state.treeStates.hasOwnProperty(item[valueFieldId]),
        )

        this.setState(prevState => ({
            treeStates: {
                ...prevState.treeStates,
                ...this.getTreeItems(newElements),
            },
        }))
    }

    /**
   * Изменяет стейт элемента дерева
   * @param itemState - стейс элемента
   * @param id - id элемента
   */

    overrideElement(itemState, id) {
        const newData = { ...this.state.treeStates,
            [id]: itemState }

        this.setState({ treeStates: newData })
    }

    /**
   * Изменения статус выбора у заданных элементов
   * @param nodes - элементы дерева
   * @param newStatus - новый статус
   */

    changeSelected(nodes, newStatus) {
        const { valueFieldId } = this.props
        nodes.map((node) => {
            const nodeState = this.state.treeStates[node[valueFieldId]]

            nodeState.selected = newStatus
            this.overrideElement(nodeState)
        })
    }

    /**
   * Устанавливает статус выбора у родительских элементов дерева
   * @param item - элемент дерева
   */

    selectParent(item) {
        const { valueFieldId, parentFieldId } = this.props
        const parent = this.state.options[
            this.state.options.findIndex(
                parent => parent[valueFieldId] === item[parentFieldId],
            )
        ]

        if (!parent) {
            return
        }

        const parentState = this.state.treeStates[parent[valueFieldId]]

        if (parentState.selected === NODE_SELECTED.NOT_SELECTED) {
            parentState.selected = NODE_SELECTED.PARTIALLY
            this.overrideElement(parentState, parent[valueFieldId])

            if (parent[parentFieldId]) {
                this.selectParent(parent)
            }
        }
    }

    /**
   * Снимает статус выбора с родительских элементов дерева
   * @param item - элемент дерева
   */

    unselectParent(item) {
        const { valueFieldId, parentFieldId } = this.props
        const parent = this.state.options[
            this.state.options.findIndex(
                parent => parent[valueFieldId] === item[parentFieldId],
            )
        ]
        const parentState = parent
            ? this.state.treeStates[parent[valueFieldId]]
            : null

        if (parentState && parentState.selected === NODE_SELECTED.PARTIALLY) {
            const selectedChild = this.state.options.find(
                child => child[parentFieldId] === parent[valueFieldId] &&
          this.state.treeStates[child[valueFieldId]].selected ===
            NODE_SELECTED.SELECTED,
            )

            if (!selectedChild) {
                parentState.selected = NODE_SELECTED.NOT_SELECTED
                this.overrideElement(parentState, parent[valueFieldId])
            }

            if (parent[parentFieldId]) {
                this.unselectParent(parent)
            }
        }
    }

    /**
   * Обрабатывает нажатия на кнопку клавиатуры "Стрелка вниз"
   * @private
   */

    _handleArrowDown() {
        if (this.state.active) {
            this._setNextNodeActive()
        } else {
            this._setFirstNodeActive()
        }
    }

    /**
   * Обрабатывает нажатия на кнопку клавиатуры "Стрелка вверх"
   * @private
   */

    _handleArrowUp() {
        if (this.state.active) {
            this._setPrevNodeActive()
        }
    }

    /**
   * Обрабатывает нажатия на кнопку клавиатуры "Стрелка вправо"
   * @private
   */

    _handleArrowRight() {
        if (this.state.active) {
            if (!this.state.active.expanded && this.state.active.hasChildren) {
                this._handleExpandClick(this.state.active, true)
            } else {
                this._setNextNodeActive()
            }
        } else {
            this._setFirstNodeActive()
        }
    }

    /**
   * Обрабатывает нажатия на кнопку клавиатуры "Стрелка влево"
   * @private
   */

    _handleArrowLeft() {
        const { active } = this.state
        const { parentFieldId } = this.props

        if (active) {
            if (active.expanded) {
                this._collapseNode()
            } else if (active[parentFieldId]) {
                this.setState({ active: this.state.treeStates[active[parentFieldId]] })
                this._scrollOntoElement(this.state.treeStates[active[parentFieldId]])
            } else {
                this._setPrevNodeActive()
            }
        }
    }

    /**
   * Схлопывает элемент дерева
   * @private
   */

    _collapseNode() {
        const { valueFieldId } = this.props
        const activeItem = {
            ...this.state.active,
            expanded: false,
        }

        this.overrideElement(activeItem, activeItem[valueFieldId])
        this.setState({ active: activeItem })
    }

    /**
   * Устанавливает первый элемент дерева активным
   * @private
   */

    _setFirstNodeActive() {
        const { valueFieldId, parentFieldId } = this.props
        const rootNodes = this.state.options.filter(node => !node[parentFieldId])
        this.setState({
            active: this.state.treeStates[rootNodes[0][valueFieldId]],
        })
    }

    /**
   * Устанавливает следующий элемент дерева активный
   * @private
   */

    _setNextNodeActive() {
        const { valueFieldId } = this.props
        const nextItem = this.getNextItem(this.state.active)

        if (nextItem) {
            const nextItemState = this.state.treeStates[nextItem[valueFieldId]]
            this.setState({ active: nextItemState })
            this._scrollOntoElement(nextItemState)
        }
    }

    /**
   * Устанавливает предыдущий элемент дерева активный
   * @private
   */

    _setPrevNodeActive() {
        const { valueFieldId } = this.props
        const prevItem = this.getPrevItem(this.state.active)

        if (prevItem) {
            const prevItemState = this.state.treeStates[prevItem[valueFieldId]]
            this.setState({ active: prevItemState })
            this._scrollOntoElement(prevItemState)
        }
    }

    _scrollOntoElement(nodeState) {
        const nodeElement = ReactDOM.findDOMNode(nodeState.ref.current)
        nodeElement.scrollIntoView()
    }

    /**
   * Обрабатывает нажатие кнопки Enter
   * @private
   */

    _handleEnter() {
        if (this.state.active) {
            this._handleItemSelect(this.state.active)
        }
    }

    /**
   * Обрабатывает нажатия на кнопку клавиатуры
   * @param e - событие нажатия
   */

    handleKeyPress(e) {
        if (this.KEY_MAPPER.hasOwnProperty(e.key)) {
            this.KEY_MAPPER[e.key]()
        }
    }

    /**
   * Обрабатывает изменение активного элемента
   * @param item
   */

    handleFocus(item) {
        this.setState({ active: item })
    }

    /**
   * Находит предыдущий элемент
   * @param currentItem - текущий элемент
   * @returns {*}
   */

    getPrevItem(currentItem) {
        const { parentFieldId } = this.props
        if (currentItem[parentFieldId]) {
            return this._findPrevParentItem(currentItem)
        }
        return this._findPrevRootItem(currentItem)
    }

    /**
   * Находит предыдущий элемент без родителя
   * @param currentItem - текущий активный элемент
   * @returns {*}
   * @private
   */

    _findPrevRootItem(currentItem) {
        const { valueFieldId, parentFieldId } = this.props
        const rootNodes = this.state.options.filter(node => !node[parentFieldId])
        const prevNode = this._findPrevItem(rootNodes, currentItem)

        if (!prevNode) {
            this._changePopUpVision(false)
            return null
        } if (!this.state.treeStates[prevNode[valueFieldId]].expanded) {
            return prevNode
        }
        return this._getLowest(prevNode)
    }

    /**
   * Находит предыдущий элемент-родитель
   * @param currentItem - текущий элемент
   * @returns {*}
   * @private
   */

    _findPrevParentItem(currentItem) {
        const { valueFieldId, parentFieldId } = this.props
        const sameLevels = this.state.options.filter(
            node => node[parentFieldId] === currentItem[parentFieldId],
        )
        const nextItem = this._findPrevItem(sameLevels, this.state.active)

        if (nextItem) {
            return nextItem
        }
        return this.state.options[
            this.state.options.findIndex(
                node => node[valueFieldId] === currentItem[parentFieldId],
            )
        ]
    }

    /**
   * Находит самый последний элемент в поддереве
   * @param item - корень поддерева
   * @returns {*}
   * @private
   */

    _getLowest(item) {
        const { valueFieldId, parentFieldId } = this.props
        const childs = this.state.options.filter(
            node => node[parentFieldId] === item[valueFieldId],
        )
        const lastChild = childs[childs.length - 1]

        if (this.state.treeStates[lastChild[valueFieldId]].expanded) {
            return this._getLowest(lastChild)
        }
        return lastChild
    }

    /**
   * Находит следующий элемент
   * @param currentItem
   * @param lookChilds
   * @returns {*}
   */

    getNextItem(currentItem, lookChilds = true) {
        const { valueFieldId, parentFieldId } = this.props
        const childs = this.state.options.filter(
            node => node[parentFieldId] === currentItem[valueFieldId],
        )
        const itemState = this.state.treeStates[currentItem[valueFieldId]]

        if (lookChilds && itemState.expanded && childs.length > 0) {
            return childs[0]
        } if (currentItem[parentFieldId]) {
            return this._findNextParentItem(currentItem)
        }
        const rootNodes = this.state.options.filter(node => !node[parentFieldId])

        return this._findNextItem(rootNodes, currentItem)
    }

    /**
   * Находит следующий элемент-родителя
   * @param currentItem
   * @returns {*}
   * @private
   */

    _findNextParentItem(currentItem) {
        const { valueFieldId, parentFieldId } = this.props
        const sameLevels = this.state.options.filter(
            node => node[parentFieldId] === currentItem[parentFieldId],
        )
        const nextItem = this._findNextItem(sameLevels, this.state.active)

        if (nextItem) {
            return nextItem
        }
        const parent = this.state.options[
            this.state.options.findIndex(
                node => node[valueFieldId] === currentItem[parentFieldId],
            )
        ]

        return this.getNextItem(parent, false)
    }

    /**
   * скрыть / показать попап
   * @param isExpanded
   * @private
   */
    _setIsExpanded(isExpanded) {
        const { onToggle, onClose, onOpen } = this.props
        const { isExpanded: previousIsExpanded } = this.state
        if (isExpanded !== previousIsExpanded) {
            this.setState({ isExpanded })
            onToggle()
            isExpanded ? onOpen() : onClose()
        }
    }

    /**
   * Получает следующий по-порядку элемент из списка элементов
   * @param nodes - список элементов
   * @param active - активный элемент
   * @returns {null}
   * @private
   */

    _findNextItem(nodes, active) {
        const { valueFieldId } = this.props
        const currentIndex = nodes.findIndex(
            node => node[valueFieldId] === active[valueFieldId],
        )
        const nextIndex = currentIndex + 1

        return nextIndex <= nodes.length ? nodes[nextIndex] : null
    }

    /**
   * Получает предыдущий по-порядку элемент из списка элементов
   * @param nodes - список элементов
   * @param active - активный элемент
   * @returns {null}
   * @private
   */

    _findPrevItem(nodes, active) {
        const { valueFieldId } = this.props
        const currentIndex = nodes.findIndex(
            node => node[valueFieldId] === active[valueFieldId],
        )
        const nextIndex = currentIndex - 1

        return nextIndex >= 0 ? nodes[nextIndex] : null
    }

    /**
   * Обработчик раскрытия элемента дерева
   * @param itemState - стейт элемента для раскрытия
   * @param newState - новый статус
   * @private
   */

    async _handleExpandClick(itemState, newState = null) {
        const { valueFieldId } = this.props
        if (this.props.ajax && !itemState.loaded) {
            itemState.expanded = true
            itemState.loaded = true
            this.props.handleItemOpen(itemState[valueFieldId])
        } else {
            itemState.expanded = newState || !itemState.expanded
        }

        this.overrideElement(itemState, itemState[valueFieldId])
    }

    _handleClick() {
    // const searchCallback = () => {
        this._setIsExpanded(true)
        // };
        // this._handleDataSearch(this.state.input, false, searchCallback);
        this._setSelected(false)
        this._setInputFocus(true)
    }

    _toggle() {
        const { closePopupOnSelect } = this.props
        this.setState((prevState) => {
            if (!closePopupOnSelect && prevState.isExpanded) { return false }

            return { isExpanded: !prevState.isExpanded }
        })
    }

    _setSelected(isInputSelected) {
        this.setState({ isInputSelected })
    }

    _setInputFocus(inputFocus) {
        this.setState({ inputFocus })
    }

    /**
   * Рендер
   */

    render() {
        const {
            loading,
            labelFieldId,
            iconFieldId,
            disabled,
            placeholder,
            multiSelect,
            disabledValues,
            imageFieldId,
            hasCheckboxes,
            format,
            ajax,
            lengthToGroup,
            collapseSelected,
            badgeFieldId,
            badgeColorFieldId,
            groupFieldId,
            valueFieldId,
            style,
            expandPopUp,
            parentFieldId,
            hasChildrenFieldId,
        } = this.props
        const inputSelectStyle = { width: '100%', cursor: 'text', ...style }

        return (
            <Dropdown
                style={inputSelectStyle}
                className="n2o-input-select"
                toggle={() => {}}
                onBlur={() => {
                    this._setInputFocus(false)
                    this._setSelected(false)
                }}
                onFocus={() => {
                    this._setInputFocus(true)
                    this._setSelected(true)
                }}
                isOpen={this.state.isExpanded && !disabled}
            >
                <DropdownToggle disabled={disabled}>
                    <InputSelectGroup
                        isExpanded={this.state.isExpanded}
                        setIsExpanded={this._setIsExpanded}
                        loading={loading}
                        selected={this.state.value}
                        input={this.state.input}
                        iconFieldId={iconFieldId}
                        imageFieldId={imageFieldId}
                        multiSelect={multiSelect}
                        isInputInFocus={this.state.inputFocus}
                        onClearClick={this._handleElementClear}
                    >
                        <InputContent
                            loading={loading}
                            value={this.state.input}
                            disabledValues={disabledValues}
                            valueFieldId={valueFieldId}
                            placeholder={placeholder}
                            options={this.state.options}
                            openPopUp={() => this._setIsExpanded(true)}
                            closePopUp={() => this._setIsExpanded(false)}
                            onInputChange={this._setNewInputValue}
                            onRemoveItem={this._removeSelectedItem}
                            isExpanded={this.state.isExpanded}
                            isSelected={this.state.isInputSelected}
                            inputFocus={this.state.inputFocus}
                            iconFieldId={iconFieldId}
                            activeValueId={this.state.activeValueId}
                            setActiveValueId={this._setActiveValueId}
                            imageFieldId={imageFieldId}
                            selected={this.state.value}
                            labelFieldId={labelFieldId}
                            clearSelected={this._clearSelected}
                            multiSelect={multiSelect}
                            onClick={this._handleClick}
                            onSelect={this._handleItemSelect}
                        />
                    </InputSelectGroup>
                </DropdownToggle>
                <Popup isExpanded={this.state.isExpanded} expandPopUp={expandPopUp}>
                    <TreeItems
                        value={this.state.value}
                        options={this.state.options}
                        treeStates={this.state.treeStates}
                        hasCheckboxes={hasCheckboxes}
                        imageFieldId={imageFieldId}
                        iconFieldId={iconFieldId}
                        labelFieldId={labelFieldId}
                        valueFieldId={valueFieldId}
                        parentFieldId={parentFieldId}
                        badgeFieldId={badgeFieldId}
                        badgeColorFieldId={badgeColorFieldId}
                        format={format}
                        hasChildrenFieldId={hasChildrenFieldId}
                        ajax={ajax}
                        active={this.state.active}
                        handleSelect={this._handleItemSelect}
                        handleDelete={this._handleRemove}
                        onExpandClick={this.onExpandClick}
                        handleFocus={this.handleFocus}
                        disabledValues={disabledValues}
                        groupFieldId={groupFieldId}
                    />
                </Popup>
            </Dropdown>
        )
    }
}

InputSelectTree.propTypes = {
    hasChildrenFieldId: PropTypes.bool,
    parentFieldId: PropTypes.string,
    loading: PropTypes.bool,
    options: PropTypes.array.isRequired,
    valueFieldId: PropTypes.string,
    labelFieldId: PropTypes.string,
    iconFieldId: PropTypes.string,
    imageFieldId: PropTypes.string,
    badgeFieldId: PropTypes.string,
    badgeColorFieldId: PropTypes.string,
    groupFieldId: PropTypes.string,
    disabled: PropTypes.bool,
    disabledValues: PropTypes.array,
    filter: PropTypes.oneOf(['includes', 'startsWith', 'endsWith', false]),
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    onInput: PropTypes.func,
    onChange: PropTypes.func,
    onSelect: PropTypes.func,
    placeholder: PropTypes.string,
    onOpen: PropTypes.func,
    onClose: PropTypes.func,
    multiSelect: PropTypes.bool,
    closePopupOnSelect: PropTypes.bool,
    hasCheckboxes: PropTypes.bool,
    format: PropTypes.string,
    collapseSelected: PropTypes.bool,
    lengthToGroup: PropTypes.number,
    onSearch: PropTypes.func,
    expandPopUp: PropTypes.bool,
    ajax(props, propName, componentName) {
        if (props[propName] && props.multiSelect) {
            return new Error(
                `Invalid prop \`${propName}\` supplied to` +
          ` \`${componentName}\`. Ajax and multiSelect together doesn't allowed.`,
            )
        }
    },
    handleItemOpen: PropTypes.func,
}

InputSelectTree.defaultProps = {
    hasChildrenFieldId: 'hasChildren',
    parentFieldId: 'parentId',
    valueFieldId: 'id',
    labelFieldId: 'name',
    iconFieldId: 'icon',
    imageFieldId: 'image',
    badgeFieldId: 'badge',
    loading: false,
    disabled: false,
    disabledValues: [],
    resetOnBlur: false,
    filter: false,
    multiSelect: false,
    closePopupOnSelect: true,
    hasCheckboxes: false,
    collapseSelected: true,
    lengthToGroup: 3,
    ajax: false,
    expandPopUp: true,
    onSearch() {},
    onSelect() {},
    onToggle() {},
    onInput() {},
    onOpen() {},
    onClose() {},
    onChange() {},
    onScrollEnd() {},
    onElementCreate() {},
}

const fff = {
    className: 'n2o',
    prefixCls: '',
    animation: '',
    transitionName: '',
    choiceTransitionName: '',
    dropdownMatchSelectWidth: false,
    dropdownClassName: '',
    dropdownStyle: {},
    dropdownPopupAlign: null,
    onDropdownVisibleChange: () => true,
    notFoundContent: 'Ничего не найдено',
    showSearch: true,
    allowClear: false,
    maxTagTextLength: null,
    maxTagCount: null,
    maxTagPlaceholder: null,
    multiple: false,
    disabled: false,
    searchValue: '',
    defaultValue: null,
    value: null,
    labelInValue: false,
    onChange: null,
    onSelect: null,
    onSearch: null,
    onTreeExpand: null,
    showCheckedStrategy: SHOW_CHILD,
    treeIcon: false,
    treeLine: false,
    treeDefaultExpandAll: false,
    treeDefaultExpandedKeys: null,
    treeExpandedKeys: null,
    treeCheckable: false,
    treeCheckStrictly: false,
    filterTreeNode: Function,
    treeNodeFilterProp: 'value',
    treeNodeLabelProp: 'title',
    treeData: [
        { key: 1, pId: 0, label: 'test1', value: 'test1' },
        { key: 121, pId: 0, label: 'test2', value: 'test2' },
        { key: 11, pId: 1, label: 'test11', value: 'test11' },
        { key: 12, pId: 1, label: 'test12', value: 'test12' },
        { key: 111, pId: 11, label: 'test111', value: 'test111' },
    ],
    treeDataSimpleMode: false,
    loadData: null,
    getPopupContainer: () => document.body,
    autoClearSearchValue: true,
    inputIcon: null,
    clearIcon: null,
    removeIcon: null,
    switcherIcon: null,
}

export default onClickOutside(InputSelectTree)

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

import Popup from '../InputSelect/Popup'
import InputContent from '../InputSelect/InputContent'
import InputSelectGroup from '../InputSelect/InputSelectGroup'

import NODE_SELECTED from './nodeSelected'
import TreeItems from './TreeItems'

const getArray = (value) => {
    if (Array.isArray(value)) { return value }
    if (value) { return [value] }

    return []
}

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
 * @reactProps {boolean} ajax - загрузка элементов при раскрытии
 */

class InputSelectTree extends React.Component {
    constructor(props) {
        super(props)
        const {
            value,
            options,
            labelFieldId,
            multiSelect,
        } = this.props
        const input = value && !multiSelect ? value[labelFieldId] : ''

        this.state = {
            isExpanded: false,
            value: value || [],
            selected: [],
            options,
            active: null,
            treeStates: this.getTreeItems(options),
            input,
        }

        this.handleInputChange = this.handleInputChange.bind(this)
        this.hideOptionsList = this.hideOptionsList.bind(this)
        this.handleItemSelect = this.handleItemSelect.bind(this)
        this.removeSelectedItem = this.removeSelectedItem.bind(this)
        this.clearSelected = this.clearSelected.bind(this)
        this.handleElementClear = this.handleElementClear.bind(this)
        this.onExpandClick = this.onExpandClick.bind(this)
        this.handleRemove = this.handleRemove.bind(this)
        this.handleKeyPress = this.handleKeyPress.bind(this)
        this.handleFocus = this.handleFocus.bind(this)
        this.handleResetOnBlur = this.handleResetOnBlur.bind(this)
        this.toggle = this.toggle.bind(this)
        this.handleArrowDown = this.handleArrowDown.bind(this)
        this.handleArrowUp = this.handleArrowUp.bind(this)
        this.handleArrowRight = this.handleArrowRight.bind(this)
        this.handleArrowLeft = this.handleArrowLeft.bind(this)
        this.handleEnter = this.handleEnter.bind(this)
        this.setIsExpanded = this.setIsExpanded.bind(this)
        this.handleClick = this.handleClick.bind(this)
        this.clearSelected = this.clearSelected.bind(this)
        this.setNewInputValue = this.setNewInputValue.bind(this)
        this.setInputFocus = this.setInputFocus.bind(this)

        this.KEY_MAPPER = {
            ArrowDown: this.handleArrowDown,
            ArrowUp: this.handleArrowUp,
            ArrowRight: this.handleArrowRight,
            ArrowLeft: this.handleArrowLeft,
            Enter: this.handleEnter,
        }
    }

    componentWillReceiveProps(nextProps) {
        const {
            multiSelect,
            value,
            labelFieldId,
            options,
        } = nextProps
        const { options: stateOptions } = this.state
        const { value: propsValue } = this.props

        if (!isEqual(nextProps.options, stateOptions)) {
            this.setState({ options, treeStates: this.getTreeItems(options) })
        }
        if (!isEqual(nextProps.value, propsValue)) {
            const valueArray = getArray(value)
            const input = value && !multiSelect ? value[labelFieldId] : ''

            this.setState({ value: valueArray, input })
        }
    }

    // eslint-disable-next-line consistent-return
    handleChange() {
        const { selected } = this.state
        const { onChange, multiSelect } = this.props

        if (!onChange) {
            return false
        }

        if (selected.length) {
            if (multiSelect) {
                onChange(selected)
            } else {
                onChange(selected[0])
            }
        } else {
            onChange(null)
        }
    }

    /**
     * Обработчик события по удалению элемента из выбранных
     * @param item - элемент для удаления
     * @private
     */
    handleRemove(item) {
        let items = []
        const { multiSelect } = this.props

        if (multiSelect) {
            items = [...this.retrieveChilds(item), item]
            this.changeSelected(items, NODE_SELECTED.NOT_SELECTED)
            this.unselectParent(item)
        } else {
            items.push(item)
        }

        items.map(node => this.removeSelectedItem(node))
    }

    /**
     * Удаляет элемент из списка выбранных
     * @param item - элемент
     * @private
     */
    removeSelectedItem(item) {
        const { onChange } = this.props
        const { value: stateValue } = this.state
        const value = stateValue.filter(i => i.id !== item.id)

        this.setState({ value }, onChange(this.getValue()))
    }

    /**
     * новое значение инпута search)
     * @param input
     * @private
     */
    setNewInputValue(input) {
        const { onInput } = this.props
        const { input: stateInput } = this.state
        const onSetNewInputValue = (input) => {
            onInput(input)
            this.handleDataSearch(input)
        }

        if (stateInput !== input) {
            this.setSelected(false)
            this.setState({ input }, () => onSetNewInputValue(input))
        }
    }

    /**
     * Изменение видимости попапа
     * @param newState - новое значение видимости
     * @private
     */
    changePopUpVision(newState) {
        const { onClose } = this.props
        const { isExpanded } = this.state

        if (isExpanded === newState) {
            return
        }

        if (isExpanded && onClose) {
            onClose()
        }

        this.setState({
            isExpanded: newState,
        })
    }

    /**
     * Скрывает popUp
     * @private
     */
    hideOptionsList() {
        this.changePopUpVision(false)
    }

    /**
     * Уставнавливает новое значение инпута
     * @param newValue - новое значение
     * @private
     */
    setNewValue(newValue) {
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
    makeSearch(options, searchValue) {
        const { valueFieldId, parentFieldId, filter: propsFilter, labelFieldId } = this.props
        const { options: stateOptions } = this.state
        const filter = item => String.prototype[propsFilter].call(item, searchValue)

        return options.filter((item) => {
            const children = stateOptions.filter(
                node => node[parentFieldId] === item[valueFieldId],
            )

            return (
                filter(item[labelFieldId].toString()) ||
                this.makeSearch(children, searchValue).length > 0
            )
        })
    }

    /**
     * Выполняет поиск элементов для popUp, если установлен фильтр
     * @private
     */
    handleDataSearch(input, delay = true, callback) {
        const { onSearch, filter, labelFieldId, options } = this.props
        const { value } = this.props

        if (filter && ['includes', 'startsWith', 'endsWith'].includes(filter)) {
            const filterFunc = item => String.prototype[filter].call(item, input)
            const filteredData = options.filter(item => filterFunc(item[labelFieldId]))

            this.setState({ options: filteredData })
        } else {
            // серверная фильтрация
            const labels = value.map(item => item[labelFieldId])

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
    insertSelected(items) {
        const { valueFieldId, multiSelect } = this.props

        if (multiSelect) {
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
    handleSelect(item) {
        const { multiSelect } = this.props
        let items = []

        if (multiSelect) {
            items = [item, ...this.retrieveChilds(item)]
            this.selectParent(item)
        } else {
            items.push(item)
            this.unselectAll()
        }

        this.insertSelected(items)
        this.changeSelected(items, NODE_SELECTED.SELECTED)
    }

    /**
     * Изменяет стейт выбора всех элементов
     * @private
     */
    unselectAll() {
        const { treeStates } = this.state

        // eslint-disable-next-line no-unused-vars
        Object.entries(treeStates).forEach(([key, item]) => {
            if (item.selected === NODE_SELECTED.SELECTED) {
                item.selected = NODE_SELECTED.NOT_SELECTED
            }
        })

        this.setState({ treeStates })
    }

    /**
     * Изменяет стейт выбора для одного элемент
     * @param node - элемент дерева
     * @private
     */
    unselectNode(node) {
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
    retrieveChilds(item) {
        const { valueFieldId, parentFieldId } = this.props
        const { options } = this.state
        const children = options.filter(
            node => node[parentFieldId] === item[valueFieldId],
        )

        if (children.length > 0) {
            children.forEach((child) => {
                children.concat(this.retrieveChilds(child))
            })
        }

        return children
    }

    /**
     * Удаляет все элементы поддерева из выбранных
     * @param item - корень поддерева
     * @private
     */
    removeChild(item) {
        const { valueFieldId, parentFieldId, multiSelect } = this.props
        const { options } = this.state

        if (multiSelect) {
            const children = options.filter(
                node => node[parentFieldId] === item[valueFieldId],
            )

            if (children.length > 0) {
                children.forEach((child) => {
                    this.removeSelectedItem(child)
                    this.removeChild(child)
                })
            }
        }
    }

    /**
     * Очищает выбранный элемент
     * @private
     */
    clearSelected() {
        const { onChange } = this.props

        this.setState({ value: [], input: '' }, () => onChange(this.getValue()))
    }

    /**
     * Обрабатывает изменение инпута
     * @param newValue - новое значение
     * @private
     */
    handleInputChange(newValue) {
        this.setNewValue(newValue)
        this.handleDataSearch(newValue)

        const { resetOnBlur, onChange, onInput } = this.props

        if (!resetOnBlur && onChange) {
            onChange(newValue)
        }

        if (onInput) {
            onInput(newValue)
        }
    }

    /**
     * Возвращает текущее значение (массив - если ипут селект, объект - если нет)
     * или null если пусто
     * @returns {*}
     * @private
     */
    getValue() {
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

    handleItemSelect(item) {
        const {
            multiSelect,
            closePopupOnSelect,
            labelFieldId,
            options,
            onSelect,
            onChange,
        } = this.props
        const selectCallback = () => {
            if (closePopupOnSelect) {
                this.hideOptionsList()
            }
            onSelect(item)
            onChange(this.getValue())
            this.setSelected(true)
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
    clearSearchField() {
        const { options } = this.props

        this.setState({
            value: '',
            options,
        })
    }

    /**
     * Обрабатывает поведение инпута при потери фокуса, если есть resetOnBlur
     * @private
     */
    handleResetOnBlur() {
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
                        () => onChange(this.getValue()),
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
                    () => onChange(this.getValue()),
                )
            } else {
                this.setState(
                    {
                        input: '',
                        value: multiSelect ? value : [],
                    },
                    () => onChange(this.getValue()),
                )
            }
        } else {
            this.setState(
                {
                    value: multiSelect ? [...value, newValue] : [newValue],
                    input: multiSelect ? '' : input,
                },
                () => onChange(this.getValue()),
            )
        }
    }

    handleElementClear() {
        this.clearSearchField()
        this.clearSelected()
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
        const { treeStates } = this.state
        const itemState = treeStates[item[valueFieldId]]

        this.handleExpandClick(itemState)
    }

    /**
     * Получает state для новых элементов дерева
     * @param options
     */
    getTreeItems(options) {
        const result = {}
        const { valueFieldId, parentFieldId, hasChildrenFieldId } = this.props
        const treeStates = get(this, 'state.treeStates', false)

        options.map(
            // eslint-disable-next-line no-return-assign
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
     * Изменяет стейт элемента дерева
     * @param itemState - стейс элемента
     * @param id - id элемента
     */
    overrideElement(itemState, id) {
        const { treeStates } = this.state
        const newData = {
            ...treeStates,
            [id]: itemState,
        }

        this.setState({ treeStates: newData })
    }

    /**
     * Изменения статус выбора у заданных элементов
     * @param nodes - элементы дерева
     * @param newStatus - новый статус
     */
    changeSelected(nodes, newStatus) {
        const { valueFieldId } = this.props
        const { treeStates } = this.state

        nodes.forEach((node) => {
            const nodeState = treeStates[node[valueFieldId]]

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
        const { options, treeStates } = this.state
        const parent = options[
            options.findIndex(
                parent => parent[valueFieldId] === item[parentFieldId],
            )
        ]

        if (!parent) {
            return
        }

        const parentState = treeStates[parent[valueFieldId]]

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
        const { options, treeStates } = this.state
        const parent = options[
            options.findIndex(
                parent => parent[valueFieldId] === item[parentFieldId],
            )
        ]
        const parentState = parent
            ? treeStates[parent[valueFieldId]]
            : null

        if (parentState && parentState.selected === NODE_SELECTED.PARTIALLY) {
            const selectedChild = options.find(
                child => child[parentFieldId] === parent[valueFieldId] &&
                treeStates[child[valueFieldId]].selected === NODE_SELECTED.SELECTED,
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
    handleArrowDown() {
        const { active } = this.state

        if (active) {
            this.setNextNodeActive()
        } else {
            this.setFirstNodeActive()
        }
    }

    /**
     * Обрабатывает нажатия на кнопку клавиатуры "Стрелка вверх"
     * @private
     */
    handleArrowUp() {
        const { active } = this.state

        if (active) {
            this.setPrevNodeActive()
        }
    }

    /**
     * Обрабатывает нажатия на кнопку клавиатуры "Стрелка вправо"
     * @private
     */
    handleArrowRight() {
        const { active } = this.state

        if (active) {
            if (!active.expanded && active.hasChildren) {
                this.handleExpandClick(active, true)
            } else {
                this.setNextNodeActive()
            }
        } else {
            this.setFirstNodeActive()
        }
    }

    /**
     * Обрабатывает нажатия на кнопку клавиатуры "Стрелка влево"
     * @private
     */
    handleArrowLeft() {
        const { active, treeStates } = this.state
        const { parentFieldId } = this.props

        if (active) {
            if (active.expanded) {
                this.collapseNode()
            } else if (active[parentFieldId]) {
                this.setState({ active: treeStates[active[parentFieldId]] })
                this.scrollOntoElement(treeStates[active[parentFieldId]])
            } else {
                this.setPrevNodeActive()
            }
        }
    }

    /**
     * Схлопывает элемент дерева
     * @private
     */
    collapseNode() {
        const { valueFieldId } = this.props
        const { active } = this.state
        const activeItem = {
            ...active,
            expanded: false,
        }

        this.overrideElement(activeItem, activeItem[valueFieldId])
        this.setState({ active: activeItem })
    }

    /**
     * Устанавливает первый элемент дерева активным
     * @private
     */
    setFirstNodeActive() {
        const { valueFieldId, parentFieldId } = this.props
        const { options, treeStates } = this.state
        const rootNodes = options.filter(node => !node[parentFieldId])

        this.setState({
            active: treeStates[rootNodes[0][valueFieldId]],
        })
    }

    /**
     * Устанавливает следующий элемент дерева активный
     * @private
     */
    setNextNodeActive() {
        const { valueFieldId } = this.props
        const { active, treeStates } = this.state
        const nextItem = this.getNextItem(active)

        if (nextItem) {
            const nextItemState = treeStates[nextItem[valueFieldId]]

            this.setState({ active: nextItemState })
            this.scrollOntoElement(nextItemState)
        }
    }

    /**
     * Устанавливает предыдущий элемент дерева активный
     * @private
     */
    setPrevNodeActive() {
        const { valueFieldId } = this.props
        const { active, treeStates } = this.state
        const prevItem = this.getPrevItem(active)

        if (prevItem) {
            const prevItemState = treeStates[prevItem[valueFieldId]]

            this.setState({ active: prevItemState })
            this.scrollOntoElement(prevItemState)
        }
    }

    // eslint-disable-next-line class-methods-use-this
    scrollOntoElement(nodeState) {
        // eslint-disable-next-line react/no-find-dom-node
        const nodeElement = ReactDOM.findDOMNode(nodeState.ref.current)

        nodeElement.scrollIntoView()
    }

    /**
     * Обрабатывает нажатие кнопки Enter
     * @private
     */
    handleEnter() {
        const { active } = this.state

        if (active) {
            this.handleItemSelect(active)
        }
    }

    /**
     * Обрабатывает нажатия на кнопку клавиатуры
     * @param e - событие нажатия
     */
    handleKeyPress(e) {
        // eslint-disable-next-line no-prototype-builtins
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
            return this.findPrevParentItem(currentItem)
        }

        return this.findPrevRootItem(currentItem)
    }

    /**
     * Находит предыдущий элемент без родителя
     * @param currentItem - текущий активный элемент
     * @returns {*}
     * @private
     */
    findPrevRootItem(currentItem) {
        const { valueFieldId, parentFieldId } = this.props
        const { options, treeStates } = this.state
        const rootNodes = options.filter(node => !node[parentFieldId])
        const prevNode = this.findPrevItem(rootNodes, currentItem)

        if (!prevNode) {
            this.changePopUpVision(false)

            return null
        } if (!treeStates[prevNode[valueFieldId]].expanded) {
            return prevNode
        }

        return this.getLowest(prevNode)
    }

    /**
     * Находит предыдущий элемент-родитель
     * @param currentItem - текущий элемент
     * @returns {*}
     * @private
     */
    findPrevParentItem(currentItem) {
        const { valueFieldId, parentFieldId } = this.props
        const { options, active } = this.state
        const sameLevels = options.filter(
            node => node[parentFieldId] === currentItem[parentFieldId],
        )
        const nextItem = this.findPrevItem(sameLevels, active)

        if (nextItem) {
            return nextItem
        }

        return options[
            options.findIndex(
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
    getLowest(item) {
        const { valueFieldId, parentFieldId } = this.props
        const { treeStates, options } = this.state
        const children = options.filter(
            node => node[parentFieldId] === item[valueFieldId],
        )
        const lastChild = children[children.length - 1]

        if (treeStates[lastChild[valueFieldId]].expanded) {
            return this.getLowest(lastChild)
        }

        return lastChild
    }

    /**
     * Находит следующий элемент
     * @param currentItem
     * @param lookChildren
     * @returns {*}
     */
    getNextItem(currentItem, lookChildren = true) {
        const { valueFieldId, parentFieldId } = this.props
        const { options, treeStates } = this.state
        const children = options.filter(
            node => node[parentFieldId] === currentItem[valueFieldId],
        )
        const itemState = treeStates[currentItem[valueFieldId]]

        if (lookChildren && itemState.expanded && children.length > 0) {
            return children[0]
        } if (currentItem[parentFieldId]) {
            return this.findNextParentItem(currentItem)
        }
        const rootNodes = options.filter(node => !node[parentFieldId])

        return this.findNextItem(rootNodes, currentItem)
    }

    /**
     * Находит следующий элемент-родителя
     * @param currentItem
     * @returns {*}
     * @private
     */
    findNextParentItem(currentItem) {
        const { valueFieldId, parentFieldId } = this.props
        const { options, active } = this.state
        const sameLevels = options.filter(
            node => node[parentFieldId] === currentItem[parentFieldId],
        )
        const nextItem = this.findNextItem(sameLevels, active)

        if (nextItem) {
            return nextItem
        }
        const parent = options[
            options.findIndex(
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
    setIsExpanded(isExpanded) {
        const { onToggle, onClose, onOpen } = this.props
        const { isExpanded: previousIsExpanded } = this.state

        if (isExpanded !== previousIsExpanded) {
            this.setState({ isExpanded })
            onToggle()
            if (isExpanded) {
                onOpen()
            } else {
                onClose()
            }
        }
    }

    /**
     * Получает следующий по-порядку элемент из списка элементов
     * @param nodes - список элементов
     * @param active - активный элемент
     * @returns {null}
     * @private
     */

    findNextItem(nodes, active) {
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
    findPrevItem(nodes, active) {
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
    async handleExpandClick(itemState, newState = null) {
        const { valueFieldId, ajax, handleItemOpen } = this.props

        if (ajax && !itemState.loaded) {
            itemState.expanded = true
            itemState.loaded = true
            handleItemOpen(itemState[valueFieldId])
        } else {
            itemState.expanded = newState || !itemState.expanded
        }

        this.overrideElement(itemState, itemState[valueFieldId])
    }

    handleClick() {
        this.setIsExpanded(true)
        this.setSelected(false)
        this.setInputFocus(true)
    }

    toggle() {
        const { closePopupOnSelect } = this.props

        this.setState((prevState) => {
            if (!closePopupOnSelect && prevState.isExpanded) { return false }

            return { isExpanded: !prevState.isExpanded }
        })
    }

    setSelected(isInputSelected) {
        this.setState({ isInputSelected })
    }

    setInputFocus(inputFocus) {
        this.setState({ inputFocus })
    }

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
        const {
            isExpanded,
            value: stateValue,
            options: stateOptions,
            active,
            inputFocus,
            input,
            treeStates,
            isInputSelected,
            activeValueId,
        } = this.state

        return (
            <Dropdown
                style={inputSelectStyle}
                className="n2o-input-select"
                toggle={() => {}}
                onBlur={() => {
                    this.setInputFocus(false)
                    this.setSelected(false)
                }}
                onFocus={() => {
                    this.setInputFocus(true)
                    this.setSelected(true)
                }}
                isOpen={isExpanded && !disabled}
            >
                <DropdownToggle disabled={disabled}>
                    <InputSelectGroup
                        isExpanded={isExpanded}
                        setIsExpanded={this.setIsExpanded}
                        loading={loading}
                        selected={stateValue}
                        input={input}
                        iconFieldId={iconFieldId}
                        imageFieldId={imageFieldId}
                        multiSelect={multiSelect}
                        isInputInFocus={inputFocus}
                        onClearClick={this.handleElementClear}
                    >
                        <InputContent
                            loading={loading}
                            value={input}
                            disabledValues={disabledValues}
                            valueFieldId={valueFieldId}
                            placeholder={placeholder}
                            options={stateOptions}
                            openPopUp={() => this.setIsExpanded(true)}
                            closePopUp={() => this.setIsExpanded(false)}
                            onInputChange={this.setNewInputValue}
                            onRemoveItem={this.removeSelectedItem}
                            isExpanded={isExpanded}
                            isSelected={isInputSelected}
                            inputFocus={inputFocus}
                            iconFieldId={iconFieldId}
                            activeValueId={activeValueId}
                            setActiveValueId={this.setActiveValueId}
                            imageFieldId={imageFieldId}
                            selected={stateValue}
                            labelFieldId={labelFieldId}
                            clearSelected={this.clearSelected}
                            multiSelect={multiSelect}
                            onClick={this.handleClick}
                            onSelect={this.handleItemSelect}
                        />
                    </InputSelectGroup>
                </DropdownToggle>
                <Popup isExpanded={isExpanded} expandPopUp={expandPopUp}>
                    <TreeItems
                        value={stateValue}
                        options={stateOptions}
                        treeStates={treeStates}
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
                        active={active}
                        handleSelect={this.handleItemSelect}
                        handleDelete={this.handleRemove}
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
    style: PropTypes.object,
    onToggle: PropTypes.func,
    resetOnBlur: PropTypes.bool,
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
    onSearch: PropTypes.func,
    expandPopUp: PropTypes.bool,
    // eslint-disable-next-line consistent-return
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
    ajax: false,
    expandPopUp: true,
    onSearch() {},
    onSelect() {},
    onToggle() {},
    onInput() {},
    onOpen() {},
    onClose() {},
    onChange() {},
}

export default onClickOutside(InputSelectTree)

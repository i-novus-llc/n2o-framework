import React from 'react'
import { compose, setDisplayName } from 'recompose'
import { findDOMNode } from 'react-dom'
import PropTypes from 'prop-types'
import onClickOutside from 'react-onclickoutside'
import cx from 'classnames'
import { Dropdown, DropdownToggle, DropdownMenu } from 'reactstrap'
import find from 'lodash/find'
import isEqual from 'lodash/isEqual'
import isEmpty from 'lodash/isEmpty'

import Alert from '../../snippets/Alerts/Alert'

import InputContent from './InputContent'
import PopupList from './PopupList'
import InputSelectGroup from './InputSelectGroup'

/**
 * InputSelect
 * @reactProps {object} style - css стили
 * @reactProps {boolean} loading - флаг анимации загрузки
 * @reactProps {array} options - данные
 * @reactProps {string} valueFieldId - значение ключа value в данных
 * @reactProps {string} labelFieldId - значение ключа label в данных
 * @reactProps {string} iconFieldId - поле для иконки
 * @reactProps {string} imageFieldId - поле для картинки
 * @reactProps {string} badgeFieldId - поле для баджей
 * @reactProps {string} badgeColorFieldId - поле для цвета баджа
 * @reactProps {string} statusFieldId - поле для статуса
 * @reactProps {string} descriptionFieldId - поле для описания
 * @reactProps {boolean} disabled - флаг неактивности
 * @reactProps {array} disabledValues - неактивные данные
 * @reactProps {string} filter - варианты фильтрации
 * @reactProps {string} value - текущее значение
 * @reactProps {function} onToggle
 * @reactProps {function} onInput - callback при вводе в инпут
 * @reactProps {function} onChange - callback при выборе значения или вводе
 * @reactProps {function} onSelect
 * @reactProps {function} onScrollENd - callback при прокрутке скролла popup
 * @reactProps {string} placeHolder - подсказка в инпуте
 * @reactProps {boolean} resetOnBlur - фича, при которой: (значение - true) - сбрасывается значение контрола, если оно не выбрано из popup, (значение - false) - создает объект в текущем value
 * @reactProps {function} onOpen - callback на открытие попапа
 * @reactProps {function} onClose - callback на закрытие попапа
 * @reactProps {boolean} multiSelect - флаг мульти выбора
 * @reactProps {string} groupFieldId - поле для группировки
 * @reactProps {boolean} closePopupOnSelect - флаг закрытия попапа при выборе
 * @reactProps {boolean} hasCheckboxes - флаг наличия чекбоксов
 * @reactProps {string} format - формат
 * @reactProps {function} onSearch
 * @reactProps {boolean} expandPopUp
 * @reactProps {array} alerts
 * @reactProps {boolean} popupAutoSize - флаг включения автоматическиого расчета длины PopUp
 */

class InputSelect extends React.Component {
    constructor(props) {
        super(props)
        const { value, options, labelFieldId, multiSelect } = this.props
        const valueArray = Array.isArray(value) ? value : value ? [value] : []
        const input = value && !multiSelect ? value[labelFieldId] : ''
        this.state = {
            inputFocus: props.autoFocus || false,
            isExpanded: false,
            isInputSelected: false,
            value: valueArray,
            activeValueId: null,
            options,
            input,
        }

        this._hideOptionsList = this._hideOptionsList.bind(this)
        this._handleItemSelect = this._handleItemSelect.bind(this)
        this._removeSelectedItem = this._removeSelectedItem.bind(this)
        this._setIsExpanded = this._setIsExpanded.bind(this)
        this._handleClick = this._handleClick.bind(this)
        this._clearSelected = this._clearSelected.bind(this)
        this._setNewInputValue = this._setNewInputValue.bind(this)
        this._setInputFocus = this._setInputFocus.bind(this)
        this._setActiveValueId = this._setActiveValueId.bind(this)
        this._handleValueChangeOnSelect = this._handleValueChangeOnSelect.bind(
            this,
        )
        this._handleValueChangeOnBlur = this._handleValueChangeOnBlur.bind(this)
        this._handleDataSearch = this._handleDataSearch.bind(this)
        this._handleElementClear = this._handleElementClear.bind(this)
        this.setSelectedItemsRef = this.setSelectedItemsRef.bind(this)
        this.setTextareaRef = this.setTextareaRef.bind(this)
        this.setSelectedListRef = this.setSelectedListRef.bind(this)
        this.onInputBlur = this.onInputBlur.bind(this)
        this.onFocus = this.onFocus.bind(this)
        this.setInputRef = this.setInputRef.bind(this)
        this.addObjectToValue = this.addObjectToValue.bind(this)
        this.toggle = this.toggle.bind(this)
    }

    setTextareaRef(poperRef) {
        return (r) => {
            this._textarea = r
            poperRef(r)
        }
    }

    setSelectedListRef(selectedList) {
        this._selectedList = selectedList
    }

    componentWillReceiveProps(nextProps) {
        const state = {}
        const { multiSelect, value, labelFieldId, options } = nextProps
        if (!isEqual(nextProps.options, this.state.options)) {
            state.options = options
        }
        if (!isEqual(nextProps.value, this.props.value)) {
            const valueArray = Array.isArray(value) ? value : value ? [value] : []
            const input = value && !multiSelect ? value[labelFieldId] : ''

            state.value = valueArray
            state.input = input
        }

        if (!isEmpty(state)) {
            this.setState(state)
        }
    }

    /**
   * установить акстивный элемент дропдауна
   * @param activeValueId
   * @private
   */
    _setActiveValueId(activeValueId) {
        this.setState({ activeValueId })
    }

    /**
   * обработка изменения значения при потери фокуса(считаем, что при потере фокуса пользователь закончил вводить новое значение)
   * @private
   */
    _handleValueChangeOnBlur() {
        const { value, input } = this.state
        const {
            onChange,
            multiSelect,
            resetOnBlur,
            labelFieldId,
            options,
        } = this.props
        const findValue = find(value, [labelFieldId, input])
        const conditionForAddingAnObject = (resetOnBlur, input, options, value) => (
            !resetOnBlur &&
        input.split(' ').every(char => char === '') !== true &&
        options.some(person => person.id === input) !== true &&
        value.some(person => person.id === input) !== true
        )

        if (input && isEmpty(findValue) && resetOnBlur) {
            this.setState(
                {
                    input: multiSelect ? '' : (value[0] && value[0][labelFieldId]) || '',
                    value,
                },
                () => onChange(this._getValue()),
            )
        }
        if (!input && value.length) {
            this.setState(
                {
                    input: '',
                    value: multiSelect ? value : [],
                },
                () => onChange(this._getValue()),
            )
        }
        if (conditionForAddingAnObject(resetOnBlur, input, options, value)) {
            this.addObjectToValue()
        }
    }

    /**
   * Обработка клика на инпут
   * @private
   */
    _handleClick() {
        this.setState({
            inputFocus: true,
            isInputSelected: false,
        })
    }

    /**
   * Обработка изменения значения при выборе из дропдауна
   * @param item
   * @private
   */
    _handleValueChangeOnSelect(item) {
        const { value } = this.state
        const { onChange, multiSelect, labelFieldId } = this.props
        this.setState(
            {
                input: multiSelect ? item[labelFieldId] : '',
                value: multiSelect ? [...value, item] : [item],
            },
            () => {
                onChange(this._getValue())
            },
        )
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
   * Удаляет элемент из списка выбранных
   * @param item - элемент
   * @private
   */

    _removeSelectedItem(item) {
        const { onChange } = this.props
        const value = this.state.value.filter(i => i.id !== item.id)
        this.setState({ value }, () => onChange(value))
    }

    /**
   * Скрывает popUp
   * @private
   */

    _hideOptionsList() {
        this._setInputFocus(false)
        this._setIsExpanded(false)
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
   * установить / сбросить фокус
   * @param inputFocus
   * @private
   */
    _setInputFocus(inputFocus) {
        this.setState({ inputFocus })
    }

    /**
   * скрыть / показать попап
   * @param isExpanded
   * @private
   */
    _setIsExpanded(isExpanded) {
        const { disabled, onToggle, onClose, onOpen } = this.props
        const { isExpanded: previousIsExpanded, inputFocus, input } = this.state

        if (!disabled && isExpanded !== previousIsExpanded) {
            this.setState({ isExpanded })
            onToggle(isExpanded)

            isExpanded && (inputFocus || isEmpty(input)) ? onOpen() : onClose()
        }
    }

    /**
   * выделить текст
   * @param isInputSelected
   * @private
   */
    _setSelected(isInputSelected) {
        this.setState({ isInputSelected })
    }

    /**
   * Выполняет поиск элементов для popUp, если установлен фильтр
   * @param newValue - значение для поиска
   * @private
   */

    _handleDataSearch(input, delay = 400, callback) {
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
   * новое значение инпута search)
   * @param input
   * @private
   */
    _setNewInputValue(input) {
        const { onInput, isExpanded } = this.props
        const onSetNewInputValue = (input) => {
            onInput(input)
            this._handleDataSearch(input)
        }

        if (this.state.input !== input) {
            if (!isExpanded) {
                this._setIsExpanded(true)
            }
            this._setSelected(false)
            this.setState({ input }, () => onSetNewInputValue(input))
        }
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
            () => {
                selectCallback()
                this.props.onBlur(this._getValue())

                if (this._input) {
                    this._input.focus()
                }
            },
        )
    }

    /**
   * Очищает инпут и результаты поиска
   * @private
   */

    _clearSearchField() {
        this.setState({ input: '' }, this._handleDataSearch)
    }

    /**
   * Очищениеб сброс фокуса, выделенного значения
   * @private
   */
    _handleElementClear() {
        if (!this.props.disabled) {
            this._clearSearchField()
            this._clearSelected()
            this._setInputFocus(false)
        }
    }

    /**
   * Добавлет объект к текущему value, при resetOnBlur = false
   * @private
   */

    addObjectToValue() {
        const { multiSelect, labelFieldId } = this.props

        const userInput = this.state.input
        const currentValue = this.state.value
        const { options } = this.state

        Array.isArray(options) && multiSelect
            ? options.length === 0 &&
        this.setState({
            value: [...currentValue, { [labelFieldId]: userInput }],
            input: '',
        })
            : this.setState({
                value: [{ [labelFieldId]: userInput }],
            })
    }

    /**
   * Обрабатывает клик за пределы компонента
   * @param evt
   */

    handleClickOutside(evt) {
        const { resetOnBlur } = this.props
        const { isExpanded } = this.state
        if (isExpanded) {
            this._hideOptionsList()
            resetOnBlur && this._handleValueChangeOnBlur()
            this.props.onBlur(this._getValue())
        }
    }

    setSelectedItemsRef(ref) {
        this._selectedItems = ref
    }

    calcSelectedItemsWidth() {
        if (this._selectedItems) {
            const element = findDOMNode(this._selectedItems)
            if (element && element.getBoundingClientRect) {
                return element.getBoundingClientRect().width || undefined
            }
        }
    }

    onInputBlur() {
        if (!this.state.isExpanded) {
            this.props.onBlur(this._getValue())
        }
        this._handleValueChangeOnBlur()
        this._setInputFocus(false)
    }

    onFocus() {
        const { openOnFocus } = this.props

        if (openOnFocus) {
            this._setIsExpanded(true)
        }
    }

    setInputRef(popperRef) {
        return (r) => {
            this._input = r
            popperRef(r)
        }
    }

    toggle() {
        if (!this.state.isExpanded && !this.props.disabled) {
            this.setState(
                {
                    isExpanded: true,
                },
                this.props.onOpen,
            )
        }
    }

    /**
   * Рендер
   */
    render() {
        const {
            loading,
            className,
            valueFieldId,
            labelFieldId,
            iconFieldId,
            descriptionFieldId,
            disabled,
            placeholder,
            multiSelect,
            disabledValues,
            imageFieldId,
            groupFieldId,
            enabledFieldId,
            hasCheckboxes,
            format,
            badgeFieldId,
            statusFieldId,
            badgeColorFieldId,
            onScrollEnd,
            style,
            alerts,
            autoFocus,
            popupAutoSize,
        } = this.props

        const inputSelectStyle = { width: '100%', cursor: 'text', ...style }
        const selectedPadding = this.calcSelectedItemsWidth()
        const needAddFilter = !find(
            this.state.value,
            item => item[labelFieldId] === this.state.input,
        )
        return (
            <div
                style={inputSelectStyle}
                className={cx('n2o-input-select n2o-input-select--default', {
                    disabled,
                })}
            >
                <Dropdown isOpen={this.state.isExpanded} toggle={this.toggle}>
                    <DropdownToggle tag="div" className="n2o-input-select__toggle">
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
                            disabled={disabled}
                            className={className}
                            setSelectedItemsRef={this.setSelectedItemsRef}
                        >
                            <InputContent
                                setRef={() => () => {}}
                                onFocus={this.onFocus}
                                onBlur={this.onInputBlur}
                                loading={loading}
                                value={this.state.input}
                                disabled={disabled}
                                disabledValues={disabledValues}
                                valueFieldId={valueFieldId}
                                placeholder={placeholder}
                                options={this.state.options}
                                openPopUp={this._setIsExpanded}
                                closePopUp={this._setIsExpanded}
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
                                autoFocus={autoFocus}
                                selectedPadding={selectedPadding}
                                setTextareaRef={() => () => {}}
                                setSelectedListRef={this.setSelectedListRef}
                                _textarea={this._textarea}
                                _selectedList={this._selectedList}
                            />
                        </InputSelectGroup>
                    </DropdownToggle>

                    <DropdownMenu
                        className={cx('n2o-input-select__menu', {
                            'n2o-input-select__menu--autosize': popupAutoSize,
                        })}
                    >
                        <PopupList
                            scheduleUpdate={() => {}}
                            loading={loading}
                            isExpanded={this.state.isExpanded}
                            activeValueId={this.state.activeValueId}
                            setActiveValueId={this._setActiveValueId}
                            onScrollEnd={onScrollEnd}
                            filterValue={{
                                [labelFieldId]: this.state.input,
                            }}
                            needAddFilter={needAddFilter}
                            options={this.state.options}
                            valueFieldId={valueFieldId}
                            labelFieldId={labelFieldId}
                            iconFieldId={iconFieldId}
                            imageFieldId={imageFieldId}
                            badgeFieldId={badgeFieldId}
                            statusFieldId={statusFieldId}
                            descriptionFieldId={descriptionFieldId}
                            badgeColorFieldId={badgeColorFieldId}
                            onSelect={(item) => {
                                this._handleItemSelect(item)
                            }}
                            selected={this.state.value}
                            disabledValues={disabledValues}
                            groupFieldId={groupFieldId}
                            enabledFieldId={enabledFieldId}
                            hasCheckboxes={hasCheckboxes}
                            onRemoveItem={this._removeSelectedItem}
                            format={format}
                            inputSelect={this.inputSelect}
                        >
                            <div className="n2o-alerts">
                                {alerts &&
                  alerts.map(alert => (
                      <Alert
                          key={alert.id}
                          onDismiss={() => this.props.onDismiss(alert.id)}
                          {...alert}
                      />
                  ))}
                            </div>
                        </PopupList>
                    </DropdownMenu>
                </Dropdown>
            </div>
        )
    }
}

InputSelect.propTypes = {
    /**
   * Стили
   */
    style: PropTypes.object,
    /**
   * Флаг загрузки
   */
    loading: PropTypes.bool,
    /**
   * Массив данных
   */
    options: PropTypes.array.isRequired,
    /**
   * Ключ id в данных
   */
    valueFieldId: PropTypes.string.isRequired,
    /**
   * Ключ label в данных
   */
    labelFieldId: PropTypes.string.isRequired,
    /**
   * Ключ icon в данных
   */
    iconFieldId: PropTypes.string,
    /**
   * Ключ image в данных
   */
    imageFieldId: PropTypes.string,
    /**
   * Ключ badge в данных
   */
    badgeFieldId: PropTypes.string,
    /**
   * Ключ цвета badgeColor в данных
   */
    badgeColorFieldId: PropTypes.string,
    /**
   * Ключ сортировки в данных
   */
    sortFieldId: PropTypes.string,
    /**
   * Флаг активности
   */
    disabled: PropTypes.bool,
    /**
   * Неактивные данные
   */
    disabledValues: PropTypes.array,
    /**
   * Варианты фильтрации
   */
    filter: PropTypes.oneOf(['includes', 'startsWith', 'endsWith', false]),
    /**
   * Значение
   */
    value: PropTypes.oneOfType([PropTypes.array, PropTypes.object]),
    /**
   * Callback на переключение
   */
    onToggle: PropTypes.func,
    onInput: PropTypes.func,
    /**
   * Callback на изменение
   */
    onChange: PropTypes.func,
    /**
   * Callback на выбор
   */
    onSelect: PropTypes.func,
    /**
   * Callback на скрол в самый низ
   */
    onScrollEnd: PropTypes.func,
    /**
   * Placeholder контрола
   */
    placeholder: PropTypes.string,
    /**
   * Фича, при которой сбрасывается значение контрола, если оно не выбрано из popup
   */
    resetOnBlur: PropTypes.bool,
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
   * Поле для группировки
   */
    groupFieldId: PropTypes.string,
    /**
   * Флаг закрытия попапа при выборе
   */
    closePopupOnSelect: PropTypes.bool,
    /**
   * Флаг наличия чекбоксов в селекте
   */
    hasCheckboxes: PropTypes.bool,
    /**
   * Формат
   */
    format: PropTypes.string,
    /**
   * Callback на поиск
   */
    onSearch: PropTypes.func,
    expandPopUp: PropTypes.bool,
    alerts: PropTypes.array,
    /**
   * Авто фокусировка на селекте
   */
    autoFocus: PropTypes.bool,
    /**
   * Флаг авто размера попапа
   */
    popupAutoSize: PropTypes.bool,
}

InputSelect.defaultProps = {
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
    expandPopUp: false,
    autoFocus: false,
    popupAutoSize: false,
    onSearch() {},
    onSelect() {},
    onToggle() {},
    onInput() {},
    onOpen() {},
    onClose() {},
    onChange() {},
    onScrollEnd() {},
    onBlur() {},
}

export { InputSelect }
export default compose(
    setDisplayName('InputSelect'),
    onClickOutside,
)(InputSelect)

import React from 'react'
import { compose, setDisplayName } from 'recompose'
import PropTypes from 'prop-types'
import onClickOutside from 'react-onclickoutside'
import classNames from 'classnames'
import { Dropdown, DropdownToggle, DropdownMenu } from 'reactstrap'
import find from 'lodash/find'
import isEqual from 'lodash/isEqual'
import isEmpty from 'lodash/isEmpty'

import Alert from '../../snippets/Alerts/Alert'

import InputSelectGroup from './InputSelectGroup'
import PopupList from './PopupList'
import InputContent from './InputContent'
import { getValueArray } from './utils'

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

        const valueArray = getValueArray(value)
        const input = value && !multiSelect ? value[labelFieldId] : ''

        this.state = {
            inputFocus: props.autoFocus || false,
            isExpanded: false,
            isInputSelected: false,
            value: valueArray,
            activeValueId: null,
            isPopupFocused: false,
            options,
            input,
        }
    }

    // eslint-disable-next-line react/no-deprecated
    componentWillReceiveProps(nextProps) {
        const state = {}
        const { multiSelect, value, labelFieldId, options } = nextProps
        const { value: propsValue } = this.props
        const { options: stateOptions } = this.props

        if (!isEqual(nextProps.options, stateOptions)) {
            state.options = options
        }
        if (!isEqual(nextProps.value, propsValue)) {
            const valueArray = getValueArray(value)
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
    setActiveValueId = (activeValueId) => {
        this.setState({ activeValueId })
    }

    /**
     * обработка изменения значения при потери фокуса(считаем, что при потере фокуса пользователь закончил вводить новое значение)
     * @private
     */
    handleValueChangeOnBlur = () => {
        const { value, input, isPopupFocused } = this.state
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

        if (input && isEmpty(findValue) && resetOnBlur && !isPopupFocused) {
            this.setState(
                {
                    input: multiSelect ? '' : (value[0] && value[0][labelFieldId]) || '',
                    value,
                },
                () => onChange(this.getValue()),
            )
        }

        if (!input && value.length) {
            onChange(this.getValue())
        }

        if (conditionForAddingAnObject(resetOnBlur, input, options, value)) {
            this.addObjectToValue()
        }
    }

    /**
     * Обработка клика на инпут
     * @private
     */
    handleClick = () => {
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
    handleValueChangeOnSelect = (item) => {
        const { value } = this.state
        const { onChange, multiSelect, labelFieldId } = this.props

        this.setState(
            {
                input: multiSelect ? item[labelFieldId] : '',
                value: multiSelect ? [...value, item] : [item],
            },
            () => {
                onChange(this.getValue())
            },
        )
    }

    /**
     * Возвращает текущее значение (массив - если ипут селект, объект - если нет)
     * или null если пусто
     * @returns {*}
     * @private
     */

    getValue = () => {
        const { multiSelect } = this.props
        const { value } = this.state

        if (!value) {
            return null
        }

        if (multiSelect) {
            return value
        }

        return value[0]
    }

    /**
     * Удаляет элемент из списка выбранных
     * @param item - элемент
     * @private
     */

    removeSelectedItem = (item) => {
        const { onChange } = this.props
        const { value: stateValue } = this.state
        const value = stateValue.filter(i => i.id !== item.id)

        this.setState({ value }, () => onChange(value))
    }

    /**
     * Скрывает popUp
     * @private
     */

    hidePopUp = () => {
        this.setInputFocus(false)
        this.setState({ isExpanded: false })
    }

    /**
     * Очищает выбранный элемент
     * @private
     */

    clearSelected = () => {
        const { onChange, onBlur } = this.props

        this.setState({ value: [], input: '' }, () => {
            onChange(this.getValue())
            onBlur(this.getValue())
        })
    }

    /**
     * установить / сбросить фокус
     * @param inputFocus
     * @private
     */
    setInputFocus = (inputFocus) => {
        this.setState({ inputFocus })
    }

    /**
     * скрыть / показать попап
     * @param isExpanded
     * @private
     */
    setIsExpanded = (isExpanded) => {
        const { disabled, onToggle, onOpen } = this.props

        if (!isExpanded || disabled) {
            return null
        }

        this.setState({
            isExpanded,
            inputFocus: isExpanded,
        },
        onOpen)

        onToggle(isExpanded)

        return null
    }

    /**
     * выделить текст
     * @param isInputSelected
     * @private
     */
    setSelected = (isInputSelected) => {
        this.setState({ isInputSelected })
    }

    /**
     * Выполняет поиск элементов для popUp, если установлен фильтр
     * @private
     */

    handleDataSearch = (input, delay = 400, callback) => {
        const { onSearch, filter, labelFieldId, options } = this.props
        const { value } = this.state

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
     * новое значение инпута search)
     * @param input
     * @private
     */
    setNewInputValue = (input) => {
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
     * Обрабатывает выбор элемента из popUp
     * @param item - элемент массива options
     * @private
     */

    handleItemSelect = (item) => {
        const {
            multiSelect,
            closePopupOnSelect,
            labelFieldId,
            options,
            onSelect,
            onChange,
            onBlur,
        } = this.props

        const selectCallback = () => {
            if (closePopupOnSelect) {
                this.hidePopUp()
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
            () => {
                selectCallback()
                onBlur(this.getValue())

                if (this.inputRef) {
                    this.inputRef.focus()
                }
            },
        )
    }

    /**
     * Очищает инпут и результаты поиска
     * @private
     */

    clearSearchField = () => {
        this.setState({ input: '' }, this.handleDataSearch)
    }

    /**
     * Очищениеб сброс фокуса, выделенного значения
     * @private
     */
    handleElementClear = () => {
        const { disabled } = this.props

        if (!disabled) {
            this.clearSearchField()
            this.clearSelected()
            this.setInputFocus(false)
        }
    }

    /**
     * Обрабатывает клик за пределы компонента
     * вызывается библиотекой react-onclickoutside
     */
    handleClickOutside = () => {
        const { resetOnBlur, onBlur } = this.props
        const { isExpanded } = this.state

        if (isExpanded) {
            this.hidePopUp()

            if (resetOnBlur) {
                this.handleValueChangeOnBlur()
            }

            onBlur(this.getValue())
        }
    }

    /**
     * Добавлет объект к текущему value, при resetOnBlur = false
     * @private
     */

    addObjectToValue = () => {
        const { multiSelect, labelFieldId } = this.props
        const {
            input: userInput,
            value: currentValue,
            options,
        } = this.state

        if (Array.isArray(options) && multiSelect) {
            if (options.length === 0) {
                this.setState({
                    value: [...currentValue, { [labelFieldId]: userInput }],
                    input: '',
                })
            }
        } else {
            this.setState({
                value: [{ [labelFieldId]: userInput }],
            })
        }
    }

    handlePopupListMouseEnter = () => {
        this.setState({
            isPopupFocused: true,
        })
    }

    handlePopupListMouseLeave = () => {
        this.setState({
            isPopupFocused: false,
        })
    }

    onInputBlur = () => {
        const { onBlur } = this.props
        const { isExpanded, value, isPopupFocused } = this.state

        if (!isExpanded) {
            onBlur(this.getValue())
        }

        this.handleValueChangeOnBlur()
        this.setInputFocus(false)

        if (isEmpty(value) && !isPopupFocused) {
            this.setNewInputValue('')
        }
    }

    onFocus = () => {
        const { openOnFocus } = this.props

        if (openOnFocus) {
            this.setIsExpanded(true)
        }
    }

    setInputRef = popperRef => (r) => {
        this.inputRef = r
        popperRef(r)
    }

    toggle = () => {
        const { isExpanded } = this.state

        this.setIsExpanded(!isExpanded)
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
            maxTagTextLength,
            onDismiss,
        } = this.props
        const {
            value: stateValue,
            input,
            isExpanded,
            inputFocus,
            activeValueId,
            options,
            isInputSelected,
        } = this.state

        const inputSelectStyle = { width: '100%', cursor: 'text', ...style }
        const needAddFilter = !find(stateValue, item => item[labelFieldId] === input)

        return (
            <div
                style={inputSelectStyle}
                className={classNames('n2o-input-select n2o-input-select--default', {
                    disabled,
                })}
            >
                <Dropdown
                    isOpen={isExpanded}
                    toggle={this.toggle}
                >
                    <DropdownToggle tag="div" className="n2o-input-select__toggle">
                        <InputSelectGroup
                            isExpanded={isExpanded}
                            hidePopUp={this.hidePopUp}
                            loading={loading}
                            selected={stateValue}
                            input={input}
                            iconFieldId={iconFieldId}
                            imageFieldId={imageFieldId}
                            multiSelect={multiSelect}
                            inputFocus={inputFocus}
                            onClearClick={this.handleElementClear}
                            disabled={disabled}
                            className={`${className} ${isExpanded ? 'focus' : ''}`}
                        >
                            <InputContent
                                setRef={this.setInputRef}
                                onFocus={this.onFocus}
                                onBlur={this.onInputBlur}
                                loading={loading}
                                value={input}
                                disabled={disabled}
                                disabledValues={disabledValues}
                                valueFieldId={valueFieldId}
                                placeholder={placeholder}
                                options={options}
                                openPopUp={this.setIsExpanded}
                                closePopUp={this.setIsExpanded}
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
                                autoFocus={autoFocus}
                                maxTagTextLength={maxTagTextLength}
                            />
                        </InputSelectGroup>
                    </DropdownToggle>

                    <DropdownMenu
                        className={classNames('n2o-input-select__menu', {
                            'n2o-input-select__menu--autosize': popupAutoSize,
                        })}
                    >
                        <PopupList
                            handleMouseEnter={this.handlePopupListMouseEnter}
                            handleMouseLeave={this.handlePopupListMouseLeave}
                            scheduleUpdate={() => {}}
                            loading={loading}
                            isExpanded={isExpanded}
                            activeValueId={activeValueId}
                            setActiveValueId={this.setActiveValueId}
                            onScrollEnd={onScrollEnd}
                            filterValue={{
                                [labelFieldId]: input,
                            }}
                            needAddFilter={needAddFilter}
                            options={options}
                            valueFieldId={valueFieldId}
                            labelFieldId={labelFieldId}
                            iconFieldId={iconFieldId}
                            imageFieldId={imageFieldId}
                            badgeFieldId={badgeFieldId}
                            statusFieldId={statusFieldId}
                            descriptionFieldId={descriptionFieldId}
                            badgeColorFieldId={badgeColorFieldId}
                            onSelect={(item) => {
                                this.handleItemSelect(item)
                            }}
                            selected={stateValue}
                            disabledValues={disabledValues}
                            groupFieldId={groupFieldId}
                            enabledFieldId={enabledFieldId}
                            hasCheckboxes={hasCheckboxes}
                            onRemoveItem={this.removeSelectedItem}
                            format={format}
                        >
                            <div className="n2o-alerts">
                                {alerts &&
                  alerts.map(alert => (
                      <Alert
                          key={alert.id}
                          onDismiss={() => onDismiss(alert.id)}
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
    valueFieldId: PropTypes.string,
    /**
     * Ключ label в данных
     */
    labelFieldId: PropTypes.string,
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
    /**
     * Максимальная длина текста в тэге, до усечения
     */
    maxTagTextLength: PropTypes.number,
    isExpanded: PropTypes.bool,
    openOnFocus: PropTypes.bool,
    onBlur: PropTypes.func,
    className: PropTypes.string,
    descriptionFieldId: PropTypes.string,
    statusFieldId: PropTypes.string,
    enabledFieldId: PropTypes.string,
    onDismiss: PropTypes.func,
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

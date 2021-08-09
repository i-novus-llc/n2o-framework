import React from 'react'
import PropTypes from 'prop-types'
import { compose, setDisplayName } from 'recompose'
import onClickOutside from 'react-onclickoutside'
import isEqual from 'lodash/isEqual'
import get from 'lodash/get'
import first from 'lodash/first'
import isEmpty from 'lodash/isEmpty'
import find from 'lodash/find'
import filter from 'lodash/filter'
import Button from 'reactstrap/lib/Button'

import Popup from '../InputSelect/Popup'
import PopupList from '../InputSelect/PopupList'
import InputSelectGroup from '../InputSelect/InputSelectGroup'
import declensionNoun from '../../../utils/declensionNoun'

import N2OSelectInput from './N2OSelectInput'

/**
 * N2OSelect
 * @reactProps {boolean} loading - флаг анимации загрузки
 * @reactProps {array} options - данные
 * @reactProps {string} valueFieldId - значение ключа value в данных
 * @reactProps {string} labelFieldId - значение ключа label в данных
 * @reactProps {string} iconFieldId - поле для иконки
 * @reactProps {string} imageFieldId - поле для картинки
 * @reactProps {string} statusFieldId - поле для статуса
 * @reactProps {string} badgeFieldId - поле для баджей
 * @reactProps {string} badgeColorFieldId - поле для цвета баджа
 * @reactProps {boolean} disabled - флаг неактивности
 * @reactProps {array} disabledValues - неактивные данные
 * @reactProps {string} filter - варианты фильтрации
 * @reactProps {string} value - текущее значение
 * @reactProps {function} onInput - callback при вводе в инпут
 * @reactProps {function} onChange - callback при выборе значения или вводе
 * @reactProps {function} onScrollEnd - callback при прокрутке скролла popup
 * @reactProps {string} placeHolder - подсказка в инпуте
 * @reactProps {boolean} resetOnBlur - фича, при которой сбрасывается значение контрола, если оно не выбрано из popup
 * @reactProps {function} onOpen - callback на открытие попапа
 * @reactProps {function} onClose - callback на закрытие попапа
 * @reactProps {string} groupFieldId - поле для группировки
 * @reactProps {boolean} closePopupOnSelect - флаг закрытия попапа при выборе
 * @reactProps {boolean} hasCheckboxes - флаг наличия чекбоксов
 * @reactProps {string} format - формат
 * @reactProps {boolean} searchByTap - поиск по нажатию кнопки
 */

const selectType = {
    SINGLE: 'single',
    CHECKBOXES: 'checkboxes',
}

class N2OSelect extends React.Component {
    constructor(props) {
        super(props)
        const { value, options, type } = this.props

        this.state = {
            value: '',
            isExpanded: false,
            options,
            selected: this.getSelected(value),
            hasCheckboxes: type === selectType.CHECKBOXES,
        }

        this.control = null

        this.handleButtonClick = this.handleButtonClick.bind(this)
        this.handleInputChange = this.handleInputChange.bind(this)
        this.handleInputFocus = this.handleInputFocus.bind(this)
        this.hideOptionsList = this.hideOptionsList.bind(this)
        this.handleItemSelect = this.handleItemSelect.bind(this)
        this.removeSelectedItem = this.removeSelectedItem.bind(this)
        this.clearSelected = this.clearSelected.bind(this)
        this.handleSearchButton = this.handleSearchButton.bind(this)
        this.handleOnBlur = this.handleOnBlur.bind(this)
        this.setControlRef = this.setControlRef.bind(this)
    }

    componentDidMount() {
        const { initial, options, valueFieldId } = this.props

        if (Array.isArray(initial)) {
            this.setStateFromInitial(initial, options, valueFieldId)
        }
    }

    // eslint-disable-next-line react/no-deprecated
    componentWillReceiveProps(nextProps) {
        const { options } = this.props
        const state = {}

        if (!isEqual(options, nextProps.options)) {
            state.options = nextProps.options
        }
        this.setState(state)
    }

    componentDidUpdate(prevProps) {
        const { initial, options, valueFieldId, value } = this.props

        if (Array.isArray(initial) && !isEqual(initial, prevProps.initial)) {
            this.setStateFromInitial(initial, options, valueFieldId)

            return
        }
        if (!isEqual(value, prevProps.value)) {
            this.setState({
                selected: this.getSelected(value),
            })
        }
    }

    /**
     * Хак для мапинга айдишников, которые берутся из адресной строки в виде строк, но ожидается число
     * TODO удалить после того, как починится поведение парсинга адресной строки будет опираться на указанные типы
     * @param {Array.<object>} [initial]
     * @param options
     * @param {String} valueFieldId
     * @private
     */
    setStateFromInitial(initial, options, valueFieldId) {
        const mapOptions = (data, type = 'string') => data.map(option => ({
            ...option,
            ...{
                [valueFieldId]:
            type === 'number'
                ? Number(option[valueFieldId])
                : String(option[valueFieldId]),
            },
        }))

        if (isEmpty(options)) {
            this.setState({
                selected: mapOptions(initial),
            })
        } else {
            const selected = filter(
                options,
                (option) => {
                    const idType = typeof option[valueFieldId]

                    return find(mapOptions(initial, idType), option)
                },
                [],
            )

            this.setState({
                selected,
            })
        }
    }

    /**
     * @param {Array | string} [value]
     * @return {Array}
     * @private
     */
    getSelected = (value) => {
        if (Array.isArray(value)) {
            return value
        }
        if (value) {
            return [value]
        }

        return []
    }

    /**
     * Удаляет элемент из списка выбранных
     * @param item - элемент
     * @private
     */
    removeSelectedItem(item) {
        const { valueFieldId, onChange } = this.props
        const { selected: stateSelected } = this.state
        const selected = stateSelected.filter(
            i => i[valueFieldId] !== item[valueFieldId],
        )

        this.setState({
            selected,
        })
        if (onChange) {
            onChange(selected)
        }
    }

    /**
     * Изменение видимости попапа
     * @param newState - новое значение видимости
     * @private
     */
    changePopUpVision(newIsExpanded) {
        const { onOpen, onClose } = this.props
        const { isExpanded } = this.state

        if (isExpanded === newIsExpanded) { return }
        this.setState(
            {
                isExpanded: newIsExpanded,
            },
            newIsExpanded ? onOpen : onClose,
        )
    }

    /**
     * Обрабатывает нажатие на кнопку
     * @private
     */
    handleButtonClick() {
        const { disabled } = this.props
        const { isExpanded } = this.state

        if (!disabled) {
            this.changePopUpVision(!isExpanded)
        }
    }

    /**
     * Обрабатывает форкус на инпуте
     * @private
     */
    handleInputFocus() {
        this.changePopUpVision(true)
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
     * Удаляет выбранные элементы
     * @private
     */
    clearSelected(e) {
        e.stopPropagation()
        e.preventDefault()

        const { disabled, onChange, onBlur } = this.props

        if (disabled) {
            return
        }

        this.setState({
            selected: [],
        })
        onChange(null)
        onBlur(null)
    }

    /**
     * Выполняет поиск элементов для popUp, если установлен фильтр
     * @param newValue - значение для поиска
     * @private
     */
    handleDataSearch(input, delay = true, callback) {
        const { onSearch, filter, options: data, labelFieldId } = this.props

        if (filter) {
            const filterFunc = item => String.prototype[filter].call(item, input)
            const options = data.filter(item => filterFunc(item[labelFieldId].toString()))

            this.setState({ options })
        } else {
            onSearch(input, delay, callback)
        }
    }

    /**
     * Устанавливает выбранный элемент
     * @param item - элемент массива options
     * @private
     */
    insertSelected(item) {
        const { onChange, onBlur } = this.props
        const { selected: stateSelected, hasCheckboxes } = this.state
        let selected = [item]
        let value = item

        if (hasCheckboxes) {
            selected = [...stateSelected, item]
            value = selected
        }

        this.setState({
            selected,
        })

        if (onChange) {
            onChange(value)
            onBlur(value)
        }
    }

    /**
     * Обрабатывает изменение инпута
     * @param newValue - новое значение
     * @private
     */
    handleInputChange(newValue) {
        const { searchByTap, onChange, onInput, resetOnBlur } = this.props

        this.setNewValue(newValue)

        if (!searchByTap) {
            this.handleDataSearch(newValue)
        }
        if (!resetOnBlur) {
            onChange(newValue)
        }
        onInput(newValue)
    }

    /**
     * Обрабатывает поиск по нажатию
     * @private
     */
    handleSearchButton() {
        const { value } = this.state

        this.handleDataSearch(value)
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
     * Обрабатывает выбор элемента из popUp
     * @param item - элемент массива options
     * @private
     */
    handleItemSelect(item) {
        const { closePopupOnSelect } = this.props

        this.insertSelected(item)

        if (closePopupOnSelect) {
            this.hideOptionsList()
        }

        this.clearSearchField()

        if (this.control) {
            this.control.focus()
        }
    }

    /**
     * Обрабатывает поведение инпута при потери фокуса, если есть resetOnBlur
     * @private
     */
    handleResetOnBlur() {
        const { selected } = this.state
        const { resetOnBlur, options } = this.props

        if (resetOnBlur && !selected) {
            this.setState({
                value: '',
                options,
            })
        }
    }

    /**
     * Обрабатывает клик за пределы компонента
     * вызывается библиотекой react-onclickoutside
     */
    handleClickOutside() {
        this.hideOptionsList()
        this.handleResetOnBlur()
    }

    handleOnBlur(e) {
        e.preventDefault()
        this.handleResetOnBlur()
    }

    setControlRef(el) {
        this.control = el
    }

    renderPlaceholder() {
        const {
            selectFormat = 'Объектов {size} шт',
            selectFormatOne = '',
            selectFormatFew = '',
            selectFormatMany = '',
        } = this.props

        const { selected, hasCheckboxes } = this.state
        const selectedCount = selected.length
        let text

        if (
            !isEmpty(selectFormatOne) &&
            !isEmpty(selectFormatFew) &&
            !isEmpty(selectFormatMany) &&
            selectedCount >= 1 &&
            hasCheckboxes
        ) {
            text = declensionNoun(
                selectedCount,
                selectFormatOne,
                selectFormatFew,
                selectFormatMany,
            ).replace('{size}', selectedCount)
        } else if (selectedCount >= 1 && hasCheckboxes) {
            text = selectFormat.replace('{size}', selectedCount)
        } else {
            text = null
        }

        return text
    }

    render() {
        const {
            loading,
            className,
            valueFieldId,
            labelFieldId,
            iconFieldId,
            disabled,
            disabledValues,
            imageFieldId,
            statusFieldId,
            groupFieldId,
            descriptionFieldId,
            format,
            placeholder,
            badgeFieldId,
            badgeColorFieldId,
            onScrollEnd,
            hasSearch,
            cleanable,
            style,
        } = this.props
        const inputSelectStyle = { width: '100%', ...style }

        const { selected, isExpanded, hasCheckboxes, value, options } = this.state

        const title = get(first(selected), `${labelFieldId}`)

        return (
            <div
                className="n2o-input-select"
                title={title}
                style={inputSelectStyle}
                onBlur={this.handleOnBlur}
            >
                <Button innerRef={this.setControlRef} onClick={this.handleButtonClick}>
                    <InputSelectGroup
                        className={className}
                        isExpanded={isExpanded}
                        loading={loading}
                        disabled={disabled}
                        iconFieldId={iconFieldId}
                        imageFieldId={imageFieldId}
                        cleanable={cleanable}
                        selected={selected}
                        onClearClick={this.clearSelected}
                    >
                        <span className="valueText">
                            {
                                hasCheckboxes
                                    ? this.renderPlaceholder()
                                    : !isEmpty(selected) && selected[0][labelFieldId]
                            }
                        </span>
                    </InputSelectGroup>
                </Button>
                <Popup isExpanded={isExpanded}>
                    <>
                        {hasSearch && (
                            <N2OSelectInput
                                placeholder={placeholder}
                                onChange={this.handleInputChange}
                                onSearch={this.handleSearchButton}
                                value={value}
                            />
                        )}
                        <PopupList
                            options={options}
                            valueFieldId={valueFieldId}
                            labelFieldId={labelFieldId}
                            iconFieldId={iconFieldId}
                            imageFieldId={imageFieldId}
                            statusFieldId={statusFieldId}
                            badgeFieldId={badgeFieldId}
                            descriptionFieldId={descriptionFieldId}
                            badgeColorFieldId={badgeColorFieldId}
                            onSelect={this.handleItemSelect}
                            onScrollEnd={onScrollEnd}
                            isExpanded={isExpanded}
                            selected={selected}
                            disabledValues={disabledValues}
                            groupFieldId={groupFieldId}
                            hasCheckboxes={hasCheckboxes}
                            onRemoveItem={this.removeSelectedItem}
                            format={format}
                            loading={loading}
                        />
                    </>
                </Popup>
            </div>
        )
    }
}

N2OSelect.propTypes = {
    /**
     * Флаг загрузки
     */
    loading: PropTypes.bool,
    /**
     * Данные
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
    cleanable: PropTypes.bool,
    /**
     * Ключ icon в данных
     */
    iconFieldId: PropTypes.string,
    /**
     * Ключ image в данных
     */
    imageFieldId: PropTypes.string,
    /**
     * Ключ image в данных
     */
    statusFieldId: PropTypes.string,
    /**
     * Ключ badge в данных
     */
    badgeFieldId: PropTypes.string,
    /**
     * Ключ badgeColor в данных
     */
    badgeColorFieldId: PropTypes.string,
    /**
     * Флаг активности
     */
    disabled: PropTypes.bool,
    /**
     * Неактивные данные
     */
    disabledValues: PropTypes.array,
    /**
     * Фильтрация
     */
    filter: PropTypes.oneOf(['includes', 'startsWith', 'endsWith', false]),
    /**
     * Значение
     */
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    initial: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    /**
     * Callback при вводе в инпут
     */
    onInput: PropTypes.func,
    /**
     * Callback на изменение
     */
    onChange: PropTypes.func,
    /**
     * Callback на изменение
     */
    onScrollEnd: PropTypes.func,
    /**
     * Placeholder контрола
     */
    placeholder: PropTypes.string,
    /**
     * Сброс значения при потере фокуса
     */
    resetOnBlur: PropTypes.bool,
    /**
     * Callback на открытие попапа
     */
    onOpen: PropTypes.func,
    /**
     * Callback на закрытие попапа
     */
    onClose: PropTypes.func,
    groupFieldId: PropTypes.string,
    /**
     * Формат
     */
    format: PropTypes.string,
    /**
     * Поиск по нажатию кнопки
     */
    searchByTap: PropTypes.bool,
    /**
     * Callback на поиск
     */
    onSearch: PropTypes.func,
    /**
     * Флаг наличия поиска
     */
    hasSearch: PropTypes.bool,
    type: PropTypes.string,
    closePopupOnSelect: PropTypes.bool,
    onBlur: PropTypes.func,
    descriptionFieldId: PropTypes.string,
    selectFormat: PropTypes.string,
    selectFormatOne: PropTypes.string,
    selectFormatFew: PropTypes.string,
    selectFormatMany: PropTypes.string,
    className: PropTypes.string,
    style: PropTypes.object,
}

N2OSelect.defaultProps = {
    cleanable: true,
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
    searchByTap: false,
    hasSearch: false,
    onSearch() {},
    onChange() {},
    onScrollEnd() {},
    onInput() {},
    onOpen() {},
    onClose() {},
    onBlur() {},
}

export { N2OSelect }
export default compose(
    setDisplayName('N2OSelect'),
    onClickOutside,
)(N2OSelect)

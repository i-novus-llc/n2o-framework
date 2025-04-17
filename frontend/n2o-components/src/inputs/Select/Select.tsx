import React, { FocusEvent, RefObject, createRef, MouseEvent } from 'react'
import onClickOutside from 'react-onclickoutside'
import isEqual from 'lodash/isEqual'
import get from 'lodash/get'
import first from 'lodash/first'
import isEmpty from 'lodash/isEmpty'
import find from 'lodash/find'
import { Button } from 'reactstrap'

import { InputSelectGroup } from '../InputSelect/InputSelectGroup'
import { TOption } from '../InputSelect/types'
import { PopupList } from '../InputSelect/PopupList'
import { WithPopUpHeight } from '../WithPopUpHeight'
import { InputContent } from '../InputSelect/InputContent'

import { getNoun } from './utils'
import { Popup } from './Popup'
import { N2OSelectInput } from './SelectInput'
import { State, Props } from './types'

/**
 * N2OSelect
 * @reactProps {boolean} loading - флаг анимации загрузки
 * @reactProps {array} options - данные
 * @reactProps {string} valueFieldId - значение ключа value в данных
 * @reactProps {string} labelFieldId - значение ключа label в данных
 * @reactProps {string} iconFieldId - поле для иконки
 * @reactProps {string} imageFieldId - поле для картинки
 * @reactProps {string} statusFieldId - поле для статуса
 * @reactProps {object} badge - данные для баджа
 * @reactProps {boolean} disabled - флаг неактивности
 * @reactProps {string} enabledFieldId - поле для активности
 * @reactProps {string} filter - варианты фильтрации
 * @reactProps {string} value - текущее значение
 * @reactProps {function} onInput - callback при вводе в инпут
 * @reactProps {function} onChange - callback при выборе значения или вводе
 * @reactProps {string} placeHolder - подсказка в инпуте
 * @reactProps {boolean} resetOnBlur - фича, при которой сбрасывается значение контрола, если оно не выбрано из popup
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

function getSelected(value: Props['value']) {
    if (Array.isArray(value)) { return value }
    if (!isEmpty(value)) { return [value] }

    return []
}

class SelectComponent extends React.Component<Props, State> {
    private control: null | HTMLButtonElement = null

    readonly hasCheckboxes: boolean

    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    n2oSelectRef: RefObject<any>

    constructor(props: Props) {
        super(props)
        const { value, options, type } = this.props

        this.state = {
            value: '',
            isExpanded: false,
            options,
            selected: getSelected(value),
            activeValueId: null,
        }

        this.control = null
        this.n2oSelectRef = createRef()
        this.hasCheckboxes = type === selectType.CHECKBOXES
    }

    componentDidMount() {
        const { initial, options, valueFieldId } = this.props

        if (Array.isArray(initial)) {
            this.setStateFromInitial(initial, options, valueFieldId)
        }
    }

    // eslint-disable-next-line react/no-deprecated
    componentWillReceiveProps(nextProps: Props) {
        const { options } = this.props
        const state = {} as State

        if (!isEqual(options, nextProps.options)) {
            state.options = nextProps.options
        }
        this.setState(state)
    }

    componentDidUpdate(prevProps: Props) {
        const { initial, options, valueFieldId, value } = this.props

        if (Array.isArray(initial) && !isEqual(initial, prevProps.initial)) {
            this.setStateFromInitial(initial, options, valueFieldId)

            return
        }
        if (!isEqual(value, prevProps.value)) {
            this.setState({
                selected: getSelected(value),
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
    setStateFromInitial(initial: Props['options'], options: Props['options'], valueFieldId: Props['valueFieldId']) {
        const mapOptions = (options: Props['options'], type = 'string') => options.map(option => ({
            ...option,
            ...{
                [valueFieldId]:
                    type === 'number'
                        ? Number(option[valueFieldId as keyof TOption])
                        : String(option[valueFieldId as keyof TOption]),
            },
        }))

        if (isEmpty(options)) {
            this.setState({
                selected: mapOptions(initial),
            })
        } else {
            const selected = options.filter((option) => {
                const idType = typeof option[valueFieldId as keyof TOption]

                return find(mapOptions(initial, idType), option)
            })

            this.setState({ selected })
        }
    }

    /**
     * Удаляет элемент из списка выбранных
     * @param item - элемент
     * @private
     */
    removeSelectedItem = (item: TOption) => {
        const { valueFieldId, onChange } = this.props
        const { selected: stateSelected } = this.state
        const selected = typeof stateSelected !== 'string' ? stateSelected.filter(
            i => i[valueFieldId] !== item[valueFieldId as keyof TOption],
        ) : stateSelected

        this.setState({ selected })

        if (onChange) { onChange(selected) }
    }

    /**
     * Изменение видимости попапа
     * @param newIsExpanded - новое значение видимости
     * @private
     */
    changePopUpVision(newIsExpanded: State['isExpanded']) {
        const { fetchData, onClose } = this.props
        const { isExpanded } = this.state

        if (isExpanded === newIsExpanded) { return }

        this.setState(
            { isExpanded: newIsExpanded },
            newIsExpanded ? fetchData({ page: 1 }) : onClose,
        )
    }

    /**
     * Обрабатывает нажатие на кнопку
     * @private
     */
    handleButtonClick = () => {
        const { disabled } = this.props
        const { isExpanded } = this.state

        if (!disabled) { this.changePopUpVision(!isExpanded) }
    }

    /**
     * Обрабатывает форкус на инпуте
     * @private
     */
    handleInputFocus = () => this.changePopUpVision(true)

    /**
     * Скрывает popUp
     * @private
     */
    hideOptionsList = () => this.changePopUpVision(false)

    /**
     * Уставнавливает новое значение инпута
     * @param newValue - новое значение
     * @private
     */
    setNewValue(newValue: State['value']) {
        this.setState({ value: newValue })
    }

    /**
     * Удаляет выбранные элементы
     * @private
     */
    clearSelected = (e: MouseEvent<HTMLElement>) => {
        e.stopPropagation()
        e.preventDefault()

        const { disabled, onChange, onBlur } = this.props

        if (disabled) { return }

        this.setState({
            selected: [],
        })
        onChange(null)
        onBlur(null)
    }

    /**
     * Выполняет поиск элементов для popUp, если установлен фильтр
     * @param input - значение для поиска
     * @param {boolean} delay
     * @param {Function} callback
     * @private
     */
    handleDataSearch: Props['onSearch'] = (input, delay, callback) => {
        const { onSearch, filter, options, labelFieldId } = this.props

        if (filter) {
            // eslint-disable-next-line @typescript-eslint/no-explicit-any
            const filterFunc = (item: TOption) => (String.prototype[filter as any] as any).call(item, input)
            const filteredOptions = options.filter(item => filterFunc(item[labelFieldId as keyof TOption].toString()))

            this.setState({ options: filteredOptions })
        } else {
            onSearch(input, delay === false ? delay : true, callback)
        }
    }

    /**
     * Устанавливает выбранный элемент
     * @param item - элемент массива options
     * @private
     */
    insertSelected(item: TOption) {
        const { onChange, onBlur } = this.props
        const { selected: stateSelected } = this.state
        let selected = [item]
        let value: TOption | TOption[] = item

        if (this.hasCheckboxes) {
            selected = [...(stateSelected || []), item]
            value = selected
        }

        this.setState({ selected })

        if (onChange) {
            onChange(value)
            // onBlur(value)
        }
    }

    /**
     * Обрабатывает изменение инпута
     * @param newValue - новое значение
     * @private
     */
    handleInputChange = (newValue: string) => {
        const { searchByTap, onChange, onInput, resetOnBlur } = this.props

        this.setNewValue(newValue)

        if (!searchByTap) { this.handleDataSearch(newValue) }
        if (!resetOnBlur) { onChange(newValue) }
        onInput(newValue)
    }

    /**
     * Обрабатывает поиск по нажатию
     * @private
     */
    handleSearchButton = () => {
        const { value } = this.state

        this.handleDataSearch(value)
    }

    /**
     * Очищает инпут и результаты поиска
     * @private
     */
    clearSearchField() {
        const { options } = this.props

        this.setState({ value: '', options })
    }

    /**
     * Обрабатывает выбор элемента из popUp
     * @param item - элемент массива options
     * @private
     */
    handleItemSelect = (item: TOption) => {
        const { closePopupOnSelect } = this.props

        this.insertSelected(item)

        if (closePopupOnSelect) { this.hideOptionsList() }

        this.clearSearchField()

        if (this.control) {
            // eslint-disable-next-line @typescript-eslint/no-explicit-any
            (this.control as any).focus()
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
            this.setState({ value: '', options })
        }
    }

    /**
     * Обрабатывает клик за пределы компонента
     * вызывается библиотекой react-onclickoutside
     */
    handleClickOutside() {
        const { onBlur, value } = this.props
        const { isExpanded } = this.state

        if (isExpanded) {
            this.hideOptionsList()
            onBlur(value)
        }
    }

    handleOnBlur = (e: FocusEvent<HTMLDivElement>) => {
        e.preventDefault()
        this.handleResetOnBlur()
    }

    setControlRef = (el: HTMLButtonElement) => { this.control = el }

    getPlaceholder = () => {
        const { selected } = this.state
        const count = selected.length

        if (count < 1) { return '' }

        const {
            selectFormat = 'Объектов {size} шт',
            selectFormatOne = '',
            selectFormatFew = '',
            selectFormatMany = '',
        } = this.props

        if (
            !isEmpty(selectFormatOne) &&
            !isEmpty(selectFormatFew) &&
            !isEmpty(selectFormatMany)
        ) {
            return getNoun(
                count,
                selectFormatOne,
                selectFormatFew,
                selectFormatMany,
            ).replace('{size}', `${count}`)
        }

        return selectFormat.replace('{size}', `${count}`)
    }

    getValue = () => {
        const { labelFieldId, placeholder = '' } = this.props
        const { selected } = this.state

        if (isEmpty(selected)) { return placeholder }

        const [selectedElement] = selected

        return get(selectedElement, labelFieldId)
    }

    /**
     * установить акстивный элемент дропдауна
     * @param activeValueId
     * @private
     */
    setActiveValueId = (activeValueId: State['activeValueId']) => {
        this.setState({ activeValueId })
    }

    render() {
        const {
            loading,
            className,
            valueFieldId,
            labelFieldId,
            iconFieldId,
            disabled,
            enabledFieldId,
            imageFieldId,
            statusFieldId,
            groupFieldId,
            descriptionFieldId,
            format,
            placeholder,
            badge,
            fetchData,
            page,
            hasSearch,
            cleanable,
            style,
            onKeyDown,
            size,
            count,
            popUpItemRef,
            popUpStyle,
        } = this.props
        const inputSelectStyle = { width: '100%', ...style }

        const { selected, isExpanded, value, options, activeValueId } = this.state

        const title = get(first(selected), `${labelFieldId}`)
        const inputValue = this.hasCheckboxes ? this.getPlaceholder() : this.getValue()

        return (
            <div
                className="n2o-input-select readonly-select"
                title={title}
                style={inputSelectStyle}
                onBlur={this.handleOnBlur}
                ref={this.n2oSelectRef}
            >
                <Button innerRef={this.setControlRef} onClick={this.handleButtonClick} onKeyDown={onKeyDown}>
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
                        <InputContent
                            value={inputValue}
                            options={options}
                            selected={selected}
                            onRemoveItem={this.removeSelectedItem}
                            setActiveValueId={this.setActiveValueId}
                            activeValueId={activeValueId || ''}
                            valueFieldId={valueFieldId}
                            placeholder={placeholder}
                            onSelect={this.handleItemSelect}
                            labelFieldId={labelFieldId}
                            isExpanded={isExpanded}
                            className="valueText"
                            readOnly
                        />
                    </InputSelectGroup>
                </Button>
                <Popup isExpanded={isExpanded} inputSelect={this.n2oSelectRef.current}>
                    <>
                        {hasSearch && (
                            <N2OSelectInput
                                placeholder={placeholder || ''}
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
                            badge={badge}
                            descriptionFieldId={descriptionFieldId}
                            enabledFieldId={enabledFieldId}
                            onSelect={this.handleItemSelect}
                            fetchData={fetchData}
                            page={page}
                            isExpanded={isExpanded}
                            selected={selected}
                            groupFieldId={groupFieldId}
                            hasCheckboxes={this.hasCheckboxes}
                            onRemoveItem={this.removeSelectedItem}
                            format={format}
                            loading={loading}
                            size={size}
                            count={count}
                            popUpItemRef={popUpItemRef}
                            style={popUpStyle}
                            activeValueId={activeValueId}
                            setActiveValueId={this.setActiveValueId}
                        />
                    </>
                </Popup>
            </div>
        )
    }

    static defaultProps = {
        cleanable: true,
        valueFieldId: 'id',
        labelFieldId: 'name',
        iconFieldId: 'icon',
        loading: false,
        disabled: false,
        resetOnBlur: false,
        searchByTap: false,
        hasSearch: false,
        options: [],
        value: {},
        descriptionFieldId: '',
        enabledFieldId: '',
        statusFieldId: '',
        groupFieldId: '',
        imageFieldId: '',
        closePopupOnSelect: true,
        onSearch() {},
        onChange() {},
        fetchData: () => () => {},
        onInput() {},
        onClose() {},
        onBlur() {},
        onKeyDown() {},
        popUpItemRef: null,
        popUpStyle: {},
    } as Props
}

export const Select = WithPopUpHeight(onClickOutside(SelectComponent) as never)

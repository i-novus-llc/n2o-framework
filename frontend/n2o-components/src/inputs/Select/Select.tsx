import React, { FocusEvent, KeyboardEvent, RefObject, createRef } from 'react'
import onClickOutside from 'react-onclickoutside'
import isEqual from 'lodash/isEqual'
import get from 'lodash/get'
import first from 'lodash/first'
import isEmpty from 'lodash/isEmpty'
import find from 'lodash/find'
import { Button } from 'reactstrap'

import { InputSelectGroup } from '../InputSelect/InputSelectGroup'
import { Filter, TOption } from '../InputSelect/types'
import { BadgeType, PopupList } from '../InputSelect/PopupList'

import { getNoun } from './utils'
import { Popup } from './Popup'
import { N2OSelectInput } from './SelectInput'

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
 * @reactProps {array} disabledValues - неактивные данные
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

type State = {
    hasCheckboxes: boolean,
    input?: string
    isExpanded: boolean,
    options?: TOption[],
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    selected: any[]
    value: string
}

function getSelected(value: Props['value']) {
    if (Array.isArray(value)) {
        return value
    }
    if (!isEmpty(value)) {
        return [value]
    }

    return []
}

class SelectComponent extends React.Component<Props, State> {
    private control: null | HTMLButtonElement = null

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
            hasCheckboxes: type === selectType.CHECKBOXES,
        }

        this.control = null
        this.n2oSelectRef = createRef()

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

            this.setState({
                selected,
            })
        }
    }

    /**
     * Удаляет элемент из списка выбранных
     * @param item - элемент
     * @private
     */
    removeSelectedItem(item: TOption) {
        const { valueFieldId, onChange } = this.props
        const { selected: stateSelected } = this.state
        const selected = typeof stateSelected !== 'string' ? stateSelected.filter(
            i => i[valueFieldId] !== item[valueFieldId as keyof TOption],
        ) : stateSelected

        this.setState({
            selected,
        })
        if (onChange) {
            onChange(selected)
        }
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

        const onOpen = fetchData({ page: 1 })

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
    setNewValue(newValue: State['value']) {
        this.setState({
            value: newValue,
        })
    }

    /**
     * Удаляет выбранные элементы
     * @private
     */
    clearSelected(e: Event) {
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
        const { selected: stateSelected, hasCheckboxes } = this.state
        let selected = [item]
        let value: TOption | TOption[] = item

        if (hasCheckboxes) {
            selected = [...(stateSelected || []), item]
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
    handleInputChange(newValue: string) {
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
    handleItemSelect(item: TOption) {
        const { closePopupOnSelect } = this.props

        this.insertSelected(item)

        if (closePopupOnSelect) {
            this.hideOptionsList()
        }

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
        const { onBlur, value } = this.props
        const { isExpanded } = this.state

        if (isExpanded) {
            this.hideOptionsList()
            onBlur(value)
        }
    }

    handleOnBlur(e: FocusEvent<HTMLDivElement>) {
        e.preventDefault()
        this.handleResetOnBlur()
    }

    setControlRef(el: HTMLButtonElement) {
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
            text = getNoun(
                selectedCount,
                selectFormatOne,
                selectFormatFew,
                selectFormatMany,
            ).replace('{size}', `${selectedCount}`)
        } else if (selectedCount >= 1 && hasCheckboxes) {
            text = selectFormat.replace('{size}', `${selectedCount}`)
        } else {
            text = null
        }

        return text
    }

    renderValue() {
        const { labelFieldId, placeholder = '' } = this.props
        const { selected } = this.state

        if (isEmpty(selected)) {
            return placeholder
        }

        const [selectedElement] = selected

        return get(selectedElement, labelFieldId)
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
                        <span className="valueText">
                            {
                                hasCheckboxes
                                    ? this.renderPlaceholder()
                                    : this.renderValue()
                            }
                        </span>
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

    static defaultProps = {
        cleanable: true,
        valueFieldId: 'id',
        labelFieldId: 'name',
        iconFieldId: 'icon',
        loading: false,
        disabled: false,
        disabledValues: [],
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
    } as Props
}

type Props = {
    /**
     * Данные для badge
     */
    badge?: BadgeType,
    className?: string,
    cleanable: boolean,
    closePopupOnSelect: boolean,
    descriptionFieldId: string,
    /**
     * Флаг активности
     */
    disabled: boolean,
    /**
     * Неактивные данные
     */
    disabledValues: [],
    /**
     * Ключ enabled в данных
     */
    enabledFieldId: string,
    fetchData(arg: object): () => void,
    /**
     * Фильтрация
     */
    filter?: Filter,
    /**
     * Формат
     */
    format?: string,
    groupFieldId: string,
    /**
     * Флаг наличия поиска
     */
    hasSearch: boolean,
    /**
     * Ключ icon в данных
     */
    iconFieldId: string,
    /**
     * Ключ image в данных
     */
    imageFieldId: string,
    initial?: string | number,
    /**
     * Ключ label в данных
     */
    labelFieldId: string,
    /**
     * Флаг загрузки
     */
    loading: boolean,
    onBlur(arg: Props['value']): void,
    /**
     * Callback на изменение
     */
    onChange(arg: TOption | TOption[] | null | string): void,
    /**
     * Callback на закрытие попапа
     */
    onClose(): void,
    /**
     * Callback при вводе в инпут
     */
    onInput(input: string): void,
    onKeyDown?(evt: KeyboardEvent<HTMLButtonElement>): void,
    /**
     * Callback на поиск
     */
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    onSearch(input: State['input'], delay?: boolean, callback?: any): void,
    /**
     * Данные
     */
    options: TOption[],
    page?: number,
    /**
     * Placeholder контрола
     */
    placeholder?: string,
    /**
     * Сброс значения при потере фокуса
     */
    resetOnBlur: boolean,
    /**
     * Поиск по нажатию кнопки
     */
    searchByTap: boolean,
    selectFormat?: string,
    selectFormatFew?: string,
    selectFormatMany?: string,
    selectFormatOne?: string,
    /**
     * Ключ image в данных
     */
    statusFieldId: string,
    style?: object,
    type?: string,
    /**
     * Значение
     */
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    value: any,
    /**
     * Ключ id в данных
     */
    valueFieldId: string
}

export const Select = onClickOutside(SelectComponent)

import React, { KeyboardEvent, createRef } from 'react'
import classNames from 'classnames'
import { Dropdown, DropdownToggle, DropdownMenu } from 'reactstrap'
import onClickOutside from 'react-onclickoutside'
import find from 'lodash/find'
import isEqual from 'lodash/isEqual'
import isEmpty from 'lodash/isEmpty'
import omit from 'lodash/omit'

import { Alert } from '../../display/Alerts/Alert'
import { isEmptyModel } from '../../utils/isEmptyModel'

import { InputSelectGroup } from './InputSelectGroup'
import { PopupList, BadgeType } from './PopupList'
import { InputContent } from './InputContent'
import { getValueArray } from './utils'
import { DEFAULT_POPUP_HEIGHT, MEASURE } from './constants'
import { Filter, Ref, TOption } from './types'

const DEFAULT_DATA_SEARCH_DELAY = 400

/**
 * InputSelect
 * @reactProps {object} style - css стили
 * @reactProps {boolean} loading - флаг анимации загрузки
 * @reactProps {array} options - данные
 * @reactProps {string} valueFieldId - значение ключа value в данных
 * @reactProps {string} labelFieldId - значение ключа label в данных
 * @reactProps {string} iconFieldId - поле для иконки
 * @reactProps {string} imageFieldId - поле для картинки
 * @reactProps {object} badge - данные для баджа
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
 * @reactProps {boolean} resetOnBlur - фича, при которой: (значение - true) - сбрасывается значение контрола, если оно
 *     не выбрано из popup, (значение - false) - создает объект в текущем value
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
 * @reactProps {number} size - кол-во запрашиваемых записей
 * @reactProps {number} count - всего записей
 */

export class InputSelect extends React.Component<Props, State> {
    inputHeightRef: Ref

    popUpItemRef: Ref

    textAreaRef: Ref

    inputRef: Ref | undefined

    constructor(props: Props) {
        super(props)
        const { value, options, labelFieldId, multiSelect } = this.props

        const valueArray = getValueArray(value)
        const input = value && !multiSelect ? value[labelFieldId] : ''

        this.state = {
            inputFocus: props.autoFocus || false,
            isExpanded: false,
            value: valueArray,
            activeValueId: null,
            isPopupFocused: false,
            popUpMaxHeight: DEFAULT_POPUP_HEIGHT,
            options,
            input,
        }

        this.inputHeightRef = React.createRef()
        this.popUpItemRef = createRef()
        this.textAreaRef = createRef()
    }

    // eslint-disable-next-line react/no-deprecated
    componentWillReceiveProps(nextProps: Props) {
        const state: State = { }
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

    componentDidUpdate(prevProps: Props) {
        const { popUpMaxHeight } = this.state
        const { size, count } = this.props

        // контроль макс высоты и скрола с подгрузкой данных
        const overSize = count !== undefined && size !== undefined && count > 0 && count > size

        if (popUpMaxHeight && ((popUpMaxHeight < DEFAULT_POPUP_HEIGHT) || !overSize)) {
            return
        }

        const { options } = this.props

        if (!isEqual(options, prevProps.options)) {
            const popUpItem = this.popUpItemRef.current

            if (!popUpItem) {
                return
            }

            const popUpItemHeight = popUpItem.offsetHeight
            const calculatedMaxHeight = size !== undefined ? size * popUpItemHeight - 10 : -1

            if (calculatedMaxHeight > DEFAULT_POPUP_HEIGHT) {
                this.setState({ popUpMaxHeight: DEFAULT_POPUP_HEIGHT })

                return
            }

            this.setState({ popUpMaxHeight: calculatedMaxHeight })
        }
    }

    /**
     * установить акстивный элемент дропдауна
     * @param activeValueId
     * @private
     */
    setActiveValueId = (activeValueId: State['activeValueId']) => {
        this.setState({ activeValueId })
    }

    /**
     * обработка изменения значения при потери фокуса(считаем, что при потере фокуса пользователь закончил вводить
     * новое значение)
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

        const conditionForAddingAnObject = (resetOnBlur: Props['resetOnBlur'], input: State['input'], options: State['options'], value: State['value']) => (
            !resetOnBlur &&
            !input?.split(' ').every(char => char === '') &&
            !options?.some(person => person.id === input) &&
            !value?.some(person => person.id === input)
        )

        if (input && isEmpty(findValue) && resetOnBlur && !isPopupFocused) {
            this.setState(
                {
                    input: multiSelect ? '' : (value?.[0] && value?.[0][labelFieldId as keyof TOption]) || '',
                    value,
                },
                () => onChange(this.getValue()),
            )
        }

        if (!input && value?.length && value.every(element => !isEmptyModel(element))) {
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
        })
    }

    /**
     * Обработка изменения значения при выборе из дропдауна
     * @param item
     * @private
     */
    handleValueChangeOnSelect = (item: TOption) => {
        const { value } = this.state
        const { onChange, multiSelect, labelFieldId } = this.props

        this.setState(
            {
                input: multiSelect ? item[labelFieldId as keyof TOption] : '',
                value: multiSelect ? [...(value || []), item] : [item],
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

        /*
         * Зануляем значение по умолчанию, иначе бывают проблемы с несколькими redux-form
         * на одну модель при очистке поля по крестику:
         * при первом нажатии на крестик в модель попадает пустой объект и поле ведёт себя так как будто что-то выбрано
         * нормально очищалось только при повторном нажатии
         */
        return (multiSelect ? value : value?.[0]) || null
    }

    /**
     * Удаляет элемент из списка выбранных
     * @param item - элемент
     * @private
     */
    removeSelectedItem = (item: TOption) => {
        const { onChange } = this.props
        const { value: stateValue } = this.state
        const value = stateValue?.filter(i => i.id !== item.id)

        this.setState({ value }, () => onChange(value || null))
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

        this.setActiveValueId(null)
    }

    /**
     * установить / сбросить фокус
     * @param inputFocus
     * @private
     */
    setInputFocus = (inputFocus: State['inputFocus']) => {
        this.setState({ inputFocus })
    }

    /**
     * скрыть / показать попап
     * @param isExpanded
     * @private
     */
    setIsExpanded = (isExpanded: Props['isExpanded']) => {
        const { disabled, onToggle } = this.props

        if (!isExpanded || disabled) {
            return null
        }

        this.setState({
            isExpanded,
            inputFocus: isExpanded,
        }, () => {
            const { page, fetchData, value = [] } = this.props

            if (!value || !value.length || page === 1) {
                fetchData()
            }
        })

        onToggle(isExpanded)

        return null
    }

    /**
     * Выполняет поиск элементов для popUp, если установлен фильтр
     * @private
     */
    handleDataSearch = (input?: State['input'], delay?: Props['throttleDelay']) => {
        const { onSearch, filter, labelFieldId, options } = this.props

        if (filter && ['includes', 'startsWith', 'endsWith'].includes(filter as Filter)) {
            // eslint-disable-next-line @typescript-eslint/no-explicit-any
            const filterFunc = (item: TOption) => (String.prototype[filter as any] as any).call(item, input || '')
            const filteredData = options.filter(item => filterFunc(item[labelFieldId as keyof TOption]))

            this.setState({ options: filteredData })
        } else {
            // серверная фильтрация
            onSearch(input, delay || DEFAULT_DATA_SEARCH_DELAY)
        }
    }

    /**
     * новое значение инпута search)
     * @param input
     * @private
     */
    setNewInputValue = (input: State['input']) => {
        const { onInput, throttleDelay, multiSelect } = this.props
        const { input: stateInput } = this.state
        const onSetNewInputValue = (input: State['input']) => {
            onInput(input)
            this.handleDataSearch(input, throttleDelay)
        }

        if (stateInput !== input) {
            this.setState({ input }, () => onSetNewInputValue(input))

            if (!input && !multiSelect) {
                this.clearSelected()
            }
        }
    }

    /**
     * Обрабатывает выбор элемента из popUp
     * @param item - элемент массива options
     * @private
     */
    handleItemSelect = (item: TOption) => {
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
        }

        this.setState(
            prevState => ({
                value: multiSelect ? [...(prevState.value || []), item] : [item],
                input: multiSelect ? '' : item[labelFieldId as keyof TOption],
                options,
            }),
            () => {
                selectCallback()
                onBlur(this.getValue())

                if (this.inputRef) {
                    this.inputRef.current.focus()
                }
            },
        )

        const { input } = this.state

        if (input && multiSelect) {
            this.handleDataSearch('')
        }
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
        const { isExpanded } = this.state

        if (!disabled) {
            if (isExpanded) {
                this.clearSearchField()
            }
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
                    value: [...(currentValue || []), { [labelFieldId]: userInput }],
                    input: '',
                })
            }
        } else {
            this.setState({
                value: [...(currentValue || []), { [labelFieldId]: userInput }],
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
        const { onBlur, datasource, setFilter, sortFieldId, models = {} } = this.props
        const { isExpanded, value, isPopupFocused } = this.state

        if (datasource && sortFieldId) {
            const { filter } = models

            if (!isEmpty(filter)) {
                const newFilterModel = omit(filter, sortFieldId)

                setFilter(newFilterModel)
            }
        }

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

    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    setInputRef = (popperRef: any) => {
        this.textAreaRef = popperRef

        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        return (r: any) => {
            this.inputRef = r
            popperRef(r)
        }
    }

    toggle = () => {
        const { isExpanded } = this.state

        this.setIsExpanded(!isExpanded)
    }

    onInputSelectGroupClick = () => {
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        (this.textAreaRef as any).focus()
    }

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
            badge,
            statusFieldId,
            fetchData,
            style,
            alerts,
            autoFocus,
            popupAutoSize,
            maxTagTextLength,
            onDismiss,
            onKeyDown,
            page,
            size,
            count,
            filter,
            sortFieldId,
        } = this.props
        const {
            value: stateValue,
            input,
            isExpanded,
            inputFocus,
            activeValueId,
            options,
            popUpMaxHeight,
        } = this.state
        const inputSelectStyle = { width: '100%', cursor: 'text', ...style }

        const sorting = !isEmpty(sortFieldId) && !isEmpty(input)
        const needAddFilter = (!isEmpty(filter) || sorting) && !find(stateValue, item => item[labelFieldId] === input)

        const popUpStyle = { maxHeight: `${popUpMaxHeight}${MEASURE}` }

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
                    ref={this.inputHeightRef}
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
                            onClick={this.onInputSelectGroupClick}
                        >
                            <InputContent
                                setRef={this.setInputRef}
                                onFocus={this.onFocus}
                                onBlur={this.onInputBlur}
                                onKeyDown={onKeyDown}
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
                                activeValueId={activeValueId || ''}
                                setActiveValueId={this.setActiveValueId}
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
                        modifiers={{
                            offset: {
                                enabled: true,
                                offset: `0 ${this.inputHeightRef?.current?.containerRef?.current.clientHeight}px`,
                            } }}
                    >
                        <PopupList
                            handleMouseEnter={this.handlePopupListMouseEnter}
                            handleMouseLeave={this.handlePopupListMouseLeave}
                            scheduleUpdate={() => {}}
                            loading={loading}
                            isExpanded={isExpanded}
                            activeValueId={activeValueId}
                            setActiveValueId={this.setActiveValueId}
                            fetchData={fetchData}
                            page={page}
                            size={size}
                            count={count}
                            filterValue={{
                                [labelFieldId]: input,
                            }}
                            needAddFilter={needAddFilter}
                            options={options}
                            valueFieldId={valueFieldId}
                            labelFieldId={labelFieldId}
                            iconFieldId={iconFieldId}
                            imageFieldId={imageFieldId}
                            badge={badge}
                            statusFieldId={statusFieldId}
                            descriptionFieldId={descriptionFieldId}
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
                            popUpItemRef={this.popUpItemRef}
                            style={popUpStyle}
                        >
                            <div className="n2o-alerts">
                                {alerts?.map(alert => (
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

    static defaultProps = {
        valueFieldId: 'id',
        labelFieldId: 'name',
        iconFieldId: 'icon',
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
        descriptionFieldId: '',
        enabledFieldId: '',
        statusFieldId: '',
        imageFieldId: '',
        groupFieldId: '',
        sortFieldId: '',
        style: {},
        options: [],
        value: {},
        className: '',
        onSearch() {},
        onSelect() {},
        onToggle() {},
        onInput() {},
        onClose() {},
        onChange() {},
        fetchData() {},
        onBlur() {},
        onDismiss() {},
        setFilter() {},
    } as Props
}

type Props = {
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    alerts?: any[],
    /**
     * Авто фокусировка на селекте
     */
    autoFocus: boolean,
    /**
     * Данные для badge
     */
    badge?: BadgeType,
    className: string,
    /**
     * Флаг закрытия попапа при выборе
     */
    closePopupOnSelect: boolean,
    count?: number,
    datasource?: string,
    descriptionFieldId: string,
    /**
     * Флаг активности
     */
    disabled: boolean,
    /**
     * Неактивные данные
     */
    disabledValues: [],
    enabledFieldId: string,
    expandPopUp: boolean,
    fetchData(): void,
    /**
     * Варианты фильтрации
     */
    filter: Filter | boolean,
    /**
     * Формат
     */
    format?: string,
    /**
     * Поле для группировки
     */
    groupFieldId: string,
    /**
     * Флаг наличия чекбоксов в селекте
     */
    hasCheckboxes: boolean,
    /**
     * Ключ icon в данных
     */
    iconFieldId: string,
    /**
     * Ключ image в данных
     */
    imageFieldId: string,
    isExpanded?: boolean,
    /**
     * Ключ label в данных
     */
    labelFieldId: string,
    /**
     * Флаг загрузки
     */
    loading: boolean,
    /**
     * Максимальная длина текста в тэге, до усечения
     */
    maxTagTextLength?: number,
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    models?: any,
    /**
     * Мульти выбор значений
     */
    multiSelect: boolean,
    onBlur(arg: TOption | TOption[] | null): void,
    /**
     * Callback на изменение
     */
    onChange(arg: TOption | TOption[] | null): void,
    /**
     * Callback на закрытие
     */
    onClose(): void,
    onDismiss(arg: string): void,
    onInput(input: State['input']): void,
    onKeyDown?(evt: KeyboardEvent<HTMLTextAreaElement | HTMLInputElement>): void,
    /**
     * Callback на поиск
     */
    onSearch(input: State['input'], delay: Props['throttleDelay']): void,
    /**
     * Callback на выбор
     */
    onSelect(item: TOption): void,
    /**
     * Callback на переключение
     */
    onToggle(arg: boolean): void,
    openOnFocus?: boolean,
    /**
     * Массив данных
     */
    options: TOption[],
    page?: number,
    /**
     * Placeholder контрола
     */
    placeholder?: string,
    /**
     * Флаг авто размера попапа
     */
    popupAutoSize: boolean,
    /**
     * Фича, при которой сбрасывается значение контрола, если оно не выбрано из popup
     */
    resetOnBlur: boolean,
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    setFilter(arg: Pick<any, number | symbol>): void,
    size?: number,
    /**
     * Ключ сортировки в данных
     */
    sortFieldId: string,
    statusFieldId: string,
    style: object,
    throttleDelay?: number,
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

type State = {
    activeValueId?: string | null,
    input?: string,
    inputFocus?: boolean,
    isExpanded?: boolean,
    isInputSelected?: boolean,
    isPopupFocused?: boolean,
    options?: Props['options'],
    popUpMaxHeight?: number,
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    value?: any[]
}

export const InputSelectComponent = onClickOutside(InputSelect)

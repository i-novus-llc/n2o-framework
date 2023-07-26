import React from 'react'
import find from 'lodash/find'
import isEmpty from 'lodash/isEmpty'
import isFunction from 'lodash/isFunction'
import filter from 'lodash/filter'
import includes from 'lodash/includes'
import isEqual from 'lodash/isEqual'
import map from 'lodash/map'
import isArray from 'lodash/isArray'
import isNil from 'lodash/isNil'
import pick from 'lodash/pick'
import onClickOutside from 'react-onclickoutside'
import classNames from 'classnames'
import { Manager, Reference, Popper } from 'react-popper'

import { BadgeType, PopupList } from '@i-novus/n2o-components/lib/inputs/InputSelect/PopupList'
import { InputContent } from '@i-novus/n2o-components/lib/inputs/InputSelect/InputContent'
import { InputSelectGroup } from '@i-novus/n2o-components/lib/inputs/InputSelect/InputSelectGroup'
import { Alert } from '@i-novus/n2o-components/lib/display/Alerts/Alert'
import { Filter, TOption } from '@i-novus/n2o-components/lib/inputs/InputSelect/types'

import listContainer from '../listContainer'

type State = {
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    value: any[]
    input: string
    options?: TOption[]
    isExpanded: boolean
    activeValueId: string
}

const DEFAULT_DATA_SEARCH_DELAY = 400

class AutoComplete extends React.Component<Props, State> {
    input: Element | null

    constructor(props: Props) {
        super(props)

        this.state = {
            isExpanded: false,
            value: [],
            input: props.value && !props.tags ? props.value : '',
            activeValueId: '',
        }

        this.input = null
    }

    componentDidMount = () => {
        const { value, tags } = this.props

        if (!isEmpty(value)) {
            const currentValue = isArray(value) ? value : [value]

            this.setState({
                value: value ? currentValue : [],
                input: value && !tags ? value : '',
            })
        }
    }

    componentDidUpdate = (prevProps: Props, prevState: State) => {
        const { value, options, tags } = this.props
        const compareListProps = ['options', 'value']
        const compareListState = ['input']

        if (
            !isEqual(
                pick(prevProps, compareListProps),
                pick(this.props, compareListProps),
            ) || !isEqual(
                pick(prevState, compareListState),
                pick(this.state, compareListState),
            )
        ) {
            const state = {} as State

            if (!isEqual(prevProps.options, options)) {
                state.options = options
            }

            if (prevProps.value !== value) {
                const currentValue = isArray(value) ? value : [value]

                state.value = value ? currentValue : []
                state.input = value && !tags ? value : ''
            }

            if (!isEmpty(state)) {
                this.setState(state)
            }
        }
    }

    /**
     * Обрабатывает клик за пределы компонента
     * вызывается библиотекой react-onclickoutside
     */
    handleClickOutside = () => {
        const { isExpanded } = this.state

        if (isExpanded) {
            this.setIsExpanded(false)
            this.onBlur()
        }
    }

    // eslint-disable-next-line consistent-return
    calcPopperWidth = () => {
        const { input } = this
        const { popupAutoSize } = this.props

        if (input && !popupAutoSize) {
            return input.getBoundingClientRect().width
        }
    }

    setIsExpanded = (isExpanded: State['isExpanded']) => {
        const { disabled, onToggle, onClose, fetchData } = this.props
        const { isExpanded: previousIsExpanded } = this.state

        if (!disabled && isExpanded !== previousIsExpanded) {
            this.setState({ isExpanded })
            onToggle(isExpanded)

            if (isExpanded) {
                fetchData({ page: 1 })
            } else {
                onClose()
            }
        }
    }

    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    setInputRef = (popperRef: any) => (r: any) => {
        this.input = r
        popperRef(r)
    }

    onFocus = () => {
        const { openOnFocus } = this.props

        if (openOnFocus) {
            this.setIsExpanded(true)
        }
    }

    onClick = () => {
        this.setIsExpanded(true)
    }

    handleDataSearch: Props['onSearch'] = (input, delay, callback) => {
        const { onSearch, filter, labelFieldId, options } = this.props

        if (filter && ['includes', 'startsWith', 'endsWith'].includes(filter as Filter)) {
            // eslint-disable-next-line @typescript-eslint/no-explicit-any
            const filterFunc = (item: TOption) => (String.prototype[filter as any] as any).call(item, input)
            const filteredOptions = options.filter(item => filterFunc(item[labelFieldId as keyof TOption]))

            this.setState({ options: filteredOptions })
        } else {
            // серверная фильтрация
            const { value } = this.state
            const labels = map(value, item => item[labelFieldId])

            if (labels.some(label => label === input)) {
                onSearch('', delay || DEFAULT_DATA_SEARCH_DELAY, callback)
            } else {
                onSearch(input, delay || DEFAULT_DATA_SEARCH_DELAY, callback)
            }
        }
    }

    onChange = (userInput: State['input']) => {
        const { onInput, tags, onChange } = this.props
        const { input } = this.state

        const onSetNewInputValue = (input: State['input']) => {
            onInput(input)
            if (!tags && input === '') {
                onChange([])
            } else if (!tags) {
                onChange([input])
            }
            this.handleDataSearch(input)
        }

        if (!isEqual(input, userInput)) {
            const getSelected = (prevState: State) => {
                if (tags) {
                    return prevState.value
                }

                // ToDo здесь вероятно баг, удалено лишнее
                return [userInput]
            }

            this.setState(
                prevState => ({
                    input: userInput,
                    value: getSelected(prevState),
                    isExpanded: true,
                }),
                () => onSetNewInputValue(userInput),
            )
        }
    }

    onBlur = () => {
        const { onBlur } = this.props
        const { value } = this.state

        if (isFunction(onBlur)) {
            onBlur(value)
        }
    }

    onSelect = (item: TOption) => {
        if (!item) {
            return
        }

        const { onChange, closePopupOnSelect, tags, labelFieldId } = this.props

        this.setState(
            prevState => ({
                value: tags ? [...prevState.value, item] : [item],
                input: !tags ? item[labelFieldId as keyof TOption] : '',
            }),
            () => {
                const { value, input } = this.state

                if (closePopupOnSelect) {
                    this.setIsExpanded(false)
                }

                if (typeof item === 'string') {
                    this.forceUpdate()
                }

                if (tags) {
                    onChange(value)

                    return
                }

                onChange(input)
            },
        )
    }

    handleElementClear = () => {
        const { onChange, onBlur } = this.props
        const { input, value } = this.state

        this.setState(
            {
                input: '',
                value: [],
            },
            () => {
                this.handleDataSearch(input)
                onChange(value)
                onBlur(null)
            },
        )
    }

    setActiveValueId = (activeValueId: State['activeValueId']) => {
        this.setState({ activeValueId })
    }

    removeSelectedItem = (item: TOption, index: number | null = null) => {
        const { onChange } = this.props
        const { value } = this.state
        let newValue = value.slice()

        if (!isNil(index)) {
            newValue.splice(index, 1)
        } else {
            newValue = value.slice(0, value.length - 1)
        }

        this.setState({ value: newValue }, () => {
            onChange(newValue)
            this.forceUpdate()
        })
    }

    onButtonClick = () => {
        if (this.input) {
            (this.input as HTMLInputElement).focus()
        }
    }

    render() {
        const { isExpanded, value, activeValueId, input } = this.state
        const {
            loading,
            className,
            valueFieldId,
            labelFieldId,
            iconFieldId,
            disabled,
            placeholder,
            disabledValues,
            imageFieldId,
            groupFieldId,
            hasCheckboxes,
            format,
            badge,
            fetchData,
            page,
            style,
            alerts,
            autoFocus,
            options,
            tags,
            maxTagTextLength,
            onDismiss,
        } = this.props
        const needAddFilter = !find(value, item => item[labelFieldId] === input)
        const filteredOptions = filter(
            options,
            item => includes(item[labelFieldId as keyof TOption], input) || isEmpty(input),
        )

        return (
            <div
                className={classNames(
                    'n2o-autocomplete w-100 n2o-input-select n2o-input-select--default',
                    className,
                )}
                style={style}
            >
                <Manager>
                    <Reference>
                        {({ ref }) => (
                            <InputSelectGroup
                                withoutButtons
                                isExpanded={isExpanded}
                                loading={loading}
                                selected={value}
                                iconFieldId={iconFieldId}
                                imageFieldId={imageFieldId}
                                multiSelect={tags}
                                disabled={disabled}
                                className={`${className} ${isExpanded ? 'focus' : ''}`}
                                input={input}
                                onClearClick={this.handleElementClear}
                            >
                                <InputContent
                                    tags
                                    mode="autocomplete"
                                    maxTagTextLength={maxTagTextLength}
                                    multiSelect={tags}
                                    options={filteredOptions}
                                    setRef={this.setInputRef}
                                    onInputChange={this.onChange}
                                    setActiveValueId={this.setActiveValueId}
                                    closePopUp={() => this.setIsExpanded(false)}
                                    openPopUp={() => this.setIsExpanded(true)}
                                    selected={value}
                                    value={input}
                                    onFocus={this.onFocus}
                                    onClick={this.onClick}
                                    onRemoveItem={this.removeSelectedItem}
                                    isExpanded={isExpanded}
                                    valueFieldId={valueFieldId}
                                    activeValueId={activeValueId}
                                    onSelect={this.onSelect}
                                    disabled={disabled}
                                    disabledValues={disabledValues}
                                    placeholder={placeholder}
                                    labelFieldId={labelFieldId}
                                    autoFocus={autoFocus}
                                />
                            </InputSelectGroup>
                        )}
                    </Reference>
                    {isExpanded && !isEmpty(filteredOptions) && (
                        <Popper
                            placement="bottom-start"
                            strategy="fixed"
                        >
                            {/* eslint-disable-next-line @typescript-eslint/no-explicit-any */}
                            {({ ref, placement, scheduleUpdate }: any) => (
                                <div
                                    ref={ref}
                                    style={{
                                        minWidth: this.calcPopperWidth(),
                                        maxWidth: 600,
                                    }}
                                    data-placement={placement}
                                    className="n2o-pop-up"
                                >
                                    <PopupList
                                        scheduleUpdate={scheduleUpdate}
                                        autocomplete
                                        isExpanded={isExpanded}
                                        activeValueId={activeValueId}
                                        setActiveValueId={this.setActiveValueId}
                                        fetchData={fetchData}
                                        page={page}
                                        needAddFilter={needAddFilter}
                                        options={filteredOptions}
                                        valueFieldId={valueFieldId}
                                        labelFieldId={labelFieldId}
                                        iconFieldId={iconFieldId}
                                        imageFieldId={imageFieldId}
                                        badge={badge}
                                        onSelect={this.onSelect}
                                        selected={value}
                                        disabledValues={disabledValues}
                                        groupFieldId={groupFieldId}
                                        hasCheckboxes={hasCheckboxes}
                                        format={format}
                                        renderIfEmpty={false}
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
                                </div>
                            )}
                        </Popper>
                    )}
                </Manager>
            </div>
        )
    }

    static defaultProps = {
        valueFieldId: 'label',
        labelFieldId: 'name',
        iconFieldId: 'icon',
        imageFieldId: 'image',
        loading: false,
        disabled: false,
        disabledValues: [],
        resetOnBlur: false,
        filter: false,
        multiSelect: false,
        closePopupOnSelect: true,
        hasCheckboxes: false,
        expandPopUp: false,
        flip: false,
        autoFocus: false,
        popupAutoSize: false,
        tags: false,
        options: [],
        value: {},
        descriptionFieldId: '',
        enabledFieldId: '',
        statusFieldId: '',
        groupFieldId: '',
        sortFieldId: '',
        onSearch() {},
        onSelect() {},
        onToggle() {},
        onInput() {},
        fetchData() {},
        onClose() {},
        onChange() {},
        onBlur() {},
        onDismiss() {},
    } as Props
}

type Props = {
    /**
     * Стили
     */
    style?: object,
    /**
     * Флаг загрузки
     */
    loading: boolean,
    /**
     * Массив данных
     */
    options: TOption[],
    /**
     * Ключ значения
     */
    valueFieldId: string,
    /**
     * Ключ отображаемого значения
     */
    labelFieldId: string,
    /**
     * Ключ icon в данных
     */
    iconFieldId: string,
    /**
     * Ключ image в данных
     */
    imageFieldId: string,
    /**
     * Данные для badge
     */
    badge?: BadgeType,
    /**
     * Флаг активности
     */
    disabled: boolean,
    /**
     * Неактивные данные
     */
    disabledValues: [],
    /**
     * Значение
     */
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    value: any,
    /**
     * Callback на переключение
     */
    onToggle(arg: boolean): void,
    onInput(input: string): void,
    /**
     * Callback на изменение
     */
    onChange(value: Props['value']): void,
    /**
     * Callback на выбор
     */
    onSelect(): void,
    /**
     * Placeholder контрола
     */
    placeholder?: string,
    /**
     * Фича, при которой сбрасывается значение контрола, если оно не выбрано из popup
     */
    resetOnBlur: boolean,
    /**
     * Callback на открытие
     */
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    fetchData(arg: any): void,
    /**
     * Callback на закрытие
     */
    onClose(): void,
    /**
     * Мульти выбор значений
     */
    multiSelect: boolean,
    /**
     * Поле для группировки
     */
    groupFieldId: string,
    /**
     * Флаг закрытия попапа при выборе
     */
    closePopupOnSelect: boolean,
    /**
     * Флаг наличия чекбоксов в селекте
     */
    hasCheckboxes: boolean,
    /**
     * Формат
     */
    format?: string,
    /**
     * Callback на поиск
     */
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    onSearch(input: State['input'], delay?: number, callback?: any): void,
    expandPopUp: boolean,
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    alerts?: any[],
    /**
     * Авто фокусировка на селекте
     */
    autoFocus: boolean,
    /**
     * Флаг авто размера попапа
     */
    popupAutoSize: boolean,
    /**
     * Мод работы Autocomplete
     */
    tags: boolean,
    /**
     * Максимальная длина текста в тэге, до усечения
     */
    maxTagTextLength?: number,
    openOnFocus?: boolean,
    filter: Filter | boolean,
    className?: string,
    onBlur(value: Props['value']): void,
    onDismiss(id: string | number): void,
    flip: boolean,
    page?: number,
}

export { AutoComplete }
// eslint-disable-next-line @typescript-eslint/no-explicit-any
export default listContainer(onClickOutside(AutoComplete as any) as any)

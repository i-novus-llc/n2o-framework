import React from 'react'
import PropTypes from 'prop-types'
import find from 'lodash/find'
import isEmpty from 'lodash/isEmpty'
import isFunction from 'lodash/isFunction'
import get from 'lodash/get'
import filter from 'lodash/filter'
import includes from 'lodash/includes'
import isEqual from 'lodash/isEqual'
import map from 'lodash/map'
import isArray from 'lodash/isArray'
import isString from 'lodash/isString'
import isNil from 'lodash/isNil'
import pick from 'lodash/pick'
import { compose, mapProps } from 'recompose'
import onClickOutside from 'react-onclickoutside'
import classNames from 'classnames'
import { Manager, Reference, Popper } from 'react-popper'

import listContainer from '../listContainer'
import InputContent from '../InputSelect/InputContent'
import InputSelectGroup from '../InputSelect/InputSelectGroup'
import { MODIFIERS } from '../DatePicker/utils'
import PopupList from '../InputSelect/PopupList'
import Alert from '../../snippets/Alerts/Alert'

class AutoComplete extends React.Component {
    constructor(props) {
        super(props)

        this.state = {
            isExpanded: false,
            value: [],
            input: props.value && !props.tags ? props.value : '',
            activeValueId: null,
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

    componentDidUpdate = (prevProps, prevState) => {
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
            const state = {}

            if (!isEqual(prevProps.options, options)) {
                state.options = options
            }

            if (prevProps.value !== value) {
                const currentValue = isArray(value) ? value : [value]

                state.value = value ? currentValue : []

                if (isEmpty(state.value)) {
                    state.input = value && !tags ? value : ''
                }
            }

            if (!isEmpty(state)) {
                this.setState(state)
            }
        }
    };

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

    setIsExpanded = (isExpanded) => {
        const { disabled, onToggle, onClose, onOpen } = this.props
        const { isExpanded: previousIsExpanded } = this.state

        if (!disabled && isExpanded !== previousIsExpanded) {
            this.setState({ isExpanded })
            onToggle(isExpanded)

            if (isExpanded) {
                onOpen()
            } else {
                onClose()
            }
        }
    }

    setInputRef = popperRef => (r) => {
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

    handleDataSearch = (input, delay = 400, callback) => {
        const { onSearch, filter, valueFieldId, options } = this.props

        if (filter && ['includes', 'startsWith', 'endsWith'].includes(filter)) {
            const filterFunc = item => String.prototype[filter].call(item, input)
            const filteredData = filter(options, item => filterFunc(item[valueFieldId]))

            this.setState({ options: filteredData })
        } else {
        // серверная фильтрация
            const { value } = this.state
            const labels = map(value, item => item[valueFieldId])

            if (labels.some(label => label === input)) {
                onSearch('', delay, callback)
            } else {
                onSearch(input, delay, callback)
            }
        }
    }

    onChange = (userInput) => {
        const { onInput, tags, onChange } = this.props
        const { input } = this.state

        const onSetNewInputValue = (input) => {
            onInput(input)
            if (!tags && input === '') {
                onChange([])
            } else if (!tags) {
                onChange([input])
            }
            this.handleDataSearch(input)
        }

        if (!isEqual(input, userInput)) {
            const getSelected = (prevState) => {
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

    onSelect = (item) => {
        const { valueFieldId, onChange, closePopupOnSelect, tags } = this.props

        const currentValue = isString(item) ? item : get(item, valueFieldId)

        this.setState(
            prevState => ({
                value: tags ? [...prevState.value, currentValue] : [currentValue],
                input: !tags ? currentValue : '',
            }),
            () => {
                const { value, input } = this.state

                if (closePopupOnSelect) {
                    this.setIsExpanded(false)
                }

                if (isString(currentValue)) {
                    this.forceUpdate()
                }
                if (tags) {
                    onChange(value)
                } else {
                    onChange(input)
                }
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

    setActiveValueId = (activeValueId) => {
        this.setState({ activeValueId })
    }

    removeSelectedItem = (item, index = null) => {
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
            this.input.focus()
        }
    }

    render() {
        const { isExpanded, value, activeValueId, input } = this.state
        const {
            loading,
            className,
            valueFieldId,
            iconFieldId,
            disabled,
            placeholder,
            disabledValues,
            imageFieldId,
            groupFieldId,
            hasCheckboxes,
            format,
            badgeFieldId,
            badgeColorFieldId,
            onScrollEnd,
            style,
            alerts,
            autoFocus,
            options,
            data,
            tags,
            maxTagTextLength,
            onDismiss,
        } = this.props
        const needAddFilter = !find(value, item => item[valueFieldId] === input)
        const optionsList = !isEmpty(data) ? data : options
        const filteredOptions = filter(
            optionsList,
            item => includes(item[valueFieldId], input) || isEmpty(input),
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
                                setIsExpanded={this.setIsExpanded}
                                loading={loading}
                                selected={value}
                                iconFieldId={iconFieldId}
                                imageFieldId={imageFieldId}
                                multiSelect={tags}
                                disabled={disabled}
                                className={`${className} ${isExpanded ? 'focus' : ''}`}
                                setSelectedItemsRef={this.setSelectedItemsRef}
                                input={input}
                                onClearClick={this.handleElementClear}
                                onButtonClick={this.onButtonClick}
                            >
                                <InputContent
                                    tags
                                    mode="autocomplete"
                                    maxTagTextLength={maxTagTextLength}
                                    multiSelect={tags}
                                    options={filteredOptions}
                                    setRef={this.setInputRef(ref)}
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
                                    loading={loading}
                                    disabled={disabled}
                                    disabledValues={disabledValues}
                                    placeholder={placeholder}
                                    iconFieldId={iconFieldId}
                                    imageFieldId={imageFieldId}
                                    labelFieldId={valueFieldId}
                                    autoFocus={autoFocus}
                                />
                            </InputSelectGroup>
                        )}
                    </Reference>
                    {isExpanded && !isEmpty(filteredOptions) && (
                        <Popper
                            placement="bottom-start"
                            modifiers={MODIFIERS}
                            strategy="fixed"
                        >
                            {({ ref, style, placement, scheduleUpdate }) => (
                                <div
                                    ref={ref}
                                    style={{
                                        ...style,
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
                                        onScrollEnd={onScrollEnd}
                                        needAddFilter={needAddFilter}
                                        options={filteredOptions}
                                        valueFieldId={valueFieldId}
                                        labelFieldId={valueFieldId}
                                        iconFieldId={iconFieldId}
                                        imageFieldId={imageFieldId}
                                        badgeFieldId={badgeFieldId}
                                        badgeColorFieldId={badgeColorFieldId}
                                        onSelect={this.onSelect}
                                        selected={value}
                                        disabledValues={disabledValues}
                                        groupFieldId={groupFieldId}
                                        hasCheckboxes={hasCheckboxes}
                                        format={format}
                                        renderIfEmpty={false}
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
                                </div>
                            )}
                        </Popper>
                    )}
                </Manager>
            </div>
        )
    }
}

AutoComplete.propTypes = {
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
     * Ключ значения
     */
    valueFieldId: PropTypes.string,
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
     * Флаг активности
     */
    disabled: PropTypes.bool,
    /**
     * Неактивные данные
     */
    disabledValues: PropTypes.array,
    /**
     * Значение
     */
    value: PropTypes.oneOfType([PropTypes.array, PropTypes.string]),
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
     * Мод работы Autocomplete
     */
    tags: PropTypes.bool,
    /**
     * Максимальная длина текста в тэге, до усечения
     */
    maxTagTextLength: PropTypes.number,
    openOnFocus: PropTypes.bool,
    filter: PropTypes.string,
    className: PropTypes.string,
    onBlur: PropTypes.func,
    data: PropTypes.array,
    onDismiss: PropTypes.func,
    flip: PropTypes.bool,
}

AutoComplete.defaultProps = {
    valueFieldId: 'label',
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
    flip: false,
    autoFocus: false,
    popupAutoSize: false,
    tags: false,
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

const enhance = compose(
    listContainer,
    mapProps(props => ({
        ...props,
        options: !isEmpty(props.data) ? props.data : props.options,
    })),
    onClickOutside,
)

export { AutoComplete }
export default enhance(AutoComplete)

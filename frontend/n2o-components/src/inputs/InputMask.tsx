import React, { SyntheticEvent, ChangeEventHandler, KeyboardEvent } from 'react'
import MaskedInput, { conformToMask, Mask } from 'react-text-mask'
import classNames from 'classnames'
import isEqual from 'lodash/isEqual'
import createNumberMask from 'text-mask-addons/dist/createNumberMask'
import { isArray, isFunction } from 'lodash'

import { withRightPlaceholder } from '../helpers/withRightPlaceholder'
import { TBaseInputProps, TBaseProps } from '../types'

export type PresetType = 'phone' | 'post-code' | 'date' | 'money' | 'percentage' | 'card'
type MaskType = string | Array<string | RegExp> | ((val?: unknown) => Array<string | RegExp>)
type InputMaskProps = TBaseProps & Omit<TBaseInputProps<string>, 'onBlur'> & {
    clearOnBlur?: boolean,
    dictionary?: Record<string, RegExp>,
    // https://github.com/text-mask/text-mask/blob/master/componentDocumentation.md#guide
    guide?: boolean,
    // https://github.com/text-mask/text-mask/blob/master/componentDocumentation.md#keepcharpositions
    keepCharPositions?: boolean,
    // Стандартная конфигурация: 9 - цифра, S - английская буква, Б - русская буква. Дополнительную конфигурацию можно осуществить, используя dictionary
    mask?: MaskType
    onBlur?(value: string): void,
    onChange?(value: string): void,
    onKeyDown?(evt: KeyboardEvent<HTMLInputElement>): void,
    placeholder?: string,
    // символ, который будет на месте незаполненного символа маски
    placeholderChar?: string,
    preset?: PresetType,
    // настройки пресета для InputMoney
    presetConfig?: Record<string, unknown>,
}

type InputMaskState = {
    guide: boolean,
    value: string
}

class InputMaskComponent extends React.Component<InputMaskProps, InputMaskState> {
    valid: string | boolean

    dict: Record<string | number, RegExp>

    constructor(props: InputMaskProps) {
        super(props)
        this.state = {
            value: props.value || '',
            guide: false,
        }
        this.valid = false
        this.dict = {
            9: /\d/,
            S: /[A-Za-z]/,
            Б: /[А-я]/,
            ...props.dictionary,
        }
    }

    /**
     * преобразует маску-функцию, маску-строку в массив-маску (с regexp вместо символов) при помощи _mapToArray
     */
    mask = () => {
        const { mask } = this.props

        if (isArray(mask)) {
            return mask
        }

        if (isFunction(mask)) {
            return mask()
        }

        return this.mapToArray(mask)
    }

    /**
     * возвращает маску для пресета
     * @returns (number) возвращает массив-маску для пресета-аргумента
     */
    // eslint-disable-next-line consistent-return
    preset = (preset?: PresetType) => {
        const { presetConfig = {} } = this.props

        switch (preset) {
            case 'phone':
                return this.mapToArray('+9 (999)-999-99-99')
            case 'post-code':
                return this.mapToArray('999999')
            case 'date':
                return this.mapToArray('99.99.9999')
            case 'money':
                return createNumberMask(presetConfig)
            case 'percentage':
                return createNumberMask({ prefix: '', suffix: '%' })
            case 'card':
                return this.mapToArray('9999 9999 9999 9999')
            default:
                return undefined
        }
    }

    /**
     * возвращает индекс первого символа маски, который еще не заполнен
     * @returns (number) индекс первого символа маски, который еще не заполнен
     */
    indexOfFirstPlaceHolder = (value = '') => {
        const { placeholderChar } = this.props

        if (!placeholderChar) {
            return null
        }

        return value.toString().indexOf(placeholderChar)
    }

    /**
     * возвращает индекс последнего символа маски, который еще не заполнен
     * @returns (number) индекс последнего символа маски, который еще не заполнен
     */
    indexOfLastPlaceholder = (mask: MaskType) => {
        if (typeof mask === 'function') {
            return mask()
                .map(item => item instanceof RegExp)
                .lastIndexOf(true)
        }

        if (typeof mask === 'string') {
            return Math.max(
                ...Object.keys(this.dict).map(char => mask.lastIndexOf(char)),
            )
        }

        if (isArray(mask)) {
            return mask.map(item => item instanceof RegExp).lastIndexOf(true)
        }

        return -1
    }

    isValid = (value: string) => {
        const { preset, mask, guide = true } = this.props

        if (guide) {
            return value && this.indexOfFirstPlaceHolder(value) === -1
        }

        if (preset) {
            return (
                value.length > this.indexOfLastPlaceholder(this.preset(preset) || mask)
            )
        }

        return false
    }

    /**
     * преобразование строки маски в массив (уже с регулярными выражениями)
     */
    mapToArray = (mask = '') => mask.split('').map(char => (this.dict[char] ? this.dict[char] : char))

    onChange: ChangeEventHandler = (e) => {
        const { value } = e.target as HTMLInputElement
        const { guide = true, onChange = () => {} } = this.props

        this.valid = this.isValid(value)

        this.setState({ value, guide }, () => {
            if (this.valid || value === '') {
                onChange(value)
            }
        })
    }

    onBlur = (e: SyntheticEvent) => {
        const { value } = e.nativeEvent.target as HTMLInputElement
        const { onBlur = () => {}, clearOnBlur, onChange = () => {} } = this.props

        this.valid = this.isValid(value)
        onBlur(value)
        if (!this.valid) {
            const newValue = clearOnBlur ? '' : value

            this.setState(
                { value: newValue, guide: false },
                () => newValue === '' && onChange(newValue),
            )
        }
    }

    onFocus = () => {
        const { guide = true } = this.props
        const { value } = this.state

        this.valid = this.isValid(value)
        if (!this.valid) {
            this.setState({ guide })
        }
    }

    componentDidUpdate(prevProps: InputMaskProps) {
        const { value: valueFromState } = this.state
        const { value: valueFromProps = '', dictionary = {} } = this.props

        if (
            !isEqual(prevProps.value, valueFromProps) &&
            !isEqual(valueFromProps, valueFromState)
        ) {
            this.setState({
                value: this.isValid(valueFromProps) ? valueFromProps : '',
            })
        }

        this.dict = { ...this.dict, ...dictionary }
        this.valid = this.isValid(valueFromState)
    }

    formatToReactTextMask = (value: InputMaskState['value'], mask: Mask) => {
        if (!value) { return '' }

        const { conformedValue } = conformToMask(String(value), mask)

        return conformedValue
    }

    /**
     * базовый рендер компонента
     */
    render() {
        const {
            preset,
            placeholderChar = '_',
            placeholder,
            className,
            disabled,
            autoFocus,
            keepCharPositions,
            onKeyDown,
        } = this.props
        const { guide, value: stateValue } = this.state
        const mask = this.preset(preset)

        return (
            <MaskedInput
                disabled={disabled}
                className={classNames(['form-control', 'n2o-input-mask', className])}
                placeholderChar={placeholderChar}
                placeholder={placeholder}
                guide={guide}
                autoFocus={autoFocus}
                mask={mask || this.mask}
                value={this.formatToReactTextMask(stateValue, mask || this.mask)}
                onBlur={this.onBlur}
                onChange={this.onChange}
                onFocus={this.onFocus}
                keepCharPositions={keepCharPositions}
                render={(ref, props) => (
                    <input
                        ref={(input: HTMLInputElement) => ref(input)}
                        {...props}
                    />
                )}
                onKeyDown={onKeyDown}
            />
        )
    }

    static defaultProps = {
        onChange: () => {},
        placeholderChar: '_',
        guide: true,
        keepCharPositions: false,
        clearOnBlur: false,
        value: '',
        dictionary: {},
        mask: '',
        presetConfig: {},
        onBlur: () => {},
        onKeyDown: () => {},
        disabled: false,
    } as InputMaskProps
}

export const InputMask = withRightPlaceholder<InputMaskProps>(InputMaskComponent)

import React, { ChangeEvent, KeyboardEvent, LegacyRef } from 'react'
import classNames from 'classnames'
import isObject from 'lodash/isObject'
import isArray from 'lodash/isArray'
import omit from 'lodash/omit'
import isString from 'lodash/isString'

import { TBaseInputProps, TBaseProps } from '../types'

/**
 * Контрол Input
 * @reactProps {boolean} length - максимальная длина значения
 * @reactProps {string} value - значение поля
 * @reactProps {string} placeholder - плэйсхолдер
 * @reactProps {boolean} disabled - флаг неактивности поля
 * @reactProps {boolean} disabled - флаг только для чтения
 * @reactProps {boolean} autoFocus - автофокус
 * @reactProps {function} onFocus - callback на фокус
 * @reactProps {function} onClick - callback на клик
 * @reactProps {function} onPaste - callback при вставке в инпут
 * @reactProps {function} onBlur - callback при блюре инпута
 * @reactProps {function} onChange - callback при изменение инпута
 * @reactProps {function} onKeyDown - callback при нажатии на кнопку клавиатуры
 * @reactProps {boolean} autoFocus - флаг автофокуса
 * @reactProps {string} className - css-класс
 * @reactProps {object} style - объект стилей
 * @reactProps {boolean} disabled - флаг неактивности поля
 * @reactProps {string} type - тип поля
 */

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export type Props = TBaseProps & TBaseInputProps<any> & {
    autoFocus: boolean,
    checked: boolean,
    inputRef?: LegacyRef<HTMLInputElement>,
    label?: string,
    length?: string,
    max?: number,
    min?: number,
    name: string,
    onClick(): void,
    onKeyDown(arg: KeyboardEvent): void,
    onPaste(): void,
    placeholder?: string,
    step?: string | number,
    tabIndex: number,
    type: string
}

export class Input extends React.Component<Props> {
    input: HTMLInputElement | null

    constructor(props: Props) {
        super(props)
        this.input = null
        this.handleChange = this.handleChange.bind(this)
        this.setRef = this.setRef.bind(this)
    }

    focus() {
        this.input?.focus()
    }

    blur() {
        this.input?.blur()
    }

    handleChange(e: ChangeEvent<HTMLInputElement>) {
        const { props, input } = this

        if (props.disabled) {
            return
        }
        const value = isObject(props.value) || isArray(props.value)
            ? props.value
            : (e.target as HTMLInputElement).value

        if (props.onChange) {
            props.onChange({
                target: {
                    ...props,
                    value,
                },
                stopPropagation() {
                    e.stopPropagation()
                },
                preventDefault() {
                    e.preventDefault()
                },
                getElement() {
                    return input
                },
                getNormalizeValue() {
                    return { [props.name]: props.value }
                },
                nativeEvent: e,
            })
        }
    }

    setRef(node: HTMLInputElement) {
        this.input = node
    }

    handleKeyDown = (evt: KeyboardEvent<HTMLInputElement>) => {
        const { onKeyDown } = this.props

        if (onKeyDown) {
            onKeyDown(evt)
        }

        if ((evt.target as HTMLInputElement).tagName === 'INPUT' && (evt.target as HTMLInputElement).type === 'checkbox' && evt.key === 'Enter') {
            // eslint-disable-next-line @typescript-eslint/no-explicit-any
            this.handleChange(evt as any)
        }
    }

    render() {
        const {
            autoFocus,
            className,
            disabled,
            id,
            inputRef,
            label,
            name,
            onBlur,
            onClick,
            onFocus,
            onPaste,
            placeholder,
            readonly,
            style,
            type,
            value,
            autocomplete,
            ...restProps
        } = this.props

        return (
            <input
                ref={inputRef}
                className={classNames('n2o-input', className)}
                style={style}
                id={id}
                name={name}
                type={type}
                disabled={disabled}
                autoFocus={autoFocus}
                value={value}
                onPaste={onPaste}
                // eslint-disable-next-line @typescript-eslint/no-explicit-any
                onFocus={onFocus as any}
                // eslint-disable-next-line @typescript-eslint/no-explicit-any
                onBlur={onBlur as any}
                onClick={onClick}
                onKeyDown={this.handleKeyDown}
                placeholder={placeholder}
                readOnly={readonly}
                {...omit(restProps, ['length', 'onChange', 'onKeyDown'])}
                onChange={this.handleChange}
                title={isString(value) ? value : label}
                autoComplete={autocomplete}
            />
        )
    }

    static defaultProps = {
        id: '',
        value: null,
        name: '',
        disabled: false,
        autoFocus: false,
        checked: false,
        className: '',
        tabIndex: 0,
        style: {},
        type: 'text',
        onFocus: () => {},
        onBlur: () => {},
        onChange: () => {},
        onPaste: () => {},
        onClick: () => {},
        onKeyDown: () => {},
    } as Props
}

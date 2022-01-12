import React from 'react'
import PropTypes from 'prop-types'
import { pure } from 'recompose'
import classNames from 'classnames'
import isObject from 'lodash/isObject'
import isArray from 'lodash/isArray'

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
class Input extends React.Component {
    constructor(props) {
        super(props)
        this.handleChange = this.handleChange.bind(this)
        this.setRef = this.handleChange.bind(this)
    }

    focus() {
        this.input.focus()
    }

    blur() {
        this.input.blur()
    }

    handleChange(e) {
        const { props, input } = this

        if (props.disabled) {
            return
        }
        const value = isObject(props.value) || isArray(props.value)
            ? props.value
            : e.target.value

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

    setRef(node) {
        this.input = node
    }

    render() {
        const {
            placeholder,
            length,
            className,
            style,
            id,
            name,
            type,
            disabled,
            inputRef,
            onChange,
            autoFocus,
            readOnly,
            value,
            onFocus,
            onBlur,
            onPaste,
            onClick,
            onKeyDown,
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
                /* eslint-disable-next-line jsx-a11y/no-autofocus */
                autoFocus={autoFocus}
                value={value}
                onPaste={onPaste}
                onFocus={onFocus}
                onBlur={onBlur}
                onClick={onClick}
                onKeyDown={onKeyDown}
                placeholder={placeholder}
                readOnly={readOnly}
                {...restProps}
                onChange={this.handleChange}
                title={value}
            />
        )
    }
}

Input.propTypes = {
    className: PropTypes.string,
    style: PropTypes.object,
    placeholder: PropTypes.string,
    length: PropTypes.string,
    id: PropTypes.string,
    name: PropTypes.string,
    type: PropTypes.string,
    disabled: PropTypes.bool,
    readOnly: PropTypes.bool,
    autoFocus: PropTypes.bool,
    value: PropTypes.any,
    onFocus: PropTypes.func,
    onBlur: PropTypes.func,
    onChange: PropTypes.func,
    onPaste: PropTypes.func,
    onClick: PropTypes.func,
    onKeyDown: PropTypes.func,
    inputRef: PropTypes.func,
}

Input.defaultProps = {
    disabled: false,
    autoFocus: false,
    className: '',
    style: {},
    type: 'text',
    onFocus: () => {},
    onBlur: () => {},
    onChange: () => {},
    onPaste: () => {},
    onClick: () => {},
    onKeyDown: () => {},
}

export default pure(Input)

import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

/**
 * Компонент - селект. Содержит {@link Option}
 * @reactProps {boolean} required - обязательность поля
 * @reactProps {boolean} autoFocus - с автофокусом селект или нет
 * @reactProps {boolean} disabled - задизейблен или нет селект
 * @reactProps {string|number} heightSize - css-класс, определяющий высоту. Варианты: 'form-control-lg', 'form-control-sm', ''
 * @reactProps {string|number} value - дефолтное значение
 * @reactProps {boolean} visible - флаг видимости
 * @reactProps {function} onChange - callback при изменение значения
 * @reactProps {node} children - элемент потомок компонента Select
 * @example
 *       <Select value={2} onChange={(v)=>{console.log(v)}}>
 *        { options.map((option, i)=> <Option
 *          key={i}
 *          value={option.value}
 *          label={option.label}/>
 *        ) }
 *        </Select>
 *
 */

export class Select extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            value: props.value,
        }

        this.onChange = this.onChange.bind(this)
    }

    // eslint-disable-next-line react/no-deprecated
    componentWillReceiveProps(props) {
        const { value: stateValue } = this.state
        const { value: propsValue } = props

        if (propsValue && propsValue !== stateValue) {
            this.setState({ value: propsValue })
        }
    }

    onChange({ target }) {
        const { onChange } = this.props

        this.setState({ value: target.value }, () => onChange(target.value))
    }

    render() {
        const {
            children,
            heightSize,
            visible,
            disabled,
            autoFocus,
            required,
            className,
        } = this.props
        const { value: stateValue } = this.state

        return (
            visible !== false && (
                <div className={classNames('form-group', 'n2o-select', className)}>
                    <select
                        className={classNames('form-control', heightSize)}
                        onChange={this.onChange}
                        value={stateValue}
                        /* eslint-disable-next-line jsx-a11y/no-autofocus */
                        autoFocus={autoFocus}
                        required={required}
                        disabled={disabled}
                    >
                        {children}
                    </select>
                </div>
            )
        )
    }
}

Select.propTypes = {
    className: PropTypes.string,
    children: PropTypes.node,
    onChange: PropTypes.func,
    required: PropTypes.bool,
    autoFocus: PropTypes.bool,
    disabled: PropTypes.bool,
    visible: PropTypes.bool,
    heightSize: PropTypes.oneOf(['input-sm', 'input-lg', '']),
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
}

Select.defaultProps = {
    autoFocus: false,
    disabled: false,
    required: false,
    visible: true,
    heightSize: '',
    onChange: () => {},
}

export default Select

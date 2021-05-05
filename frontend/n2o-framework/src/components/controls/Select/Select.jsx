import React from 'react'
import PropTypes from 'prop-types'
import cx from 'classnames'

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

class Select extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            value: props.value,
        }

        this.onChange = this.onChange.bind(this)
    }

    componentWillReceiveProps(props) {
        if (props.value && props.value !== this.state.value) {
            this.setState({ value: props.value })
        }
    }

    onChange({ target }) {
        this.setState({ value: target.value }, () => this.props.onChange(target.value))
    }

    /**
   * Базовый рендер
   **/
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

        return (
            visible !== false && (
                <div className={cx('form-group', 'n2o-select', className)}>
                    <select
                        className={cx('form-control', heightSize)}
                        onChange={this.onChange}
                        value={this.state.value}
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

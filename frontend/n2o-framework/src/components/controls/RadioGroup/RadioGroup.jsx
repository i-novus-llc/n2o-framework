import React from 'react'
import PropTypes from 'prop-types'
import cx from 'classnames'
import get from 'lodash/get'

import Radio from '../Radio/Radio'
import RadioButton from '../Radio/RadioButton'
import RadioN2O from '../Radio/RadioN2O'

/**
 * Компонент - группа радиобаттонов, содержит {@link Radio} как children
 * @reactProps {string|number|boolean} value - выбранное значение
 * @reactProps {function} onChange - вызывается при изменении значения
 * @reactProps {boolean} disabled - только для чтения
 * @reactProps {boolean} visible - флаг видимости
 * @reactProps {object} style - стили группы
 * @reactProps {string} className - класс группы
 * @reactProps {boolean} inline
 * @reactProps {node} children
 * @example
 * <RadioGroup onChange={(val)=> console.log(val)}>
 *  <Radio value="apple" label='Apple'/>
 *  <Radio value="orange" label='Orange'/>
 *  <Radio value="watermelon" label='Watermelon'/>
 * </RadioGroup>
 */

class RadioGroup extends React.Component {
    constructor(props) {
        super(props)
        this._onChange = this._onChange.bind(this)
    }

    _onChange(e) {
        const { onChange } = this.props
        const { value } = e.target

        onChange(value)
    }

    /**
   * Рендер
   */

    render() {
        const {
            children,
            value,
            visible,
            style,
            className,
            inline,
            valueFieldId,
        } = this.props

        const element = (child) => {
            const currentValue = get(value, valueFieldId)
            const childValue = get(child, `props.value.${valueFieldId}`)

            return React.cloneElement(child, {
                checked: currentValue && currentValue == childValue,
                disabled: this.props.disabled || child.props.disabled,
                onChange: this._onChange,
                inline: this.props.inline,
            })
        }

        const isRadioChild = (child) => {
            const checkboxTypes = ['Radio', 'RadioN2O', 'RadioButton']

            return child.type && checkboxTypes.includes(child.type.displayName)
        }

        const isBtn =
      children &&
      React.Children.map(children, child => child.type.displayName).includes(
          'RadioButton',
      )

        return (
            <>
                {visible !== false && (
                    <div
                        className={cx('n2o-radio-container', className, {
                            [`btn-group${inline ? '' : '-vertical'}`]: isBtn,
                            'btn-group-toggle': isBtn,
                            'n2o-radio-inline': inline,
                        })}
                        style={style}
                    >
                        {children &&
              React.Children.map(children, (child) => {
                  if (isRadioChild(child)) {
                      return element(child)
                  }
              })}
                    </div>
                )}
            </>
        )
    }
}

RadioGroup.propTypes = {
    /**
   * Значение
   */
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.object]),
    /**
   * Callback на изменение
   */
    onChange: PropTypes.func,
    /**
   * Флаг активности
   */
    disabled: PropTypes.bool,
    /**
   * Флаг видимости
   */
    visible: PropTypes.bool,
    children: PropTypes.node.isRequired,
    /**
   * Стили
   */
    style: PropTypes.object,
    /**
   * Класс
   */
    className: PropTypes.string,
    /**
   * Флаг рендера в одну строку
   */
    inline: PropTypes.bool,
    valueFieldId: PropTypes.string,
}

RadioGroup.defaultProps = {
    isBtnGroup: false,
    visible: true,
    inline: false,
    valueFieldId: 'id',
}

RadioGroup.defaultProps = {
    onChange: () => {},
}

export default RadioGroup

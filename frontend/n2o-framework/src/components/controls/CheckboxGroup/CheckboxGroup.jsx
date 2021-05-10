import React from 'react'
import PropTypes from 'prop-types'
import cx from 'classnames'
import xorBy from 'lodash/xorBy'
import some from 'lodash/some'
import isNull from 'lodash/isNull'

/**
 * Компонент - группа чекбоксов, содержит {@link Checkbox} как children
 * @reactProps {array} value - выбранное значение
 * @reactProps {function} onChange - вызывается при изменении значения
 * @reactProps {boolean} disabled - флаг блокировки
 * @reactProps {boolean} visible - флаг видимости
 * @reactProps {boolean} inline - флаг вывода в ряд
 * @reactProps {object} style - стили группы
 * @reactProps {string} className - класс группы
 * @reactProps {node} children - элемент потомок копонента CheckboxGroup
 * @example
 * <CheckboxGroup value={ ['apple', 'orange'] } onChange={(val)=> console.log(val)}>
 * <Checkbox value="apple" compileLabel='Apple' />
 * <Checkbox value="orange" compileLabel='Orange'/>
 * <Checkbox value="watermelon" compileLabel='Watermelon'/>
 * </CheckboxGroup>
 * */

class CheckboxGroup extends React.Component {
    constructor(props) {
        super(props)

        this._onChange = this._onChange.bind(this)
        this._onBlur = this._onBlur.bind(this)
    }

    /**
   * Обработчик изменения чекбокса
   * @param e - событие
   * @private
   */

    _onChange(e) {
        const { onChange, value, valueFieldId } = this.props
        const { value: newValue } = e.target

        onChange(xorBy(value, [newValue], valueFieldId))
    }

    _onBlur(e) {
        const { onBlur, value } = this.props

        onBlur(value)
    }

    _isIncludes(collection, object, key) {
        return some(collection, item => item[key] == object[key])
    }

    /**
   * Рендер
   */

    render() {
        const {
            children,
            visible,
            inline,
            style,
            className,
            value,
            valueFieldId,
        } = this.props
        const element = child => React.cloneElement(child, {
            checked:
          !isNull(value) &&
          value &&
          this._isIncludes(value, child.props.value, valueFieldId),
            disabled: this.props.disabled || child.props.disabled,
            onChange: this._onChange,
            onBlur: this._onBlur,
            onFocus: this.props.onFocus,
            inline: this.props.inline,
        })

        const isCheckboxChild = (child) => {
            const checkboxTypes = ['Checkbox', 'CheckboxN2O', 'CheckboxButton']

            return child.type && checkboxTypes.includes(child.type.displayName)
        }

        const isBtn =
      children &&
      React.Children.map(children, child => child.type.displayName).includes(
          'CheckboxButton',
      )

        return (
            <>
                {visible !== false && (
                    <div
                        className={cx('n2o-checkbox-group', className, {
                            [`btn-group${inline ? '' : '-vertical'}`]: isBtn,
                            'btn-group-toggle': isBtn,
                            'n2o-checkbox-inline': inline,
                        })}
                        style={style}
                    >
                        {children &&
              React.Children.map(children, (child) => {
                  if (isCheckboxChild(child)) {
                      return element(child)
                  }
              })}
                    </div>
                )}
            </>
        )
    }
}

CheckboxGroup.propTypes = {
    /**
   * Значение
   */
    value: PropTypes.any,
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
   * Флаг рендера в одну строку
   */
    inline: PropTypes.bool,
    /**
   * Стили
   */
    style: PropTypes.object,
    /**
   * Класс
   */
    className: PropTypes.string,
}

CheckboxGroup.defaultProps = {
    value: [],
    visible: true,
    onChange: () => {},
}

export default CheckboxGroup

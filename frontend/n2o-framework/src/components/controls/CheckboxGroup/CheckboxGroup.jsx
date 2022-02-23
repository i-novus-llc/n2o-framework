import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
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
    /**
     * Обработчик изменения чекбокса
     * @param e - событие
     * @private
     */
    onChange = (e) => {
        const { onChange, value, valueFieldId } = this.props
        const { value: newValue } = e.target

        onChange(xorBy(value, [newValue], valueFieldId))
    }

    onBlur = () => {
        const { onBlur, value } = this.props

        onBlur(value)
    }

    // eslint-disable-next-line eqeqeq
    isIncludes = (collection, object, key) => some(collection, item => item[key] == object[key])

    render() {
        const {
            children,
            visible,
            inline,
            style,
            className,
            value,
            valueFieldId,
            onFocus,
            disabled,
        } = this.props
        const element = child => React.cloneElement(child, {
            checked:
          !isNull(value) &&
          value &&
          this.isIncludes(value, child.props.value, valueFieldId),
            disabled: disabled || child.props.disabled,
            onChange: this.onChange,
            onBlur: this.onBlur,
            onFocus,
            inline,
        })

        const isCheckboxChild = (child) => {
            const checkboxTypes = ['Checkbox', 'CheckboxN2O', 'CheckboxButton']

            return child.type && checkboxTypes.includes(child.type.displayName)
        }

        const isBtn = children && React.Children.map(children, child => child.type.displayName).includes('CheckboxButton')

        return (
            <>
                {visible !== false && (
                    <div
                        className={classNames('n2o-checkbox-group', className, {
                            [`btn-group${inline ? '' : '-vertical'}`]: isBtn,
                            'btn-group-toggle': isBtn,
                            'n2o-checkbox-inline': inline,
                        })}
                        style={style}
                    >
                        {children &&
                        React.Children.toArray(children).filter(isCheckboxChild).map(element)
                        }
                    </div>
                )}
            </>
        )
    }
}

CheckboxGroup.propTypes = {
    value: PropTypes.any,
    /**
     * Callback на изменение
     */
    onChange: PropTypes.func,
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
    style: PropTypes.object,
    className: PropTypes.string,
    valueFieldId: PropTypes.string,
    onBlur: PropTypes.func,
    onFocus: PropTypes.func,
}

CheckboxGroup.defaultProps = {
    value: [],
    visible: true,
    onChange: () => {},
}

export default CheckboxGroup

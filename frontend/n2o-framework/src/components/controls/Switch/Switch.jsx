import React from 'react'
import PropTypes from 'prop-types'
import isNil from 'lodash/isNil'
import Switch from 'rc-switch'

class N2OSwitch extends React.Component {
    /**
     * базовый рендер
     * */
    render() {
        const { disabled, value, checked, onChange } = this.props

        return (
            <div className="n2o-switch-wrapper">
                <Switch
                    prefixCls="n2o-switch"
                    checked={isNil(checked) ? !!value : checked}
                    disabled={disabled}
                    onChange={onChange}
                />
            </div>
        )
    }
}

N2OSwitch.propTypes = {
    /**
     * Значение
     */
    value: PropTypes.oneOfType([
        PropTypes.string,
        PropTypes.number,
        PropTypes.bool,
    ]),
    /**
     * Checked контрола
     */
    checked: PropTypes.bool,
    /**
     * Флаг активности
     */
    disabled: PropTypes.bool,
    /**
   * Callback на изменение
   */
    onChange: PropTypes.func,
}

export default N2OSwitch

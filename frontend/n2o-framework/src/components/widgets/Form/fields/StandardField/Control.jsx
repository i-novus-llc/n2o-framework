import React from 'react'
import classNames from 'classnames'
import PropTypes from 'prop-types'

import { Factory } from '../../../../../core/factory/Factory'
import { CONTROLS } from '../../../../../core/factory/factoryLevels'

/**
 * Контрол поля формы
 * @param {object} props - пропсы
 * @param {component|string} component - компонент контрола
 * @param {component|string} className - css стили
 * @example
 * <Control component={Input} {...props}/>
 */
const Control = ({ component, className, ...props }) => {
    const newProps = { ...props, className }
    const classes = classNames('form-control', className)

    return typeof component !== 'string' ? (
        React.createElement(component, newProps)
    ) : (
        <Factory level={CONTROLS} src={component} {...props} className={classes} />
    )
}

Control.propTypes = {
    component: PropTypes.oneOfType([
        PropTypes.string,
        PropTypes.node,
        PropTypes.func,
    ]),
    style: PropTypes.object,
    className: PropTypes.string,
}

export default Control

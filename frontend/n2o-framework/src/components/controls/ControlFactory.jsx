/**
 * Created by emamoshin on 29.05.2017.
 */
import React from 'react'
import PropTypes from 'prop-types'

import Controls from './index'

/**
 * Фабрика контролов
 * @reactProps {string} src - src контрола
 * @example
 * <ControlFactory src='./path/to/control' className='my-control-class'/>
 */
function ControlFactory({ src, ...props }) {
    const { children } = props

    return React.createElement(Controls[src], props, children)
}

ControlFactory.propTypes = {
    src: PropTypes.string.isRequired,
    children: PropTypes.any,
}

export default ControlFactory

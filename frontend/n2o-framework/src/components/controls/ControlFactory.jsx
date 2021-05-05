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
class ControlFactory extends React.Component {
    /**
   * Базовый рендер
   */
    render() {
        const { src, ...props } = this.props

        return React.createElement(Controls[src], props, this.props.children)
    }
}

ControlFactory.propTypes = {
    src: PropTypes.string.isRequired,
}

export default ControlFactory

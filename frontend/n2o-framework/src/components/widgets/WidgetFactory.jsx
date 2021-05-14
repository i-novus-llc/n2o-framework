import React from 'react'
import PropTypes from 'prop-types'

import Widgets from './index'

const WidgetFactory = ({ src, children, ...props }) => React.createElement(Widgets[src], props, children)

WidgetFactory.propTypes = {
    src: PropTypes.string.isRequired,
    children: PropTypes.node,
}

export default WidgetFactory

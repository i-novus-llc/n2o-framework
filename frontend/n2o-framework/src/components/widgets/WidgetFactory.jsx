import React from 'react'
import PropTypes from 'prop-types'

import Widgets from './index'

class WidgetFactory extends React.Component {
    render() {
        const { src, ...props } = this.props
        return React.createElement(Widgets[src], props, this.props.children)
    }
}

WidgetFactory.propTypes = {
    src: PropTypes.string.isRequired,
}

export default WidgetFactory

import { createElement, Component } from 'react'
import PropTypes from 'prop-types'

import components from './components'

export class Placeholder extends Component {
    constructor(props) {
        super(props)
        this.stopRender = false
    }

    componentDidUpdate(prevProps) {
        const { loading } = this.props

        if (prevProps.loading && !loading && !this.stopRender) {
            this.stopRender = true
        }
    }

    render() {
        const { loading, type, children, once, ...rest } = this.props

        if (!once && loading) {
            return createElement(components[type], rest)
        }
        if (once && !this.stopRender && loading) {
            return createElement(components[type], rest)
        }

        return children || null
    }
}

Placeholder.propTypes = {
    /**
     * Флаг загрузки данных
     */
    loading: PropTypes.bool,
    /**
     * Тип плейсхолдера
     */
    type: PropTypes.string,
    children: PropTypes.node,
    /**
     * Флаг показа плейсхолдера единожды
     */
    once: PropTypes.bool,
}

Placeholder.defaultProps = {
    loading: false,
    type: 'list',
    once: false,
}

export default Placeholder

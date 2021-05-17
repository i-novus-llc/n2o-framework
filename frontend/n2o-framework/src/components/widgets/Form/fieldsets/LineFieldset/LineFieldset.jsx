import React from 'react'
import PropTypes from 'prop-types'
import { isBoolean } from 'lodash'

import CollapseFieldset from '../CollapseFieldset/CollapseFieldSet'
import TitleFieldset from '../TitleFieldset/TitleFieldset'
import evalExpression, {
    parseExpression,
} from '../../../../../utils/evalExpression'

class LineFieldset extends React.Component {
    constructor(props) {
        super(props)

        this.getCollapseProps = this.getCollapseProps.bind(this)
        this.getTitleProps = this.getTitleProps.bind(this)
    }

    getCollapseProps() {
        const {
            render,
            rows,
            type,
            label,
            expand,
            className,
            hasSeparator,
            description,
        } = this.props

        return {
            render,
            rows,
            type,
            label,
            expand,
            className,
            hasSeparator,
            description,
        }
    }

    getTitleProps() {
        const {
            render,
            rows,
            label: title,
            className,
            hasSeparator,
            description } = this.props

        return {
            render,
            rows,
            title,
            className,
            hasSeparator,
            description,
        }
    }

    resolveVisible() {
        const { visible, activeModel } = this.props
        const expression = parseExpression(visible)

        if (expression) {
            return evalExpression(expression, activeModel)
        }

        if (isBoolean(visible)) {
            return visible
        }

        return true
    }

    render() {
        const { collapsible } = this.props

        if (!this.resolveVisible()) {
            return null
        }

        if (collapsible) {
            return <CollapseFieldset {...this.getCollapseProps()} />
        }

        return <TitleFieldset {...this.getTitleProps()} />
    }
}

LineFieldset.defaultProps = {
    visible: true,
    hasSeparator: true,
}

LineFieldset.propTypes = {
    render: PropTypes.func,
    rows: PropTypes.array,
    label: PropTypes.string,
    collapsible: PropTypes.bool,
    type: PropTypes.string,
    expand: PropTypes.bool,
    className: PropTypes.string,
    hasSeparator: PropTypes.bool,
    visible: PropTypes.string,
    description: PropTypes.string,
    activeModel: PropTypes.object,
}

export default LineFieldset

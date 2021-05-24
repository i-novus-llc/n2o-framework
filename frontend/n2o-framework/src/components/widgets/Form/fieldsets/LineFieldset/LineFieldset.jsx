import React from 'react'
import PropTypes from 'prop-types'

import CollapseFieldset from '../CollapseFieldset/CollapseFieldSet'
import TitleFieldset from '../TitleFieldset/TitleFieldset'

class LineFieldset extends React.Component {
    constructor(props) {
        super(props)

        this.getCollapseProps = this.getCollapseProps.bind(this)
        this.getTitleProps = this.getTitleProps.bind(this)
    }

    getCollapseProps() {
        return {
            render: this.props.render,
            rows: this.props.rows,
            type: this.props.type,
            label: this.props.label,
            expand: this.props.expand,
            className: this.props.className,
            hasSeparator: this.props.hasSeparator,
        }
    }

    getTitleProps() {
        return {
            render: this.props.render,
            rows: this.props.rows,
            title: this.props.label,
            className: this.props.className,
            hasSeparator: this.props.hasSeparator,
        }
    }

    render() {
        const { collapsible } = this.props

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
}

export default LineFieldset

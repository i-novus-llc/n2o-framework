import React from 'react'
import PropTypes from 'prop-types'

import CollapseFieldset from '../CollapseFieldset/CollapseFieldSet'
import TitleFieldset from '../TitleFieldset/TitleFieldset'
import { withFieldsetHeader } from '../withFieldsetHeader'

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
            help,
            disabled,
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
            help,
            disabled,
        }
    }

    getTitleProps() {
        const {
            render,
            rows,
            label: title,
            className,
            hasSeparator,
            description,
            help,
            disabled,
        } = this.props

        return {
            render,
            rows,
            title,
            className,
            hasSeparator,
            description,
            help,
            disabled,
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
    disabled: false,
}

LineFieldset.propTypes = {
    render: PropTypes.func,
    rows: PropTypes.array,
    label: PropTypes.string,
    collapsible: PropTypes.bool,
    type: PropTypes.string,
    expand: PropTypes.bool,
    className: PropTypes.string,
    help: PropTypes.string,
    hasSeparator: PropTypes.bool,
    visible: PropTypes.string,
    disabled: PropTypes.bool,
    description: PropTypes.string,
    activeModel: PropTypes.object,
}

export default withFieldsetHeader(LineFieldset)

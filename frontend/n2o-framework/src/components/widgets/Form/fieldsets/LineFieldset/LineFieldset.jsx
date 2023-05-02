import React from 'react'
import PropTypes from 'prop-types'

import CollapseFieldset from '../CollapseFieldset/CollapseFieldSet'
import TitleFieldset from '../TitleFieldset/TitleFieldset'
import { withFieldsetHeader } from '../withFieldsetHeader'
import propsResolver from '../../../../../utils/propsResolver'

function LineFieldset(props) {
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
        collapsible,
        badge: badgeProps,
        activeModel,
    } = props

    const badge = propsResolver(badgeProps, activeModel)
    const commonProps = {
        render,
        rows,
        label,
        className,
        hasSeparator,
        description,
        help,
        disabled,
        badge,
    }

    if (collapsible) {
        return <CollapseFieldset {...commonProps} type={type} expand={expand} />
    }

    return <TitleFieldset {...commonProps} />
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
    badge: PropTypes.object,
}

export default withFieldsetHeader(LineFieldset)

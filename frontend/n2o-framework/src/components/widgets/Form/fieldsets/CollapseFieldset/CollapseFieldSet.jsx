import React from 'react'
import cn from 'classnames'
import PropTypes from 'prop-types'

import { Panel, Collapse } from '../../../../snippets/Collapse/Collapse'
import { withFieldsetHeader } from '../withFieldsetHeader'

function CollapseFieldSet({
    render,
    rows,
    type,
    label,
    expand,
    hasArrow,
    hasSeparator,
    description,
    help,
    disabled,
}) {
    const currentType = hasSeparator ? type : 'divider'

    return (
        <Collapse className={cn({ 'n2o-disabled': disabled })} defaultActiveKey={expand ? '0' : null}>
            <Panel
                header={label}
                description={description}
                type={currentType}
                showArrow={hasArrow}
                help={help}
                forceRender
            >
                {render(rows)}
            </Panel>
        </Collapse>
    )
}

CollapseFieldSet.propTypes = {
    rows: PropTypes.array,
    type: PropTypes.string,
    label: PropTypes.string,
    expand: PropTypes.bool,
    hasArrow: PropTypes.bool,
    hasSeparator: PropTypes.bool,
    render: PropTypes.func,
    description: PropTypes.string,
    help: PropTypes.string,
    disabled: PropTypes.bool,
}

CollapseFieldSet.defaultProps = {
    rows: [],
    hasArrow: true,
    hasSeparator: true,
    disabled: false,
}

export default withFieldsetHeader(CollapseFieldSet)

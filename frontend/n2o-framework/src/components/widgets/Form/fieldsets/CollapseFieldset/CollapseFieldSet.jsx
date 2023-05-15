import React from 'react'
import classNames from 'classnames'
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
    badge,
}) {
    const currentType = hasSeparator ? type : 'divider'

    return (
        <Collapse
            className={classNames({ 'n2o-disabled': disabled })}
            defaultActiveKey={expand ? '0' : null}
        >
            <Panel
                header={label}
                description={description}
                type={currentType}
                showArrow={hasArrow}
                help={help}
                badge={badge}
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
    badge: PropTypes.object,
}

CollapseFieldSet.defaultProps = {
    rows: [],
    hasArrow: true,
    hasSeparator: true,
    disabled: false,
}

export default withFieldsetHeader(CollapseFieldSet)

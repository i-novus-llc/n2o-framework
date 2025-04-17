import React from 'react'
import classNames from 'classnames'

import { Panel, Collapse } from '../../../../snippets/Collapse/Collapse'
import { withFieldsetHeader } from '../withFieldsetHeader'
import { type FieldsetProps } from '../types'
import { EMPTY_ARRAY } from '../../../../../utils/emptyTypes'

export type Props = Pick<FieldsetProps,
    'rows' | 'render' | 'disabled' | 'label' |
    'help' | 'badge' | 'description' | 'type' |
    'expand' | 'hasArrow' | 'hasSeparator'
>

function CollapseFieldSetBody({
    render,
    type,
    label,
    expand,
    description,
    help,
    badge,
    rows = EMPTY_ARRAY,
    hasArrow = true,
    hasSeparator = true,
    disabled = false,
}: Props) {
    const currentType = hasSeparator ? type : 'divider'

    return (
        <Collapse
            className={classNames({ 'n2o-disabled': disabled })}
            defaultActiveKey={expand ? '0' : null}
            collapsible
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

export const CollapseFieldSet = withFieldsetHeader(CollapseFieldSetBody)
export default CollapseFieldSet

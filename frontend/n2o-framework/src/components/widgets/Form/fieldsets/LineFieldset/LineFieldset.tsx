import React from 'react'

import { CollapseFieldSet } from '../CollapseFieldset/CollapseFieldSet'
import { TitleFieldset } from '../TitleFieldset/TitleFieldset'
import { withFieldsetHeader } from '../withFieldsetHeader'
import { type FieldsetProps } from '../types'

export type Props = Pick<FieldsetProps,
    'rows' | 'render' | 'disabled' | 'type' | 'label' |
    'expand' | 'className' | 'hasSeparator' | 'description' | 'help' | 'collapsible' | 'badge'
>

function LineFieldset({
    render,
    rows,
    type,
    label,
    expand,
    className,
    hasSeparator = true,
    description,
    help,
    disabled = false,
    collapsible,
    badge,
}: Props) {
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
        type,
    }

    if (collapsible) { return <CollapseFieldSet {...commonProps} type={type} expand={expand} /> }

    return <TitleFieldset {...commonProps} />
}

export default withFieldsetHeader(LineFieldset)

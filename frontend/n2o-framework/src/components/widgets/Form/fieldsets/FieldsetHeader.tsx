import classNames from 'classnames'
import React from 'react'

import { Badge, Props as BadgeProps } from '@i-novus/n2o-components/lib/display/Badge/Badge'

import HelpPopover from '../fields/StandardField/HelpPopover'
import Label from '../fields/StandardField/Label'

interface Props {
    visible: boolean
    badge?: BadgeProps
    needLabel: boolean
    needDescription: boolean
    description: string
    label: string
    help?: string
}

export function FieldsetHeader(props: Props) {
    const {
        visible,
        badge,
        label,
        needLabel,
        description,
        needDescription,
        help,
    } = props

    if (!visible) { return null }

    return (
        <div className={classNames('n2o-fieldset__label-container', { 'with-badge': !!badge })}>
            <Badge {...badge} visible={!!badge}>
                <Label visible={needLabel} className={classNames('n2o-fieldset__label', { 'with-description': description })} value={label} />
            </Badge>
            <HelpPopover help={(needLabel || needDescription) ? help : null} />
            <Label visible={needDescription} className="n2o-fieldset__description" value={description} />
        </div>
    )
}

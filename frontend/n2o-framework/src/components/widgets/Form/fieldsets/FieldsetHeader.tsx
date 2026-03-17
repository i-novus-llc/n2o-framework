import classNames from 'classnames'
import React, { useContext } from 'react'
import { Props as BadgeProps } from '@i-novus/n2o-components/lib/display/Badge/Badge'
import { HelpPopover, type HelpPopoverProps } from '@i-novus/n2o-components/lib/display/HelpPopover'

import Label from '../fields/StandardField/Label'
import { FactoryLevels } from '../../../../core/factory/factoryLevels'
import { FactoryContext } from '../../../../core/factory/context'

interface Props extends HelpPopoverProps {
    visible: boolean
    badge?: BadgeProps
    needLabel?: boolean
    needDescription?: boolean
    description?: string
    label: string
}

export function FieldsetHeader({
    visible,
    badge,
    label,
    needLabel,
    description,
    needDescription,
    help,
    helpTrigger,
    helpPlacement,
}: Props) {
    const { getComponent } = useContext(FactoryContext)

    if (!visible) { return null }

    const FactoryBadge = getComponent('Badge', FactoryLevels.SNIPPETS)

    return (
        <div className={classNames('n2o-fieldset__label-container', { 'with-badge': !!badge })}>
            {FactoryBadge && (
                <FactoryBadge {...badge} visible={!!badge}>
                    <Label visible={needLabel} className={classNames('n2o-fieldset__label', { 'with-description': description })} value={label} />
                </FactoryBadge>
            )}
            <HelpPopover
                help={(needLabel || needDescription) ? help : null}
                helpTrigger={helpTrigger}
                helpPlacement={helpPlacement}
            />
            <Label visible={needDescription} className="n2o-fieldset__description" value={description} />
        </div>
    )
}

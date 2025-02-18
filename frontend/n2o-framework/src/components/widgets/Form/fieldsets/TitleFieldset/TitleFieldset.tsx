import React, { useContext } from 'react'
import classNames from 'classnames'
import { HelpPopover } from '@i-novus/n2o-components/lib/display/HelpPopover'
import { Text } from '@i-novus/n2o-components/lib/Typography/Text'

import { withFieldsetHeader } from '../withFieldsetHeader'
import { DefaultFieldset } from '../DefaultFieldset'
import { FactoryContext } from '../../../../../core/factory/context'
import { FactoryLevels } from '../../../../../core/factory/factoryLevels'
import { Label } from '../../fields/StandardField/Label'
import { type FieldsetProps } from '../types'

export type Props = Pick<FieldsetProps,
    'rows' | 'render' | 'disabled' | 'label' |
    'className' | 'subTitle' | 'help' | 'badge' |
    'description' | 'type' | 'showLine'
>

function TitleFieldsetBody({
    render,
    rows,
    label,
    className,
    subTitle,
    help,
    badge,
    description,
    type,
    showLine = true,
    disabled = false,
}: Props) {
    const { getComponent } = useContext(FactoryContext)
    const FactoryBadge = getComponent('Badge', FactoryLevels.SNIPPETS)

    return (
        <DefaultFieldset disabled={disabled} className="title-fieldset">
            <div className={classNames('title-fieldset-header', { className })}>
                {FactoryBadge && (
                    <FactoryBadge {...badge} visible={!!badge}>
                        {label && <span className="title-fieldset-text"><Text>{label}</Text></span>}
                    </FactoryBadge>
                )}
                <HelpPopover help={help} />
                <Label
                    className="n2o-fieldset__description line-description"
                    value={description}
                    visible={type === 'line' && Boolean(description)}
                />
                {subTitle && <small className="text-muted title-fieldset-subtitle"><Text>{subTitle}</Text></small>}
                {showLine && <div className="title-fieldset-line" />}
            </div>
            {render(rows)}
        </DefaultFieldset>
    )
}

export const TitleFieldset = withFieldsetHeader(TitleFieldsetBody)
export default TitleFieldset

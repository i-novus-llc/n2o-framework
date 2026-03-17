import React, { CSSProperties } from 'react'
import { Label as BootstrapLabel } from 'reactstrap'
import classNames from 'classnames'
import { Text } from '@i-novus/n2o-components/lib/Typography/Text'
import { HelpPopover, type HelpPopoverProps } from '@i-novus/n2o-components/lib/display/HelpPopover'

import { EMPTY_OBJECT } from '../../../../../utils/emptyTypes'

import { Required } from './Required'

export interface Props extends HelpPopoverProps {
    id?: string
    value?: string | null
    required?: boolean
    style?: CSSProperties
    needStub?: boolean
    visible?: boolean
}

/**
 * Лейбел поля
 */

export const Label = ({
    id,
    value,
    required,
    help,
    helpTrigger,
    helpPlacement,
    className,
    style = EMPTY_OBJECT,
    needStub = false,
    visible = true,
}: Props) => {
    if (!visible || (!value && !needStub)) { return null }

    const newProps = {
        className: classNames('col-form-label', className),
        style: { display: 'inline-block', ...style },
    }

    if (React.isValidElement(value)) {
        return (
            <div id={id} className="n2o-field-label">
                {React.cloneElement(value, newProps)}
                <Required required={required} />
                <HelpPopover help={help} helpTrigger={helpTrigger} helpPlacement={helpPlacement} />
            </div>
        )
    }

    return (
        <BootstrapLabel id={id} className={classNames('n2o-field-label', className)}>
            <section style={style}>
                <span><Text>{value}</Text></span>
                <Required required={required} />
            </section>
            <HelpPopover help={help} helpTrigger={helpTrigger} helpPlacement={helpPlacement} />
        </BootstrapLabel>
    )
}

export default Label

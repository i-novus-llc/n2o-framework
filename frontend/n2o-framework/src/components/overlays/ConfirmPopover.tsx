import React from 'react'
import { PopoverConfirm } from '@i-novus/n2o-components/lib/display/PopoverConfirm'

import { ConfirmProps } from './Confirm'
import { useConfirmEffects } from './useConfirmEffects'

export function ConfirmPopover(props: ConfirmProps) {
    const { operation, id } = props

    const { onConfirm, onDeny } = useConfirmEffects(id, operation)

    return <PopoverConfirm {...props} onConfirm={onConfirm} onCancel={onDeny} onDeny={onDeny} isOpen />
}

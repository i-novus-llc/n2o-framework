import React from 'react'

import { PopoverConfirm } from '@i-novus/n2o-components/lib/display/PopoverConfirm'

import { ConfirmProps } from './Confirm'

export type ConfirmDialogProps = ConfirmProps & {
    size: 'lg' | 'sm'
    className?: string
    target: string
}

export function ConfirmPopover(props: ConfirmDialogProps) {
    const { onDeny } = props

    return (
        <PopoverConfirm
            {...props}
            isOpen
            onCancel={onDeny}
        />
    )
}

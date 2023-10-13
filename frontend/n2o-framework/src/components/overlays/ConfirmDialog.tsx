import React from 'react'
import { useTranslation } from 'react-i18next'
import { Button, ButtonGroup, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap'
import classNames from 'classnames'

import { ConfirmProps } from './Confirm'

export type ConfirmDialogProps = ConfirmProps & {
    size: 'lg' | 'sm'
    className?: string
}

export function ConfirmDialog({
    size,
    title,
    text,
    ok,
    cancel,
    onConfirm,
    onDeny,
    closeButton,
    className,
    reverseButtons = false,
}: ConfirmDialogProps) {
    const { t } = useTranslation()

    const { label: okLabel, color: okColor = 'primary' } = ok || {}
    const { label: cancelLabel, color: cancelColor } = cancel || {}

    const hasHeader = title || closeButton

    return (
        <Modal
            isOpen
            size={size}
            toggle={onDeny}
            modalClassName={classNames('n2o-modal-dialog', className, {
                'simple-modal-dialog': !title && !text,
            })}
        >
            {hasHeader && (
                <ModalHeader toggle={closeButton ? onDeny : undefined}>
                    {title}
                </ModalHeader>
            )}
            {text && <ModalBody>{text}</ModalBody>}
            <ModalFooter>
                <ButtonGroup className={classNames({ 'flex-row-reverse': reverseButtons })}>
                    <Button onClick={onConfirm} color={okColor}>
                        {okLabel || t('confirm')}
                    </Button>
                    <Button onClick={onDeny} color={cancelColor}>{cancelLabel || t('deny')}</Button>
                </ButtonGroup>
            </ModalFooter>
        </Modal>
    )
}

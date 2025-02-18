import React from 'react'
import get from 'lodash/get'
import { useStore } from 'react-redux'
import { useTranslation } from 'react-i18next'
import { Button, ButtonGroup, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap'
import classNames from 'classnames'
import { Text } from '@i-novus/n2o-components/lib/Typography/Text'

import { parseExpression } from '../../core/Expression/parse'
import evalExpression from '../../utils/evalExpression'

import { ConfirmProps } from './Confirm'
import { useConfirmEffects } from './useConfirmEffects'

export function ConfirmDialog({
    size,
    title,
    text: propsText,
    ok,
    cancel,
    closeButton,
    className,
    id,
    operation,
    datasource,
    model,
    reverseButtons = false,
}: ConfirmProps) {
    const { t } = useTranslation()
    const { onConfirm, onDeny } = useConfirmEffects(id, operation)
    const { getState } = useStore()
    const { models } = getState()

    const { label: okLabel, color: okColor = 'primary' } = ok || {}
    const { label: cancelLabel, color: cancelColor } = cancel || {}

    const hasHeader = title || closeButton
    const expression = parseExpression(propsText)
    const text = expression ? evalExpression(expression, get(models, `${model}.${datasource}`)) as string : propsText

    return (
        <Modal
            isOpen
            size={size}
            toggle={onDeny}
            modalClassName={classNames('n2o-modal-dialog', className, {
                'simple-modal-dialog': !title && !text,
            })}
        >
            {hasHeader && <ModalHeader toggle={closeButton ? onDeny : undefined}><Text>{title}</Text></ModalHeader>}
            {text && <ModalBody><Text>{text}</Text></ModalBody>}
            <ModalFooter>
                <ButtonGroup className={classNames({ 'flex-row-reverse': reverseButtons })}>
                    <Button onClick={onConfirm} color={okColor}>{okLabel || t('confirm')}</Button>
                    <Button onClick={onDeny} color={cancelColor}>{cancelLabel || t('deny')}</Button>
                </ButtonGroup>
            </ModalFooter>
        </Modal>
    )
}

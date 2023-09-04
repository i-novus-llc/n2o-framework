import React from 'react'
import { useTranslation } from 'react-i18next'
import { Popover, PopoverHeader, PopoverBody, Button, ButtonGroup } from 'reactstrap'
import classNames from 'classnames'

import { TBaseProps } from '../types'

type Props = TBaseProps & {
    title: string | Element, // заголовок PopoverConfirm
    children: string | Element,
    ok: { label: string, color: string, }, // конфиг кнопки подтверждения
    cancel: { label: string, color: string}, // конфиг кнопки отмены
    reverseButtons: boolean,
    isOpen: boolean,
    text: string, // текст сообщения
    target: string, // target element id popover
    onDeny(): void,
    onConfirm(): void,
    onCancel(): void,
}
/**
* @example
* <PopoverConfirm title="are you sure?" okLabel="ok" cancelLabel="no" />
*/

export function PopoverConfirm(props: Props) {
    const { t } = useTranslation()
    const {
        className,
        title,
        text,
        ok,
        cancel,
        target,
        onDeny,
        onConfirm,
        isOpen,
        reverseButtons = false,
        ...rest
    } = props

    const { label: okLabel = t('confirm'), color: okColor = 'primary' } = ok || {}
    const { label: cancelLabel = t('deny'), color: cancelColor } = cancel || {}

    return (
        <div className={classNames('n2o-popover', className)}>
            {isOpen && (
                <Popover {...rest} target={target} isOpen={isOpen}>
                    <>
                        <PopoverHeader>
                            <i className={classNames('fa fa-question-circle-o mr-1')} />
                            {title}
                        </PopoverHeader>
                        <PopoverBody>
                            <div className="mb-1">{text}</div>
                            <ButtonGroup className={classNames('d-flex justify-content-between', { 'flex-row-reverse': reverseButtons })}>
                                <Button color={cancelColor} className="btn-sm" onClick={onDeny}>
                                    {cancelLabel}
                                </Button>
                                <Button color={okColor} className="btn-sm" onClick={onConfirm}>
                                    {okLabel}
                                </Button>
                            </ButtonGroup>
                        </PopoverBody>
                    </>
                </Popover>
            )}
        </div>
    )
}

PopoverConfirm.defaultProps = {
    className: '',
    title: 'Вы уверены?',
    text: '',
    onConfirm: () => {},
    onCancel: () => {},
} as Props

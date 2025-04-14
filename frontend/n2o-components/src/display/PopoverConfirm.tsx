import React from 'react'
import { useTranslation } from 'react-i18next'
import { Popover, PopoverHeader, PopoverBody, Button, ButtonGroup } from 'reactstrap'
import classNames from 'classnames'
import onClickOutsideHOC from 'react-onclickoutside'

import { TBaseProps } from '../types'
import { Text } from '../Typography/Text'

type Props = TBaseProps & {
    // конфиг кнопки подтверждения
    cancel: { color: string, label: string }, // заголовок PopoverConfirm
    children?: string | Element,
    isOpen: boolean,
    ok: { color: string, label: string },
    onCancel(): void,
    onConfirm(): void,
    // target element id popover
    onDeny(): void,
    // конфиг кнопки отмены
    reverseButtons: boolean,
    // текст сообщения
    target: string,
    text: string,
    title: string | Element
}

interface PopoverProps extends Pick<
Props, 'isOpen' | 'target' | 'title' | 'text' | 'reverseButtons' | 'onConfirm' | 'onDeny'
> {
    okColor: string
    okLabel: string | object | Array<string | object> | undefined | null
    cancelColor: string
    cancelLabel: string | object | Array<string | object> | undefined | null
}

function EnhancedPopover({
    isOpen,
    target,
    title,
    text,
    reverseButtons,
    okColor,
    onConfirm,
    okLabel,
    cancelColor,
    onDeny,
    cancelLabel,
    ...rest
}: PopoverProps) {
    return (
        <Popover {...rest} target={target} isOpen={isOpen}>
            <PopoverHeader>
                <i className={classNames('fa fa-question-circle-o mr-1')} />
                <Text>{title}</Text>
            </PopoverHeader>
            <PopoverBody>
                <div className="mb-1"><Text>{text}</Text></div>
                <ButtonGroup className={classNames('d-flex justify-content-between', { 'flex-row-reverse': reverseButtons })}>
                    <Button color={okColor} className="btn-sm" onClick={onConfirm}>{okLabel}</Button>
                    <Button color={cancelColor} className="btn-sm" onClick={onDeny}>{cancelLabel}</Button>
                </ButtonGroup>
            </PopoverBody>
        </Popover>
    )
}

export const PopoverComponent = onClickOutsideHOC(EnhancedPopover)

const noop = () => {}

/**
 * @example
 * <PopoverConfirm title="are you sure?" okLabel="ok" cancelLabel="no" />
 */
export function PopoverConfirm({
    className = '',
    title ='Вы уверены?',
    text = '',
    ok,
    cancel,
    target,
    onDeny = noop,
    onConfirm = noop,
    isOpen,
    reverseButtons = false,
    ...rest
}: Props) {
    const { t } = useTranslation()

    const { label: okLabel = t('confirm'), color: okColor = 'primary' } = ok || {}
    const { label: cancelLabel = t('deny'), color: cancelColor } = cancel || {}
    const handleClickOutside = () => onDeny()

    return (
        <div className={classNames('n2o-popover', className)}>
            <PopoverComponent
                isOpen={isOpen}
                target={target}
                title={title}
                text={text}
                reverseButtons={reverseButtons}
                okColor={okColor}
                onConfirm={onConfirm}
                cancelColor={cancelColor}
                onDeny={onDeny}
                okLabel={okLabel}
                cancelLabel={cancelLabel}
                handleClickOutside={handleClickOutside}
                {...rest}
            />
        </div>
    )
}

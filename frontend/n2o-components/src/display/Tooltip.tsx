import React, { useState, cloneElement, useRef } from 'react'
import classNames from 'classnames'
import { Tooltip as Component, TooltipProps } from 'reactstrap'

import { Text } from '../Typography/Text'

export type Props = TooltipProps

export function Tooltip({ hint, className, placement, delay, trigger, children }: Props) {
    const tooltipRef = useRef(null)
    const [isOpen, setOpen] = useState(false)

    if (!children) { return null }

    const toggle = () => setOpen(!isOpen)
    const close = () => setOpen(false)

    let content = children

    // eslint-disable-next-line no-unsafe-optional-chaining
    const { ref } = (children as TooltipProps['TooltipChildren'])?.props?.children

    if (!ref && typeof children === 'object' && 'ref' in children) {
        // @ts-ignore ругается WebStorm билд ts с этим проходит
        content = cloneElement(children, { ref: tooltipRef, tooltipClose: close })
    }

    return (
        <>
            {content}
            <Component
                toggle={toggle}
                handleClickOutside={close}
                placement={placement}
                isOpen={isOpen}
                target={ref || tooltipRef}
                container={ref || tooltipRef}
                delay={delay}
                popperClassName={classNames('tooltip-container', className)}
                trigger={trigger}
                positionFixed
                flip
            >
                <Text>{hint}</Text>
            </Component>
        </>
    )
}

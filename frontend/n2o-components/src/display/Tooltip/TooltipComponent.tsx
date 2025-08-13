import React, { CSSProperties, ReactNode } from 'react'
import classNames from 'classnames'
import { FloatingPortal } from '@floating-ui/react'

import { Text } from '../../Typography/Text'

export enum TooltipTheme {
    DARK = 'DARK',
    LIGHT = 'light',
}

export interface TooltipContentProps {
    floatingRef(node: HTMLElement | null): void
    style: CSSProperties
    className?: string
    children: ReactNode
    visible: boolean
    portal?: boolean
    theme?: TooltipTheme
}

export const TooltipComponent = ({
    floatingRef,
    style,
    className,
    children,
    visible,
    portal = false,
    theme = TooltipTheme.DARK,
}: TooltipContentProps) => {
    if (!visible) { return null }

    const content = (
        <div
            ref={floatingRef}
            style={style}
            className={classNames(
                'tooltip-container',
                'tooltip-inner',
                'py-1 px-2 rounded text-sm',
                className,
                {
                    'bg-dark text-white': theme === TooltipTheme.DARK,
                    'bg-white text-dark border border-gray-200 shadow-sm': theme === TooltipTheme.LIGHT,
                },
            )}
        >
            <Text>{children}</Text>
        </div>
    )

    return portal ? <FloatingPortal>{content}</FloatingPortal> : content
}

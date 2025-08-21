import React, { ReactNode, useMemo } from 'react'

import { replaceWrappingSymbol } from './utils'

export type TextProps = {
    children?: ReactNode
}

export function Text({
    children,
}: TextProps) {
    const content = useMemo(() => {
        if (!children) { return null }

        return typeof children === 'string'
            ? replaceWrappingSymbol(children)
            : children
    }, [children])

    // eslint-disable-next-line react/jsx-no-useless-fragment
    return <>{content}</>
}

Text.displayName = '@n2o-components/Typography/Text'

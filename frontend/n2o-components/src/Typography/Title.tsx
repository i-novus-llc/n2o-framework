import React, { HTMLAttributes, ReactNode } from 'react'

import { Text } from './Text'

type Level = 1 | 2 | 3 | 4 | 5 | 6

type Props = {
    level: Level | `${Level}`
    children?: ReactNode
} & HTMLAttributes<HTMLElement>

export function Title({ level, children, ...rest }: Props) {
    if (!children) { return false }

    const content = <Text>{children}</Text>
    const tag = `h${level}`

    return React.createElement(tag, {
        ...rest,
    }, content)
}

Title.displayName = '@n2o-components/Typography/Title'

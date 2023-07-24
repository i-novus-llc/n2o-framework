import React from 'react'

import { TBaseProps } from '../../types'

import { Base } from './Base'

type Props = TBaseProps & {
    level: string | number
}

export function Title({ level, ...rest }: Props) {
    const tag = `h${level}`

    return <Base tag={tag} {...rest} />
}

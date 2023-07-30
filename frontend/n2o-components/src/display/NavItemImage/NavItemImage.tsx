import React from 'react'
import classNames from 'classnames'

import { TBaseProps } from '../../types'

type Props = TBaseProps & {
    imageShape?: string,
    imageSrc?: string,
    title: string,
}

export const NavItemImage = ({
    imageSrc,
    imageShape,
    title,
}: Props) => {
    if (!imageSrc) {
        return null
    }

    return (
        <img
            className={classNames(`mr-2 n2o-nav-image ${{
                circle: 'rounded-circle',
                rounded: 'rounded',
            }[imageShape || ''] || ''}`)}
            src={imageSrc}
            alt={title}
            width="18"
        />
    )
}

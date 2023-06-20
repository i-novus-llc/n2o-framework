import React from 'react'
import classNames from 'classnames'

interface INavItemImage {
    imageSrc?: string
    imageShape?: string
    title: string
}

export const NavItemImage = (props: INavItemImage) => {
    const {
        imageSrc,
        imageShape = 'square',
        title,
    } = props

    if (!imageSrc) {
        return null
    }

    return (
        <img
            className={classNames(`mr-2 n2o-nav-image ${{ circle: 'rounded-circle', rounded: 'rounded' }[imageShape] || ''}`)}
            src={imageSrc}
            alt={title}
            width="18"
        />
    )
}

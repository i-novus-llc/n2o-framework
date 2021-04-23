import React from 'react'
import isUndefined from 'lodash/isUndefined'
import get from 'lodash/get'
import isEmpty from 'lodash/isEmpty'

export const getCurrentLabel = (imgError, label) => (!isEmpty(imgError) ? get(imgError, 'message') : label)

export const createCustomSizes = (width, height, iconSize, unit = 'px') => {
    const hasCustomSize = !isUndefined(width) || !isUndefined(height)

    const getSize = (size, altSize) => (!isUndefined(size) ? size : altSize)

    const currentWidth = getSize(width, height)
    const currentHeight = getSize(height, width)

    const createSize = size => `${size}${unit}`

    const createIconSize = isUndefined(iconSize)
        ? createSize((currentWidth / 3) * 2.5)
        : createSize(iconSize)

    return hasCustomSize
        ? {
            uploader: {
                width: `${createSize(currentWidth)}`,
                height: `${createSize(currentHeight)}`,
                maxWidth: `${createSize(currentWidth)}`,
                maxHeight: `${createSize(currentHeight)}`,
                minHeight: 'auto',
                minWidth: 'auto',
            },
            icon: { fontSize: `${createIconSize}` },
        }
        : {}
}

export const getSize = (sizes, path) => {
    const style = get(sizes, path)
    return !isUndefined(style) ? style : {}
}

export function defaultDropZone(icon, label, size) {
    const customIconSize = getSize(size, 'icon')
    return (
        <>
            <div style={customIconSize} className={icon} />
            {label}
        </>
    )
}

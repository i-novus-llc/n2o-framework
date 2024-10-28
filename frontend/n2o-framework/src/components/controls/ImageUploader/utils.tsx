import React, { CSSProperties } from 'react'
import isUndefined from 'lodash/isUndefined'
import get from 'lodash/get'
import isEmpty from 'lodash/isEmpty'

export type ImgError = { message?: string }

export const getCurrentLabel = (imgError: ImgError, label: string) => {
    return !isEmpty(imgError) ? get(imgError, 'message') : label
}

export const createCustomSizes = (
    width?: number,
    height?: number,
    iconSize?: number,
    unit = 'px',
): Record<string, CSSProperties> => {
    const hasCustomSize = !isUndefined(width) || !isUndefined(height)

    const getSize = (size?: number, altSize?: number): number => (!isUndefined(size) ? size : altSize || 0)

    const currentWidth = getSize(width, height)
    const currentHeight = getSize(height, width)

    const createSize = (size: number): string => `${size}${unit}`

    const createIconSize = isUndefined(iconSize)
        ? createSize((currentWidth / 3) * 2.5)
        : createSize(iconSize)

    return hasCustomSize
        ? {
            uploader: {
                width: createSize(currentWidth),
                height: createSize(currentHeight),
                maxWidth: createSize(currentWidth),
                maxHeight: createSize(currentHeight),
                minHeight: 'auto',
                minWidth: 'auto',
            },
            icon: { fontSize: createIconSize },
        }
        : {}
}

export const getSize = (sizes: Record<string, CSSProperties>, path: string) => {
    const style = get(sizes, path)

    return !isUndefined(style) ? style : {}
}

export function defaultDropZone(icon: string, label: string, size: Record<string, CSSProperties>) {
    return (
        <>
            <div style={getSize(size, 'icon')} className={icon} />
            {label}
        </>
    )
}

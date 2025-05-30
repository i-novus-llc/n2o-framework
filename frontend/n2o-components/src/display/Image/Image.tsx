import React from 'react'
import classNames from 'classnames'

import { TBaseProps } from '../../types'
import { EMPTY_OBJECT } from '../../utils/emptyTypes'

import { ImageInfo } from './ImageInfo'

export enum TextPosition {
    bottom = 'bottom',
    left = 'left',
    right = 'right',
    top = 'top',
}

export enum Shape {
    circle = 'circle',
    rounded = 'rounded',
    square = 'square',
}

export type Props = TBaseProps & {
    description?: string,
    height?: number,
    id: string,
    onClick?(): void,
    shape: Shape,
    src: string,
    style?: object,
    textPosition: TextPosition,
    title?: string,
    width: string
}

export function Image({
    id,
    src = '',
    title,
    description,
    textPosition = TextPosition.right,
    width,
    height,
    shape = Shape.rounded,
    visible = true,
    style: propsStyle = EMPTY_OBJECT,
    className,
    onClick,
}: Props) {
    const style = width ? { maxWidth: width } : {}
    const imageStyle = height || width ? { height, width } : {}
    const hasInfo = title || description

    if (!visible) { return null }

    return (
        <div id={id} className={classNames('n2o-image n2o-snippet', className)}>
            <div
                className={classNames('n2o-image__content', {
                    [textPosition]: textPosition,
                })}
            >
                <section
                    className={classNames('n2o-image__image-container', { [shape]: shape })}
                    style={{ ...style, ...propsStyle }}
                >
                    <div
                        onClick={onClick}
                    >
                        <img
                            className="n2o-image__image"
                            src={src}
                            alt="img"
                            style={imageStyle}
                        />
                    </div>
                </section>
                {hasInfo && <ImageInfo title={title} description={description} />}
            </div>
        </div>
    )
}

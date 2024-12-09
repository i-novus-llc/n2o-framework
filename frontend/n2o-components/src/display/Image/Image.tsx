import React from 'react'
import classNames from 'classnames'

import { TBaseProps } from '../../types'

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
    description: string,
    height: number,
    id: string,
    onClick(): void,
    shape: Shape,
    src: string,
    style: object,
    textPosition: TextPosition,
    title: string,
    width: string
}

export function Image({
    id,
    src,
    title,
    description,
    textPosition,
    width,
    height,
    shape,
    visible,
    style: propsStyle,
    className,
    onClick,
}: Props) {
    const style = width ? { maxWidth: width } : {}
    const imageStyle = height || width ? { height, width } : {}
    const hasInfo = title || description

    if (!visible) {
        return null
    }

    // eslint-disable-next-line consistent-return
    return (
        <div id={id} className={classNames('n2o-image n2o-snippet', className)}>
            <div
                className={classNames('n2o-image__content', {
                    [textPosition]: textPosition,
                })}
            >
                <section
                    className={classNames('n2o-image__image-container', {
                        [shape]: shape,
                    })}
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

Image.defaultProps = {
    src: '',
    textPosition: 'right',
    shape: 'rounded',
    visible: true,
    style: {},
} as Props

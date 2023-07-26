import React from 'react'
import classNames from 'classnames'

import { TBaseProps } from '../../types'

import { ImageInfo } from './ImageInfo'

enum TextPosition {
    top = 'top',
    left = 'left',
    bottom = 'bottom',
    right = 'right'
}

enum Shape {
    square = 'square',
    circle = 'circle',
    rounded = 'rounded'
}

type Props = TBaseProps & {
    id: string,
    height: number,
    src: string,
    title: string,
    description: string,
    width: string,
    textPosition: TextPosition,
    shape: Shape,
    style: object,
    onClick(): void,
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
                    {/* eslint-disable-next-line jsx-a11y/img-redundant-alt,jsx-a11y/no-noninteractive-element-interactions */}
                    <img
                        className="n2o-image__image"
                        src={src}
                        onClick={onClick}
                        alt="image error"
                        style={imageStyle}
                    />
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

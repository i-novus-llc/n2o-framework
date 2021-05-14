import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

import { ImageInfo } from './ImageInfo'

/**
 * Компонент Изображение
 * @reactProps {string} id - id
 * @reactProps {string} src - src изображения
 * @reactProps {string} title - title рядом с img
 * @reactProps {string} description - description рядом с img
 * @reactProps {string} textPosition - позиция текста рядом с img (top || left || bottom || right = default )
 * @reactProps {string} - width - кастом ширина
 * @reactProps {string} shape - форма маски img (square || circle || rounded = default)
 * @reactProps {boolean} visible - флаг видимости сниппета
 */

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
    onClick,
    className,
}) {
    const style = width ? { maxWidth: width } : {}
    const imageStyle = height || width ? { height, maxWidth: width } : {}
    const hasInfo = title || description

    if (!visible) {
        return
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

Image.propTypes = {
    onClick: PropTypes.func,
    height: PropTypes.number,
    id: PropTypes.string,
    src: PropTypes.string,
    title: PropTypes.string,
    description: PropTypes.string,
    width: PropTypes.string,
    textPosition: PropTypes.oneOf(['top', 'left', 'bottom', 'right']),
    shape: PropTypes.oneOf(['square', 'circle', 'rounded']),
    visible: PropTypes.bool,
    className: PropTypes.string,
    style: PropTypes.object,
}

Image.defaultProps = {
    src: '',
    textPosition: 'right',
    shape: 'rounded',
    visible: true,
    style: {},
}

export default Image

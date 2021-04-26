import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

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

function Image({
    id,
    src,
    title,
    description,
    textPosition,
    width,
    shape,
    visible,
    style: propsStyle,
    onClick,
    className,
}) {
    const style = width ? { maxWidth: width } : {}

    if (!visible) {
        return
    }

    return (
        <div id={id} className={classNames('n2o-image', className)}>
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
                    <img
                        className="n2o-image__image"
                        src={src}
                        onClick={onClick}
                        alt="image error"
                    />
                </section>
                <section className="n2o-image__info">
                    {title && <h4 className="n2o-image__info_label">{title}</h4>}
                    {description && (
                        <p className="n2o-image__info_description">{description}</p>
                    )}
                </section>
            </div>
        </div>
    )
}

Image.propTypes = {
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

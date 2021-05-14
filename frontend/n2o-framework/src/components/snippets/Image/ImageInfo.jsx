import React from 'react'
import PropTypes from 'prop-types'

/**
 * ImageInfo - блок информации
 * @reactProps {string} title
 * @reactProps {string} description
 */

export function ImageInfo({ title, description }) {
    return (
        <section className="n2o-image__info">
            {title && <h4 className="n2o-image__info_label">{title}</h4>}
            {description && (
                <p className="n2o-image__info_description">{description}</p>
            )}
        </section>
    )
}

ImageInfo.propTypes = {
    /**
     * title
     */
    title: PropTypes.string,
    /**
     * description
     */
    description: PropTypes.string,
}

export default ImageInfo

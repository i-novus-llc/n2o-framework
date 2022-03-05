import React from 'react'
import classNames from 'classnames'
import PropTypes from 'prop-types'

export const NavItemImage = ({
    imageSrc,
    imageShape,
    title,
}) => {
    if (!imageSrc) {
        return null
    }

    return (
        <img
            className={classNames(`mr-2 n2o-nav-image ${{
                circle: 'rounded-circle',
                rounded: 'rounded',
            }[imageShape] || ''}`)}
            src={imageSrc}
            alt={title}
            width="18"
        />
    )
}

NavItemImage.propTypes = {
    imageSrc: PropTypes.string,
    imageShape: PropTypes.string,
    title: PropTypes.string,
}

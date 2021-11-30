import React from 'react'
import cx from 'classnames'
import PropTypes from 'prop-types'

export const NavItemImage = ({
    imageSrc,
    imageShape,
    title,
}) => (
    <img
        className={cx(`mr-2 ${{
            circle: 'rounded-circle',
            rounded: 'rounded',
        }[imageShape] || ''}`)}
        src={imageSrc}
        alt={title}
        width="18"
    />
)

NavItemImage.propTypes = {
    imageSrc: PropTypes.string,
    imageShape: PropTypes.string,
    title: PropTypes.string,
}

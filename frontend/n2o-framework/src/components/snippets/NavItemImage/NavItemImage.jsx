import React from 'react'
import cx from 'classnames'
import PropTypes from 'prop-types'

export const NavItemImage = ({
    imageSrc,
    title,
}) => <img className={cx('mr-2 rounded-circle')} src={imageSrc} alt={title} />

NavItemImage.propTypes = {
    imageSrc: PropTypes.string,
    title: PropTypes.string,
}

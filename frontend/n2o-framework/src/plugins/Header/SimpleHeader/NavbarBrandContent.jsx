import React from 'react'
import PropTypes from 'prop-types'
/**
 * Рендер бренда и лого
 * @param props - пропсы
 * @param {string | element} props.brandImage - брэнд(изображение)
 */
export const NavbarBrandContent = ({ brandImage }) => {
    const img =
    brandImage && typeof brandImage === 'string' ? (
        <img
            src={brandImage}
            className="n2o-brand__image d-inline-block align-top"
            alt="brand"
        />
    ) : (
        brandImage
    )

    return <>{img}</>
}

NavbarBrandContent.propTypes = {
    brandImage: PropTypes.oneOfType([PropTypes.string, PropTypes.element]),
}

export default NavbarBrandContent

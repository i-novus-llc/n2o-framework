import React from 'react';
import PropTypes from 'prop-types';
/**
 * Рендер бренда и лого
 * @param props - пропсы
 * @param {string | element} props.brand  - брэнд(текст)
 * @param {string | element} props.brandImage - брэнд(изображение)
 */
const NavbarBrandContent = ({ brand, brandImage }) => {
  const img =
    brandImage && typeof brandImage === 'string' ? (
      <img
        src={brandImage}
        style={{
          marginRight: '5px',
          display: 'inline-block',
          alignSelf: 'center',
          margin: 'auto',
          maxWidth: 40,
          maxHeight: 40,
        }}
        className="d-inline-block align-top"
      />
    ) : (
      brandImage
    );

  return (
    <React.Fragment>
      {img}
      {brand}
    </React.Fragment>
  );
};

NavbarBrandContent.propTypes = {
  brand: PropTypes.oneOfType([PropTypes.string, PropTypes.element]),
  brandImage: PropTypes.oneOfType([PropTypes.string, PropTypes.element]),
};

export default NavbarBrandContent;

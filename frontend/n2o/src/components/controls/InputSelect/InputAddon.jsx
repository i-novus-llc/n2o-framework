import React from 'react';
import PropTypes from 'prop-types';

/**
 * InputAddon - дополнение с иконкой/картинкой для инпута {@Link InputSelectGroup}
 * @reactProps {object} item - выбранный элемент
 * @reactProps {string} iconFieldId - поле иконки
 * @reactProps {string} imageFieldId - поле картинки
 */

function InputAddon({ item, iconFieldId, imageFieldId, setRef }) {
  return (
    <span className="selected-item selected-item--single" ref={setRef}>
      {iconFieldId && item[iconFieldId] && <i className={item[iconFieldId]} />}
      {imageFieldId && item[imageFieldId] && <img src={item[imageFieldId]} />}
    </span>
  );
}

InputAddon.propTypes = {
  item: PropTypes.object.isRequired,
  iconFieldId: PropTypes.string,
  imageFieldId: PropTypes.string,
};

export default InputAddon;

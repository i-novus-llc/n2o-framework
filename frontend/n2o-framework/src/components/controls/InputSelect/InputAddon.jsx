import React from 'react'
import PropTypes from 'prop-types'

/**
 * InputAddon - дополнение с иконкой/картинкой для инпута {@Link InputSelectGroup}
 * @reactProps {object} item - выбранный элемент
 * @reactProps {string} iconFieldId - поле иконки
 * @reactProps {string} imageFieldId - поле картинки
 * @return {null}
 */

function InputAddon({ item, iconFieldId, imageFieldId, setRef }) {
    return item[iconFieldId] || item[imageFieldId] ? (
        <span className="selected-item selected-item--single" ref={setRef}>
            {iconFieldId && item[iconFieldId] && <i className={item[iconFieldId]} />}
            {/* eslint-disable-next-line jsx-a11y/alt-text */}
            {imageFieldId && item[imageFieldId] && <img src={item[imageFieldId]} />}
        </span>
    ) : null
}

InputAddon.propTypes = {
    item: PropTypes.object.isRequired,
    iconFieldId: PropTypes.string,
    imageFieldId: PropTypes.string,
    setRef: PropTypes.func,
}

export default InputAddon

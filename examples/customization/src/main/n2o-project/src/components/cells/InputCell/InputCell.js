import React from 'react';
import PropTypes from 'prop-types';

/**
 * Кастомый InputCell
 * @reactProps {string} id - id поля
 * @reactProps {object.{
 *     {string} value - значение
 *     {boolean} disabled - флаг активностт
 *     {string} type - тип поля
 *     {boolean} autoFocus - флаг включения автофокуса
 *     {object} style - стили InputCell
 * }} model - параметры InputCell
 */
class InputCell extends React.Component {
    render() {
        const { id, model } = this.props;
        return (
            <div>
                <input style={model[id].style} type={model[id].type} value={model[id].value} disabled={model[id].disabled} autoFocus={model[id].autoFocus} />
            </div>
        );
    }
}

InputCell.propTypes = {
    id: PropTypes.string,
    model: PropTypes.shape({
        value: PropTypes.string,
        disabled: PropTypes.bool,
        type: PropTypes.string,
        autoFocus: PropTypes.bool,
        style: PropTypes.object
    })
};

export default InputCell;
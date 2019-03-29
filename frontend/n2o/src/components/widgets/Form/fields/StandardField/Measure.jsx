import React from 'react';
import PropTypes from 'prop-types';

/**
 * Компонент-размерность
 * @param {string} value - размерность. Например: км, л, штук...
 * @param {object} props - остальные пропсы
 * @example
 * <Measure value="м"/>
 */
const Measure = ({ value, ...props }) =>
  value ? (
    <span style={{ marginLeft: 5 }} {...props}>
      {value}
    </span>
  ) : null;

Measure.propTypes = {
  value: PropTypes.string
};

export default Measure;

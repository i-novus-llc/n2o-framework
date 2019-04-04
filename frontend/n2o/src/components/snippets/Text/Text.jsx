/**
 * Created by emamoshin on 03.10.2017.
 */
import React from 'react';
import PropTypes from 'prop-types';
import parseFormatter from '../../../utils/parseFormatter';

/**
 * Компонент, который отображает текст
 * @reactProps {string} text - текст
 * @reactProps {string} format - формат времени
 * @reactProps {string} className - класс для элемета Text
 */
function Text({ text, format, ...rest }) {
  return <span {...rest}>{parseFormatter(text, format)}</span>;
}

Text.propTypes = {
  text: PropTypes.string,
  className: PropTypes.string,
  format: PropTypes.string,
};

export default Text;

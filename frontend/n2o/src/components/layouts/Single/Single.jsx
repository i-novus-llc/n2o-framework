import React from 'react';
import PropTypes from 'prop-types';
import Place from '../Place';
import layoutPlaceResolver from '../LayoutPlaceResolver';

/**
 * Layout виджета; Places: single
 * @param {object} props - пропсы
 * @param {string} props.className - css-класс
 * @example
 * <Single>
 *      <Section place="single">
 *         <div>N2O is awesome</div>
 *     </Section>
 *  </Single>
 */
const Single = ({ className }) => {
  return (
    <div className={`layout ${className}`}>
      <Place name="single" />
    </div>
  );
};

Single.propTypes = {
  className: PropTypes.string
};

export default layoutPlaceResolver(Single);

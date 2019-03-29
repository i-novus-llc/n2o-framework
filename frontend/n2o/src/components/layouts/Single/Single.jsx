import React from 'react';
import PropTypes from 'prop-types';
import cn from 'classnames';
import Place from '../Place';
import layoutPlaceResolver from '../LayoutPlaceResolver';

/**
 * Layout виджета; Places: single
 * @param {object} props - пропсы
 * @param {string} props.className - css-класс
 * @param {string} props.style - стили layout
 * @example
 * <Single>
 *      <Section place="single">
 *         <div>N2O is awesome</div>
 *     </Section>
 *  </Single>
 */
const Single = ({ className, style }) => {
  return (
    <div className={cn('layout', className)} style={style}>
      <Place name="single" />
    </div>
  );
};

Single.propTypes = {
  className: PropTypes.string,
  style: PropTypes.object
};

Single.defaultProps = {
  style: {}
};

export default layoutPlaceResolver(Single);

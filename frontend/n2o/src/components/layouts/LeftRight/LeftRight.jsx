import React from 'react';
import PropTypes from 'prop-types';
import Place from '../Place';
import layoutPlaceResolver from '../LayoutPlaceResolver';

/**
 * Layout виджета; Places: left, right
 * @param {object} props - пропсы
 * @param {string} props.className - css-класс
 * @example
 * <LeftRight>
 *     <Section place="left">
 *         <div>N2O is awesome</div>
 *     </Section>
 *     <Section place="right">
 *         <div>N2O is awesome</div>
 *     </Section>
 * </LeftRight>
 */
let LeftRight = ({ className }) => {
  return (
    <div className={`layout row ${className ? className : ''}`}>
      <div className="col-md-6">
        <Place name="left" />
      </div>
      <div className="col-md-6">
        <Place name="right" />
      </div>
    </div>
  );
};

LeftRight.propTypes = {
  className: PropTypes.string
};

export default layoutPlaceResolver(LeftRight);

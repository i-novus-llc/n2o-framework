import React from 'react';
import PropTypes from 'prop-types';
import Place from '../Place';
import layoutPlaceResolver from '../LayoutPlaceResolver';

/**
 * Layout виджета; Places: left, top, bottom
 * @param {object} props - пропсы
 * @param {string} props.className - css-класс
 * @example
 * <LeftTopBottom>
 *      <Section place="left">
 *         <div>N2O is awesome</div>
 *     </Section>
 *     <Section place="bottom">
 *         <div>N2O is awesome</div>
 *     </Section>
 *     <Section place="top">
 *         <div>N2O is awesome</div>
 *     </Section>
 * <LeftTopBottom/>
 */
let LeftTopBottom = ({ className }) => {
  return (
    <div className={`layout row ${className}`}>
      <div className="col-md-6">
        <Place name="left" />
      </div>
      <div className="col-md-6">
        <Place name="top" />
        <Place name="bottom" />
      </div>
    </div>
  );
};

LeftTopBottom.propTypes = {
  className: PropTypes.string
};

LeftTopBottom = layoutPlaceResolver(LeftTopBottom);
export default LeftTopBottom;

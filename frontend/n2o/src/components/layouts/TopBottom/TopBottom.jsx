import React from 'react';
import PropTypes from 'prop-types';
import Place from '../Place';
import layoutPlaceResolver from '../LayoutPlaceResolver';
import cx from 'classnames';

/**
 *Вид Layout, который состоит из 2 Place: top и bottom
 * @param {object} props - пропсы
 * @param {string} props.className - css-класс
 * @example
 * <TopBottom>
 *      <Section place="top">
 *         <div>N2O is awesome</div>
 *     </Section>
 *      <Section place="bottom">
 *         <div>N2O is awesome</div>
 *     </Section>
 *  </TopBottom>
 */
const TopBottom = ({ className, style }) => {
  return (
    <div className={cx('layout', className)} style={style}>
      <Place name="top" />
      <Place name="bottom" />
    </div>
  );
};

TopBottom.propTypes = {
  className: PropTypes.string,
  style: PropTypes.object,
};

TopBottom.defaultProps = {
  style: {},
};

export default layoutPlaceResolver(TopBottom);

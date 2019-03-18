import React from 'react';
import cn from 'classnames';
import { Col } from 'reactstrap';
import PropTypes from 'prop-types';
import Place from '../Place';
import layoutPlaceResolver from '../LayoutPlaceResolver';

/**
 * Layout виджета; Places: left, top, bottom
 * @param {object} props - пропсы
 * @param {string} props.className - css-класс
 *  @param {number} props.leftSize - размер левой колонки (указывается в колонках бутстрапа (1, 2, 3 ... 12))
 * @param {number} props.rightSize - размер правой колонки
 * @param {object} props.style - стили layout
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
let LeftTopBottom = ({ className, leftSize, rightSize, style }) => {
  return (
    <div className={cn('layout row', className)} style={style}>
      <Col md={leftSize}>
        <Place name="left" />
      </Col>
      <Col md={rightSize}>
        <Place name="top" />
        <Place name="bottom" />
      </Col>
    </div>
  );
};

LeftTopBottom.propTypes = {
  className: PropTypes.string,
  leftSize: PropTypes.number,
  rightSize: PropTypes.number,
  style: PropTypes.object
};

LeftTopBottom.defaultProps = {
  leftSize: 6,
  rightSize: 6,
  style: {}
};

LeftTopBottom = layoutPlaceResolver(LeftTopBottom);
export default LeftTopBottom;

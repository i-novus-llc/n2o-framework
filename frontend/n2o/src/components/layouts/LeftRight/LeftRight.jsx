import React from 'react';
import cn from 'classnames';
import PropTypes from 'prop-types';
import Place from '../Place';
import { Col } from 'reactstrap';
import layoutPlaceResolver from '../LayoutPlaceResolver';

/**
 * Layout виджета; Places: left, right
 * @param {object} props - пропсы
 * @param {string} props.className - css-класс
 * @param {number} props.leftSize - размер левой колонки (указывается в колонках бутстрапа (1, 2, 3 ... 12))
 * @param {number} props.rightSize - размер правой колонки
 * @param {object} props.style - стили layout
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
let LeftRight = ({ leftSize, rightSize, className, style }) => {
  return (
    <div className={cn('layout row', className)} style={style}>
      <Col md={leftSize}>
        <Place key="left" name="left" />
      </Col>
      <Col md={rightSize}>
        <Place key="right" name="right" />
      </Col>
    </div>
  );
};

LeftRight.propTypes = {
  className: PropTypes.string,
  leftSize: PropTypes.number,
  rightSize: PropTypes.number,
  style: PropTypes.object,
};

LeftRight.defaultProps = {
  leftSize: 6,
  rightSize: 6,
  style: {},
};

export default layoutPlaceResolver(LeftRight);

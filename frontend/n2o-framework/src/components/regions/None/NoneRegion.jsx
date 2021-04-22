import React from 'react';
import PropTypes from 'prop-types';
import classNames from 'classnames';
import { compose, pure, setDisplayName } from 'recompose';
import map from 'lodash/map';

import withWidgetProps from '../withWidgetProps';
import RegionContent from '../RegionContent';

/**
 * Регион None (простой див)
 * @reactProps {array} content - массив из объектов, которые описывают виджет{id}
 * @reactProps {function} getWidget - функция получения виджета
 * @reactProps {string} pageId - идентификатор страницы
 */

const NoneRegion = ({ content, className, style }) => {
  return (
    <div className={classNames('n2o-none-region', className)} style={style}>
      {map(content, item => (
        <RegionContent content={[item]} />
      ))}
    </div>
  );
};

NoneRegion.propTypes = {
  /**
   * Список элементов
   */
  items: PropTypes.array.isRequired,
  getWidget: PropTypes.func.isRequired,
  /**
   * ID страницы
   */
  pageId: PropTypes.string.isRequired,
};

export { NoneRegion };
export default compose(
  setDisplayName('NoneRegion'),
  pure,
  withWidgetProps
)(NoneRegion);

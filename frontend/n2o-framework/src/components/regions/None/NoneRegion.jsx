import React from 'react';
import PropTypes from 'prop-types';
import { compose, pure, setDisplayName } from 'recompose';
import map from 'lodash/map';

import Factory from '../../../core/factory/Factory';
import { WIDGETS } from '../../../core/factory/factoryLevels';
import withWidgetProps from '../withWidgetProps';
/**
 * Регион None (простой див)
 * @reactProps {array} items - массив из объектов, которые описывают виджет{id}
 * @reactProps {function} getWidget - функция получения виджета
 * @reactProps {string} pageId - идентификатор страницы
 */

const NoneRegion = ({ items, getWidget, pageId }) => {
  return (
    <div style={{ paddingBottom: 2, paddingTop: 2 }}>
      {map(items, item => (
        <Factory
          level={WIDGETS}
          key={item.widgetId}
          {...getWidget(pageId, item.widgetId)}
          id={item.widgetId}
          pageId={pageId}
        />
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

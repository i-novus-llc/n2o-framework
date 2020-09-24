import React from 'react';
import PropTypes from 'prop-types';

import withWidgetProps from '../withWidgetProps';
import { compose, pure, setDisplayName } from 'recompose';

import RegionContent from '../RegionContent';

/**
 * Регион None (простой див)
 * @reactProps {array} content - массив из объектов, которые описывают виджет{id}
 * @reactProps {function} getWidget - функция получения виджета
 * @reactProps {string} pageId - идентификатор страницы
 */

const NoneRegion = ({ content }) => {
  return (
    <div style={{ paddingBottom: 2, paddingTop: 2 }}>
      {content.map(item => (
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

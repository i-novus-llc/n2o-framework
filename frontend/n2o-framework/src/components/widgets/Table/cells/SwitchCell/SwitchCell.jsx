import React from 'react';
import PropTypes from 'prop-types';
import get from 'lodash/get';

import Factory from '../../../../../core/factory/Factory';
import { CELLS } from '../../../../../core/factory/factoryLevels';

function SwitchCell(props) {
  const currentCellType = get(props, 'switchFieldId');
  const cellsCollection = get(props, 'switchList');

  const defaultCell = get(props, 'switchDefault');
  const cellProps = get(cellsCollection, currentCellType, defaultCell);

  return <Factory level={CELLS} {...cellProps} />;
}

SwitchCell.propTypes = {
  /**
   * props: метаданные,
   * из которых по switchFieldId
   * в switchList берется Cell.
   * Если ключ не подходит,
   * Cell по switchDefault
   **/
  model: PropTypes.object,
};

export default SwitchCell;

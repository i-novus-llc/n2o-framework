import React from 'react';
import PropTypes from 'prop-types';
import get from 'lodash/get';
import isUndefined from 'lodash/isUndefined';
import Factory from '../../../../../core/factory/Factory';
import { CELLS } from '../../../../../core/factory/factoryLevels';

function SwitchCell({ model }) {
  const currentCellType = get(model, 'switchFieldId');
  const cellsCollection = get(model, 'switchList');
  const currentCell = get(cellsCollection, currentCellType);

  const defaultCell = get(model, 'switchDefault');

  return isUndefined(currentCell) ? (
    <Factory level={CELLS} model={defaultCell} {...defaultCell} />
  ) : (
    <Factory
      level={CELLS}
      src={currentCellType}
      model={currentCell}
      {...currentCell}
    />
  );
}

SwitchCell.propTypes = {
  /**
   * model: модель данных,
   * из которой по switchFieldId
   * в switchList берется Cell.
   * Если ключ не подходит,
   * Cell по switchDefault
   **/
  model: PropTypes.object,
};

SwitchCell.defaultProps = {
  model: {},
};

export default SwitchCell;

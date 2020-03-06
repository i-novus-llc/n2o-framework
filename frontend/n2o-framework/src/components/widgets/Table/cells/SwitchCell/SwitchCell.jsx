import React from 'react';
import get from 'lodash/get';
import Factory from '../../../../../core/factory/Factory';
import { CELLS } from '../../../../../core/factory/factoryLevels';

function SwitchCell(props) {
  const { model, switchFieldId, switchList, switchDefault } = props;
  const currentCellType = get(model, 'switchFieldId');
  const cellsCollection = get(model, 'switchList');
  const currentCell = get(cellsCollection, currentCellType);

  console.warn(currentCellType, cellsCollection, currentCell);

  return (
    <Factory
      level={CELLS}
      src={currentCellType}
      model={currentCell}
      {...currentCell}
    />
  );
}

export default SwitchCell;

import React from 'react';
import cn from 'classnames';
import map from 'lodash/map';
import calcCols from './utils';
import TilesCell from './TilesCell';
import { withResizeDetector } from 'react-resize-detector';

function Tiles(props) {
  const { tiles, className, data, id, colsSm, colsMd, colsLg, width } = props;
  const col = calcCols(colsSm, colsMd, colsLg, width);

  const renderTilesItem = (element, index) => (
    <div className={`col-${col} d-flex justify-content-center`}>
      <div className={cn('n2o-tiles__item')}>
        {map(tiles, cell => (
          <TilesCell
            className={cn('n2o-tiles__cell', cell.className)}
            index={index}
            key={cell.id}
            widgetId={id}
            id={cell.id}
            model={element}
            {...cell}
          />
        ))}
      </div>
    </div>
  );

  return (
    <div className={cn('n2o-tiles__container col-12', className)}>
      {data &&
        data.length &&
        map(data, (element, index) => renderTilesItem(element, index))}
    </div>
  );
}

export default withResizeDetector(Tiles);

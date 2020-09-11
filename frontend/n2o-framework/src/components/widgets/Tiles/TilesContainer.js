import React from 'react';
import { compose, mapProps, lifecycle, withState } from 'recompose';
import isEqual from 'lodash/isEqual';
import { withWidgetHandlers } from '../AdvancedTable/AdvancedTableContainer';
import { withContainerLiveCycle } from '../Table/TableContainer';

import widgetContainer from '../WidgetContainer';
import Tiles from './Tiles';

function TilesContainer(props) {
  return <Tiles {...props} />;
}

export default compose(
  withState('data', 'setData', []),
  widgetContainer(
    {
      mapProps: props => ({
        ...props,
      }),
    },
    'TilesWidget'
  ),
  withContainerLiveCycle,
  withWidgetHandlers,
  lifecycle({
    componentDidUpdate(prevProps) {
      if (!isEqual(prevProps.datasource, this.props.datasource)) {
        this.props.setData(this.props.datasource);
      }
    },
  }),
  mapProps(({ data, className, widgetId, tiles, colsSm, colsMd, colsLg }) => ({
    className: className,
    id: widgetId,
    tiles: tiles,
    data: data,
    colsSm: colsSm,
    colsMd: colsMd,
    colsLg: colsLg,
  }))
)(TilesContainer);

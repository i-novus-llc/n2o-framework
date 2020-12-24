import React from 'react';
import { compose, mapProps } from 'recompose';
import { withWidgetHandlers } from '../AdvancedTable/AdvancedTableContainer';
import { withContainerLiveCycle } from '../Table/TableContainer';

import widgetContainer from '../WidgetContainer';

import Cards from './Cards';

const CardsContainer = props => <Cards {...props} />;

export default compose(
  widgetContainer({
    mapProps: props => ({
      ...props,
    }),
  }),
  withContainerLiveCycle,
  withWidgetHandlers,
  mapProps(
    ({
      datasource,
      className,
      widgetId,
      cards,
      onResolve,
      dispatch,
      align,
      height,
    }) => ({
      className: className,
      id: widgetId,
      cards: cards,
      data: datasource,
      onResolve: onResolve,
      dispatch: dispatch,
      align: align,
      height: height,
    })
  )
)(CardsContainer);

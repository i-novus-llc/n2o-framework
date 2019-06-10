import React from 'react';
import dependency from '../../../core/dependency';
import StandardWidget from '../StandardWidget';
import ChartContainer from './ChartWidgetContainer';

function ChartWidget({
  id: widgetId,
  toolbar,
  disabled,
  actions,
  chart: { fetchOnInit },
  pageId,
}) {
  return (
    <StandardWidget>
      <ChartContainer />
    </StandardWidget>
  );
}

export default dependency(ChartWidget);

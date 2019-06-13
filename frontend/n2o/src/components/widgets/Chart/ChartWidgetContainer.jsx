import React from 'react';
import { compose } from 'recompose';
import widgetContainer from '../WidgetContainer';
import { CHART } from '../widgetTypes';
import ChartType from './ChartType';
import LineChart from './LineChart';
import AreaChart from './AreaChart';
import BarChart from './BarChart';
import PieChart from './PieChart';

const Charts = {
  [ChartType.LINE]: LineChart,
  [ChartType.AREA]: AreaChart,
  [ChartType.BAR]: BarChart,
  [ChartType.PIE]: PieChart,
};

function ChartWidgetContainer({ chart, datasource, ...rest }) {
  const { type } = chart;

  const Component = Charts[type];

  return (
    <div className="n2o-chart-widget">
      <Component {...chart} data={datasource} />
    </div>
  );
}

export default compose(
  widgetContainer(
    {
      mapProps: props => props,
    },
    CHART
  )
)(ChartWidgetContainer);

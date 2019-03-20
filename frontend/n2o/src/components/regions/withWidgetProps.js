import React from 'react';
import { connect } from 'react-redux';
import { widgetsSelector } from '../../selectors/widgets';

export default Region => {
  class RegionWithWidgetProps extends React.Component {
    constructor(props) {
      super(props);
      this.getWidgetProps = this.getWidgetProps.bind(this);
    }

    getWidgetProps(widgetId) {
      return this.props.widgets[widgetId];
    }

    render() {
      return <Region {...this.props} getWidgetProps={this.getWidgetProps} />;
    }
  }

  const mapStateToProps = state => ({
    widgets: widgetsSelector(state)
  });

  return connect(mapStateToProps)(RegionWithWidgetProps);
};

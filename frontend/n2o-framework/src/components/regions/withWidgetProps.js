import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import omit from 'lodash/omit';
import get from 'lodash/get';
import { widgetsSelector } from '../../selectors/widgets';
import { makeModelsByPrefixSelector } from '../../selectors/models';

import { pagesSelector } from '../../selectors/pages';
import {
  hideWidget,
  showWidget,
  disableWidget,
  enableWidget,
  dataRequestWidget,
} from '../../actions/widgets';
import { PREFIXES } from '../../constants/models';

/**
 * HOC для работы с данными
 * @param WrappedComponent - оборачиваемый компонент
 */

function withGetWidget(WrappedComponent) {
  class WithGetWidget extends React.Component {
    constructor(props) {
      super(props);

      this.getWidget = this.getWidget.bind(this);
      this.getWidgetProps = this.getWidgetProps.bind(this);
    }

    getWidget(pageId, widgetId) {
      return this.props.pages[pageId].metadata.widgets[widgetId];
    }

    getWidgetProps(widgetId) {
      return {
        ...get(this.props.widgets, widgetId, {}),
        datasource: this.props.widgetsDatasource[widgetId],
      };
    }

    /**
     * Рендер
     */
    render() {
      const props = omit(this.props, ['widgets']);
      return (
        <WrappedComponent
          {...props}
          getWidget={this.getWidget}
          getWidgetProps={this.getWidgetProps}
        />
      );
    }
  }

  WithGetWidget.propTypes = {
    pages: PropTypes.object,
    widgets: PropTypes.object,
    hideWidget: PropTypes.func,
    showWidget: PropTypes.func,
    disableWidget: PropTypes.func,
    enableWidget: PropTypes.func,
  };

  const mapStateToProps = state => {
    return {
      pages: pagesSelector(state),
      widgets: widgetsSelector(state),
      widgetsDatasource: makeModelsByPrefixSelector(PREFIXES.datasource)(state),
    };
  };

  const mapDispatchToProps = dispatch =>
    bindActionCreators(
      {
        hideWidget: widgetId => hideWidget(widgetId),
        showWidget: widgetId => showWidget(widgetId),
        disableWidget: widgetId => disableWidget(widgetId),
        enableWidget: widgetId => enableWidget(widgetId),
        fetchWidget: (widgetId, options) =>
          dataRequestWidget(widgetId, options),
      },
      dispatch
    );

  return connect(
    mapStateToProps,
    mapDispatchToProps
  )(WithGetWidget);
}

export default withGetWidget;

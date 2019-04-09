import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { pick, omit, get } from 'lodash';
import { widgetsSelector } from '../../selectors/widgets';

import { pagesSelector } from '../../selectors/pages';
import {
  hideWidget,
  showWidget,
  disableWidget,
  enableWidget,
} from '../../actions/widgets';

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
      return get(this.props.widgets, widgetId, {});
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
    };
  };

  const mapDispatchToProps = dispatch =>
    bindActionCreators(
      {
        hideWidget: widgetId => hideWidget(widgetId),
        showWidget: widgetId => showWidget(widgetId),
        disableWidget: widgetId => disableWidget(widgetId),
        enableWidget: widgetId => enableWidget(widgetId),
      },
      dispatch
    );

  return connect(
    mapStateToProps,
    mapDispatchToProps
  )(WithGetWidget);
}

export default withGetWidget;

import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { pick } from 'lodash';

import { pagesSelector } from '../../selectors/pages';
import { hideWidget, showWidget, disableWidget, enableWidget } from '../../actions/widgets';

/**
 * HOC для работы с данными
 * @param WrappedComponent - оборачиваемый компонент
 */

function withGetWidget(WrappedComponent) {
  class WithGetWidget extends React.Component {
    constructor(props) {
      super(props);

      this.getWidget = this.getWidget.bind(this);
    }

    getWidget(pageId, widgetId) {
      return this.props.pages[pageId].metadata.widgets[widgetId];
    }

    /**
     * Рендер
     */

    render() {
      return <WrappedComponent {...this.props} getWidget={this.getWidget} />;
    }
  }

  WithGetWidget.propTypes = {
    pages: PropTypes.object,
    hideWidget: PropTypes.func,
    showWidget: PropTypes.func,
    disableWidget: PropTypes.func,
    enableWidget: PropTypes.func
  };

  const mapStateToProps = state => {
    return {
      pages: pagesSelector(state)
    };
  };

  const mapDispatchToProps = dispatch =>
    bindActionCreators(
      {
        hideWidget: widgetId => hideWidget(widgetId),
        showWidget: widgetId => showWidget(widgetId),
        disableWidget: widgetId => disableWidget(widgetId),
        enableWidget: widgetId => enableWidget(widgetId)
      },
      dispatch
    );

  return connect(
    mapStateToProps,
    mapDispatchToProps
  )(WithGetWidget);
}

export default withGetWidget;

import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { createStructuredSelector } from 'reselect';
import Alert from '../snippets/Alerts/Alert';
import { makeAlertsByKeySelector } from '../../selectors/alerts';
import { removeAlert } from '../../actions/alerts';

/**
 * Компонент-редакс-обертка над алертами виджета
 * @reactProps {string} widgetId - уникальный индефикатор виджета
 * @reactProps {array} alerts - массив алертов
 */
class WidgetAlerts extends Component {
  /**
   * Базовый рендер
   */
  render() {
    const { alerts } = this.props;
    const alertList = alerts.map(alert => (
      <Alert
        key={alert.id}
        onDismiss={() => this.props.onDismiss(alert.id)}
        {...alert}
        details={alert.stacktrace}
      />
    ));
    return <div className="n2o-alerts">{alertList}</div>;
  }
}

WidgetAlerts.propTypes = {
  widgetId: PropTypes.string,
  alerts: PropTypes.array,
};

WidgetAlerts.defaultProps = {
  alerts: [],
};

const mapStateToProps = createStructuredSelector({
  alerts: (state, props) => {
    return makeAlertsByKeySelector(props.widgetId)(state, props);
  },
});

function mapDispatchToProps(dispatch, ownProps) {
  return {
    onDismiss: alertId => {
      dispatch(removeAlert(ownProps.widgetId, alertId));
    },
  };
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(WidgetAlerts);

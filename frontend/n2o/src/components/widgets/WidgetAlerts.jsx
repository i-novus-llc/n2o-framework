import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { map } from 'lodash';
import { connect } from 'react-redux';
import { createStructuredSelector } from 'reselect';
import Alerts from '../snippets/Alerts/Alerts';
import { makeAlertsByKeySelector } from '../../selectors/alerts';
import { removeAlert } from '../../actions/alerts';

/**
 * Компонент-редакс-обертка над алертами виджета
 * @reactProps {string} widgetId - уникальный индефикатор виджета
 * @reactProps {array} alerts - массив алертов
 */
class WidgetAlerts extends Component {
  mapAlertsProps(alerts, onDismiss) {
    return map(alerts, alert => ({
      ...alert,
      key: alert.id,
      onDismiss: () => onDismiss(alert.id),
      details: alert.stacktrace,
    }));
  }

  render() {
    const { alerts } = this.props;
    return (
      <div className="n2o-alerts">
        <Alerts alerts={this.mapAlertsProps(alerts, this.props.onDismiss)} />
      </div>
    );
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

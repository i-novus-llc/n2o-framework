import React, { Component } from 'react';
import { map, isEmpty } from 'lodash';
import Alert from './Alert';
import PropTypes from 'prop-types';

/**
 * Компонент обертка для Alert
 */
class Alerts extends Component {
  filterAlerts(alerts) {
    const absoluteAlerts = [];
    const relativeAlerts = [];
    map(alerts, alert => {
      if (alert.position && alert.position === 'absolute') {
        absoluteAlerts.push(alert);
      } else {
        relativeAlerts.push(alert);
      }
    });

    return {
      absoluteAlerts,
      relativeAlerts,
    };
  }

  renderAlerts(alerts) {
    return map(alerts, alert => <Alert {...alert} />);
  }

  render() {
    const { alerts } = this.props;
    const { absoluteAlerts, relativeAlerts } = this.filterAlerts(alerts);
    return (
      <div className="n2o-alerts-container">
        {!isEmpty(absoluteAlerts) && (
          <div className="n2o-alerts--absolute">
            {this.renderAlerts(absoluteAlerts)}
          </div>
        )}
        {!isEmpty(relativeAlerts) && (
          <div className="n2o-alerts--relative">
            {this.renderAlerts(relativeAlerts)}
          </div>
        )}
      </div>
    );
  }
}

Alerts.propTypes = {
  alerts: PropTypes.array,
};

Alert.defaultProps = {
  alerts: [],
};

export default Alerts;

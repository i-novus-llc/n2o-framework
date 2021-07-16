import React, { Component } from 'react'
import map from 'lodash/map'
import isEmpty from 'lodash/isEmpty'
import PropTypes from 'prop-types'

import Alert from './Alert'

/**
 * Компонент обертка для Alert
 */
export class Alerts extends Component {
  filterAlerts = (alerts) => {
      const absoluteAlerts = []
      const relativeAlerts = []
      const fixedAlerts = []

      map(alerts, (alert) => {
          if (alert.position && alert.position === 'absolute') {
              absoluteAlerts.push(alert)
          } else if (alert.position && alert.position === 'relative') {
              relativeAlerts.push(alert)
          } else {
              fixedAlerts.push(alert)
          }
      })

      return {
          absoluteAlerts,
          relativeAlerts,
          fixedAlerts,
      }
  };

  renderAlerts = alerts => map(alerts, alert => <Alert {...alert} />);

  getAlertPlacement = alerts => (alerts.length !== 0 ? alerts[0].placement || 'bottom' : null)

  render() {
      const { alerts } = this.props
      const { absoluteAlerts, relativeAlerts, fixedAlerts } = this.filterAlerts(
          alerts,
      )

      const fixedAlertsPlacement = this.getAlertPlacement(fixedAlerts)

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
              {!isEmpty(fixedAlerts) && (
                  <div className={`n2o-alerts--fixed ${fixedAlertsPlacement}`}>
                      {this.renderAlerts(fixedAlerts)}
                  </div>
              )}
          </div>
      )
  }
}

Alerts.propTypes = {
    alerts: PropTypes.array,
}

Alert.defaultProps = {
    alerts: [],
}

export default Alerts

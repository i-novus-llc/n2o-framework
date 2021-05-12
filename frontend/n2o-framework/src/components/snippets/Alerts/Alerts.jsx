import React, { Component } from 'react'
import map from 'lodash/map'
import isEmpty from 'lodash/isEmpty'
import PropTypes from 'prop-types'

import Alert from './Alert'

/**
 * Компонент обертка для Alert
 */
export class Alerts extends Component {
    constructor(props) {
        super(props)

        this.ref = React.createRef()

        this.state = {
            style: {},
        }
    }

    componentDidUpdate() {
        const { style } = this.state

        if (!style.width && this.ref.current) {
            this.setState({
                style: {
                    width: `${this.ref.current.clientWidth}px`,
                },
            })
        }
    }

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

  render() {
      const { alerts } = this.props
      const { style } = this.state
      const { absoluteAlerts, relativeAlerts, fixedAlerts } = this.filterAlerts(
          alerts,
      )

      return (
          <div className="n2o-alerts-container" ref={this.ref}>
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
                  <div className="n2o-alerts--fixed" style={style}>
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

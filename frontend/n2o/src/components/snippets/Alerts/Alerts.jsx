import React from 'react';
import PropTypes from 'prop-types';
import _ from 'lodash';
import cx from 'classnames';

/**
 * Компонент сообщениея (Alerts)
 * @reactProps {string} text - текст алерта
 * @reactProps {string} severity - тип алерта
 * @reactProps {array} alerts
 * @reactProps {string} details
 * @reactProps {string} stacktrace
 * @reactProps {Array<Object>} alerts - список раскрываемых алертов
 */
class Alerts extends React.Component {
  constructor(props) {
    super(props);

    this.alerts = [];
  }

  /**
   * Добавляет сообщение
   * Объект message = {
   *  text,
   *  severity,
   *  details,
   *  stacktrace
   * }
   * @param alert
   */
  addAlert(alert) {
    this.alerts.push(alert);
    this.forceUpdate();
  }

  /**
   * Удаляет сообщение по индексу.
   * Удаляет только среди сообщений добаленные через метод addMessage
   * @param index
   */
  clearAlert(index) {
    this.messages.splice(index, 1);
  }

  /**
   * Удаляет все сообщения добаленные через метод addMessage
   */
  clearAllAlerts() {
    this.messages = [];
  }

  render() {
    let msgTmp;
    const { text, severity, details, stacktrace, alerts } = this.props;
    const msgList = _.concat(alerts || [], this.alerts);
    if (msgList && msgList.length) {
      msgTmp = msgList.map(m => <div className={cx('alert', `alert-${m.severity}`)}>{m.text}</div>);
    } else if (text && severity) {
      msgTmp = <div className={cx('alert', `alert-${severity}`)}>{text}</div>;
    }

    return <div className="n2o-alerts">{msgTmp ? msgTmp : null}</div>;
  }
}

Alerts.propTypes = {
  text: PropTypes.string,
  severity: PropTypes.string,
  alerts: PropTypes.array,
  details: PropTypes.string,
  stacktrace: PropTypes.string,
  visible: PropTypes.bool
};

Alerts.defaultProps = {
  visible: true,
  alerts: PropTypes.array
};

export default Alerts;

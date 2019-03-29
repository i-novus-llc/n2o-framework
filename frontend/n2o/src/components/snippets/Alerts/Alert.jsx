import React from 'react';
import PropTypes from 'prop-types';
import cx from 'classnames';

/**
 * Компонент сообщения-алерта
 * @reactProps {string} label - лейбл алерта
 * @reactProps {string} text - текст алерта
 * @reactProps {string} severity - тип алерта: 'info', 'danger', 'warning' или 'success'.
 * @reactProps {string} details - подробности, находятся под текстом, показываются (скрываются) по клику на выделенный текст
 * @reactProps {boolean} closeButton - отображать кнопку скрытия алерта или нет
 * @reactProps {boolean} onDismiss - выполняется при скрытии алерта
 * @reactProps {boolean} className - css-класс
 * @reactProps {number} style -  объект css стилей
 * @reactProps {number} icon - css-класс для иконки(иконка находится перед лейбелом )
 * @reactProps {boolean} visible - флаг видимости
 * @example
 * <Alert onDismiss={this.onDismiss}
 *             label='Сообщение'
 *             text={this.text} />
 */
class Alert extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      detailsVisible: false
    };
    this.toggleDetails = this.toggleDetails.bind(this);
  }

  /**
   * скрытие / показ деталей
   */
  toggleDetails() {
    this.setState({
      detailsVisible: !this.state.detailsVisible
    });
  }

  /**
   * Базовый рендер
   */
  render() {
    const {
      label,
      text,
      severity,
      closeButton,
      className,
      style,
      icon,
      details,
      onDismiss,
      visible
    } = this.props;
    const { detailsVisible } = this.state;
    return (
      visible !== false && (
        <div className={cx('alert', `alert-${severity}`, className)} style={style}>
          {closeButton && (
            <button className="close n2o-alert-close" onClick={onDismiss}>
              <span>×</span>
            </button>
          )}
          {label && (
            <div className="n2o-alert-header">
              {icon && <i className={icon} />}
              <h4>{label}</h4>
            </div>
          )}
          <div className={'n2o-alert-body'}>
            {text}
            {details && (
              <a className="alert-link details-label" onClick={this.toggleDetails}>
                {detailsVisible ? 'Скрыть' : 'Подробнее'}
              </a>
            )}
            {detailsVisible && <div className="details">{details}</div>}
          </div>
        </div>
      )
    );
  }
}

Alert.defaultProps = {
  text: '',
  label: '',
  severity: 'info',
  details: '',
  closeButton: true,
  visible: true,
  onDismiss: () => {}
};

Alert.propTypes = {
  label: PropTypes.string,
  text: PropTypes.string,
  severity: PropTypes.oneOf([['info', 'danger', 'warning', 'success']]),
  details: PropTypes.string,
  closeButton: PropTypes.bool,
  onDismiss: PropTypes.func,
  className: PropTypes.string,
  style: PropTypes.object,
  icon: PropTypes.string,
  visible: PropTypes.bool
};

export default Alert;

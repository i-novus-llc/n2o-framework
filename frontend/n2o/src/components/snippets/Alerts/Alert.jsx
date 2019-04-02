import React from 'react';
import PropTypes from 'prop-types';
import InlineSpinner from '../Spinner/InlineSpinner';
import cx from 'classnames';
import Popover from '../Popover/Popover';

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
 * @reactProps {string} position - настройка позиционирования
 * @reactProps {string} help - текст для Popover
 * @reactProps {boolean} animate - флаг включения анимации при появлении
 * @example
 * <Alert onDismiss={this.onDismiss}
 *             label='Сообщение'
 *             text={this.text} />
 */
class Alert extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      detailsVisible: false,
    };
    this.toggleDetails = this.toggleDetails.bind(this);
    this.renderAlert = this.renderAlert.bind(this);
    this.renderDefaultAlert = this.renderDefaultAlert.bind(this);
    this.renderLoaderAlert = this.renderLoaderAlert.bind(this);
  }

  /**
   * скрытие / показ деталей
   */
  toggleDetails() {
    this.setState({
      detailsVisible: !this.state.detailsVisible,
    });
  }

  renderAlert() {
    const { loader } = this.props;
    if (loader) {
      return this.renderLoaderAlert();
    } else {
      return this.renderDefaultAlert();
    }
  }

  renderLoaderAlert() {
    const { severity, className, style, text, animate } = this.props;

    return (
      <div
        className={cx('n2o-alert', 'n2o-alert-loader', 'alert', className, {
          [`alert-${severity}`]: severity,
          'n2o-alert--animated': animate,
        })}
        style={style}
      >
        <div className="n2o-alert-body-container">
          <InlineSpinner />
          {text || 'Загрузка...'}
        </div>
      </div>
    );
  }

  renderDefaultAlert() {
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
      help,
      animate,
      position,
    } = this.props;

    const { detailsVisible } = this.state;

    return (
      <div
        className={cx('n2o-alert', 'alert', className, {
          [`alert-${severity}`]: severity,
          'n2o-alert--animated': animate,
          'n2o-alert--relative': position === 'relative',
          'n2o-alert--absolute': position === 'absolute',
        })}
        style={style}
      >
        <div className="n2o-alert-help">{help && <Popover help={help} />}</div>
        <div className="n2o-alert-body-container">
          {label && (
            <div className="n2o-alert-header">
              {icon && <i className={icon} />}
              <h4>{label}</h4>
            </div>
          )}
          <div className={'n2o-alert-body'}>
            <div className="n2o-alert-body-text">{text}</div>
            {details && (
              <a
                className="alert-link details-label"
                onClick={this.toggleDetails}
              >
                {detailsVisible ? 'Скрыть' : 'Подробнее'}
              </a>
            )}
            {detailsVisible && <div className="details">{details}</div>}
          </div>
        </div>
        <div className="n2o-alert-close-container">
          {closeButton && (
            <button className="close n2o-alert-close" onClick={onDismiss}>
              <span>×</span>
            </button>
          )}
        </div>
      </div>
    );
  }

  /**
   * Базовый рендер
   */
  render() {
    const { visible } = this.props;
    return visible !== false && this.renderAlert();
  }
}

Alert.defaultProps = {
  text: '',
  label: '',
  severity: 'info',
  details: '',
  closeButton: true,
  visible: true,
  onDismiss: () => {},
  position: 'relative',
  animate: false,
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
  visible: PropTypes.bool,
  position: PropTypes.string,
  help: PropTypes.string,
  animate: PropTypes.bool,
};

export default Alert;

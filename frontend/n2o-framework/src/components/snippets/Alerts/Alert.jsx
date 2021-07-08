import React from 'react'
import defaultTo from 'lodash/defaultTo'
import isArray from 'lodash/isArray'
import map from 'lodash/map'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import { withTranslation } from 'react-i18next'

import HelpPopover from '../../widgets/Form/fields/StandardField/HelpPopover'
import InlineSpinner from '../Spinner/InlineSpinner'
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
        super(props)
        this.state = {
            detailsVisible: false,
        }
        this.toggleDetails = this.toggleDetails.bind(this)
        this.renderAlert = this.renderAlert.bind(this)
        this.renderDefaultAlert = this.renderDefaultAlert.bind(this)
        this.renderLoaderAlert = this.renderLoaderAlert.bind(this)
    }

    /**
     * скрытие / показ деталей
     */
    toggleDetails() {
        const { detailsVisible } = this.state

        this.setState({
            detailsVisible: !detailsVisible,
        })
    }

    renderAlert() {
        const { loader } = this.props

        if (loader) {
            return this.renderLoaderAlert()
        }

        return this.renderDefaultAlert()
    }

    renderLoaderAlert() {
        const { severity, className, style, text, animate, t } = this.props

        return (
            <div
                className={classNames(
                    'n2o-alert',
                    'n2o-alert-loader',
                    'alert',
                    'n2o-snippet',
                    className,
                    {
                        [`alert-${severity}`]: severity,
                        'n2o-alert--animated': animate,
                    },
                )}
                style={style}
            >
                <div className="n2o-alert-body-container">
                    <InlineSpinner />
                    {text || `${t('loading')}...`}
                </div>
            </div>
        )
    }

    formatDetailes = (details) => {
        if (isArray(details)) {
            return map(details, d => (
                <>
                    {d}
                    <br />
                </>
            ))
        }

        return details
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
            t,
        } = this.props

        const { detailsVisible } = this.state

        const closeIconClasses = classNames(
            'close n2o-alert-close n2o-alert-close__icon',
            { 'with-details': details },
        )

        return (
            <div
                className={classNames('n2o-alert', 'alert', className, {
                    [`alert-${severity}`]: severity,
                    'n2o-alert--animated': animate,
                    'with-details': details,
                })}
                style={style}
            >
                <div className="n2o-alert-help">
                    {help && <HelpPopover help={help} />}
                </div>
                <div className="n2o-alert-body-container">
                    {label && (
                        <div className="n2o-alert-header">
                            {icon && <i className={icon} />}
                            <h4>{label}</h4>
                        </div>
                    )}
                    <div className="n2o-alert-body">
                        <div className="n2o-alert-body-text white-space-pre-line">
                            {text}
                        </div>
                        {details && (
                            <a
                                className="alert-link details-label"
                                onClick={this.toggleDetails}
                            >
                                {detailsVisible ? t('hide') : t('details')}
                            </a>
                        )}
                        {detailsVisible && (
                            <div className="details">{this.formatDetailes(details)}</div>
                        )}
                    </div>
                </div>
                {defaultTo(closeButton, true) && (
                    <div className={closeIconClasses} onClick={onDismiss}>×</div>
                )}
            </div>
        )
    }

    /**
     * Базовый рендер
     */
    render() {
        const { visible } = this.props

        return visible !== false && this.renderAlert()
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
    t: () => {},
}

Alert.propTypes = {
    /**
     * Заголовок алерта
     */
    label: PropTypes.string,
    /**
     * Текст алерта
     */
    text: PropTypes.string,
    /**
     * Цвет алерта
     */
    severity: PropTypes.oneOf([['info', 'danger', 'warning', 'success']]),
    /**
     * Подробности алерта
     */
    details: PropTypes.string,
    /**
     * Флаг показа кнопки закрытия
     */
    closeButton: PropTypes.bool,
    /**
     * Callback на закрытие
     */
    onDismiss: PropTypes.func,
    /**
     * Класс алерта
     */
    className: PropTypes.string,
    /**
     * Стили
     */
    style: PropTypes.object,
    /**
     * Иконка рядом с заголовком
     */
    icon: PropTypes.string,
    /**
     * Видимость
     */
    visible: PropTypes.bool,
    /**
     * Позиционирование алерта
     */
    // eslint-disable-next-line react/no-unused-prop-types
    position: PropTypes.string,
    /**
     * Кнопка tooltip
     */
    help: PropTypes.string,
    /**
     * Флаг включения всплытия с анимацией
     */
    animate: PropTypes.bool,
    t: PropTypes.func,
    loader: PropTypes.any,
}

export default withTranslation()(Alert)

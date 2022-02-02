import React from 'react'
import classNames from 'classnames'
import defaultTo from 'lodash/defaultTo'

import HelpPopover from '../../widgets/Form/fields/StandardField/HelpPopover'

import { AlertTypes } from './AlertsTypes'

export const DefaultAlert = ({
    title,
    text,
    color,
    href,
    timestamp,
    closeButton,
    className,
    style,
    icon,
    stacktrace,
    onDismiss,
    help,
    animate,
    t,
    detailsVisible,
    togglingDetails,
    formattingDetails,
    onClose = null,
}) => {
    const closeIconClasses = classNames(
        'close n2o-alert-close n2o-alert-close__icon',
        { 'with-details': stacktrace },
    )

    const batchedActionToClose = (e) => {
        e.preventDefault()

        if (onClose) {
            /* custom onClose this is used id AlertField */
            onClose()
        } else {
            onDismiss()
        }
    }

    const batchedActionToToggling = (e) => {
        e.preventDefault()
        togglingDetails()
    }

    return (
        <a
            href={href}
            className={classNames('n2o-alert', 'alert', className, {
                [`alert-${color}`]: color,
                'n2o-alert--animated': animate,
                'with-details': stacktrace,
                'with-link': href,
            })}
            style={style}
        >
            <div className="n2o-alert-help">
                {help && <HelpPopover help={help} />}
            </div>
            <div className="n2o-alert-body-container">
                {title && (
                    <div className="n2o-alert-header">
                        {icon && <i className={icon} />}
                        <h4>{title}</h4>
                    </div>
                )}
                {(title && text) && <hr />}
                <div className="n2o-alert-body">
                    <div className="n2o-alert-body-text white-space-pre-line">
                        {text}
                    </div>
                    {stacktrace && (
                        <a
                            className="alert-link details-title"
                            onClick={batchedActionToToggling}
                        >
                            {detailsVisible ? t('hide') : t('details')}
                        </a>
                    )}
                    {detailsVisible && (
                        <div className="details">{formattingDetails(stacktrace)}</div>
                    )}
                </div>
            </div>
            <span>{timestamp}</span>
            {defaultTo(closeButton, true) && (
                <div className={closeIconClasses} onClick={batchedActionToClose}>×</div>
            )}
        </a>
    )
}

DefaultAlert.propTypes = AlertTypes

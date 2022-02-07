import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

export const TextSegment = ({ text, className }) => {
    if (!text) {
        return null
    }

    return (
        <section className={classNames(
            'w-100 d-flex align-self-start align-items-center',
            className,
        )}
        >
            {text}
        </section>
    )
}

TextSegment.propTypes = {
    text: PropTypes.string,
    className: PropTypes.string,
}

export const TimeStampSegment = ({ timestamp, text }) => {
    if (!timestamp) {
        return null
    }

    return (
        <section className={classNames(
            'w-50 n2o-alert-segment__timestamp d-flex align-items-start',
            {
                'w-100 justify-content-start': !text,
                'justify-content-end text-right': text,
            },
        )}
        >
            {timestamp}
        </section>
    )
}

TimeStampSegment.propTypes = {
    timestamp: PropTypes.string,
    text: PropTypes.string,
}

export const CloseButtonSegment = ({ closeButton, onClick }) => {
    if (!closeButton) {
        return null
    }

    return (
        <section className="n2o-alert-segment__icon-container d-flex justify-content-end">
            <i
                onClick={onClick}
                className="fa fa-close n2o-alert-segment__icon-close d-flex align-items-start"
            />
        </section>
    )
}

CloseButtonSegment.propTypes = {
    closeButton: PropTypes.bool,
    onClick: PropTypes.func,
}

export const StacktraceSegment = ({ stacktrace, onClick, stacktraceVisible, t }) => {
    if (!stacktrace) {
        return null
    }

    return (
        <section className="w-100 n2o-alert-segment__stacktrace-container">
            <div
                onClick={onClick}
                className="d-flex flex-nowrap align-items-center n2o-alert-segment__stacktrace-details-button"
            >
                <div className="mt-1 mb-1">
                    {stacktraceVisible ? t('hide') : t('details')}
                </div>
                <i className={classNames(
                    'mt-1 ml-1 n2o-alert-segment__arrow',
                    {
                        'fa fa-arrow-up': stacktraceVisible,
                        'fa fa-arrow-down': !stacktraceVisible,
                    },
                )}
                />
            </div>
            <div className={classNames(
                'n2o-alert-segment__stacktrace-area',
                {
                    visible: stacktraceVisible,
                },
            )}
            >
                {stacktrace}
            </div>
        </section>
    )
}

StacktraceSegment.propTypes = {
    stacktrace: PropTypes.string,
    onClick: PropTypes.func,
    stacktraceVisible: PropTypes.bool,
    t: PropTypes.func,
}

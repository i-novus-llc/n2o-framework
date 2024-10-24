import React from 'react'
import classNames from 'classnames'

interface TextSegmentProps {
    text?: string
    className?: string
}

export const TextSegment = ({ text, className }: TextSegmentProps) => {
    if (!text) { return null }

    return (
        <section className={classNames('w-100 d-flex align-self-start align-items-center', className)}>
            {text}
        </section>
    )
}

interface TimeStampSegmentProps {
    timestamp?: string
    text?: string
}

export const TimeStampSegment = ({ timestamp, text }: TimeStampSegmentProps) => {
    if (!timestamp) { return null }

    return (
        <section className={classNames(
            'n2o-alert-segment__timestamp d-flex align-items-start justify-content-end text-right',
            {
                'w-50': text,
                'w-100': !text,
            },
        )}
        >
            {timestamp}
        </section>
    )
}

interface CloseButtonSegmentProps {
    closeButton?: boolean
    onClick?(): void
    text?: string
    timestamp?: string
    extended?: boolean
}

export const CloseButtonSegment = ({ closeButton, onClick, text, timestamp, extended }: CloseButtonSegmentProps) => {
    if (!closeButton) { return null }

    return (
        <section className={classNames(
            'n2o-alert-segment__icon-container d-flex justify-content-end',
            {
                'text-empty': !text,
                'w-100': !text && !timestamp,
                extended,
            },
        )}
        >
            <i onClick={onClick} className="fa fa-close n2o-alert-segment__icon-close d-flex align-items-start" />
        </section>
    )
}

interface StacktraceSegmentProps {
    stacktrace?: string
    onClick?(): void
    stacktraceVisible: boolean
    t(key: string): string
}

export const StacktraceSegment = ({ stacktrace, onClick, stacktraceVisible, t }: StacktraceSegmentProps) => {
    if (!stacktrace) { return null }

    return (
        <section className="w-100 n2o-alert-segment__stacktrace-container">
            <div
                onClick={onClick}
                className="d-flex flex-nowrap align-items-center n2o-alert-segment__stacktrace-details-button"
            >
                <div className="mt-1 mb-1">{stacktraceVisible ? t('hide') : t('details')}</div>
                <i className={classNames(
                    'mt-1 ml-1 n2o-alert-segment__arrow',
                    {
                        'fa fa-arrow-up': stacktraceVisible,
                        'fa fa-arrow-down': !stacktraceVisible,
                    },
                )}
                />
            </div>
            <div className={classNames('n2o-alert-segment__stacktrace-area', { visible: stacktraceVisible })}>
                {stacktrace}
            </div>
        </section>
    )
}

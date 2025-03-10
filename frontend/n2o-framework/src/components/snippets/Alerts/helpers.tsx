import React, { useCallback } from 'react'
import classNames from 'classnames'
import { Text } from '@i-novus/n2o-components/lib/Typography/Text'

import { copyTextToClipboard } from '../../../utils/copyTextToClipboard'

import { CommonAlertProps, SegmentProps } from './types'

export const TextSegment = ({ text, className }: SegmentProps) => {
    if (!text) { return null }

    return (
        <section className={classNames('w-100 d-flex align-self-start align-items-center', className)}>
            <Text>{text}</Text>
        </section>
    )
}

export const TimeStampSegment = ({ timestamp, text }: SegmentProps) => {
    if (!timestamp) { return null }

    return (
        <section className={classNames(
            'n2o-alert-segment__timestamp d-flex align-items-start justify-content-end text-right',
            { 'w-50': text, 'w-100': !text },
        )}
        >
            {timestamp}
        </section>
    )
}

export const CloseButtonSegment = ({ closeButton, onClick, text, timestamp, extended }: SegmentProps) => {
    if (!closeButton) { return null }

    return (
        <section className={classNames(
            'n2o-alert-segment__icon-container d-flex justify-content-end',
            { 'text-empty': !text, 'w-100': !text && !timestamp, extended },
        )}
        >
            <i onClick={onClick} className="fa fa-close n2o-alert-segment__icon-close d-flex align-items-start" />
        </section>
    )
}

export const StacktraceSegment = ({ stacktrace, onClick, stacktraceVisible, t }: CommonAlertProps) => {
    const copyStacktraceToClipboard = useCallback(() => copyTextToClipboard(stacktrace), [stacktrace])

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
                <i
                    onClick={copyStacktraceToClipboard}
                    title="Скопировать текст ошибки"
                    className="fa-regular fa-copy n2o-alert-segment__copy-button"
                />
                {stacktrace}
            </div>
        </section>
    )
}

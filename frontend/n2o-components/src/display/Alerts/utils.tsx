import React from 'react'
import classNames from 'classnames'

import { Text } from '../../Typography/Text'

import { AlertSectionProps } from './types'

type TextSegmentPropsType = { className: AlertSectionProps['textClassName'] } & Pick<AlertSectionProps, 'text'>

export const TextSegment = ({ text, className }: TextSegmentPropsType) => {
    if (!text) {
        return null
    }

    return (
        <section className={classNames(
            'w-100 d-flex align-self-start align-items-center',
            className,
        )}
        >
            <Text>{text}</Text>
        </section>
    )
}

type TimeStampSegmentPropsType = Pick<AlertSectionProps, 'text' | 'timestamp'>

export const TimeStampSegment = ({ timestamp, text }: TimeStampSegmentPropsType) => {
    if (!timestamp) {
        return null
    }

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
type CloseButtonSegmentPropsType = { extended: AlertSectionProps['isSimple'] } & Pick<AlertSectionProps, 'closeButton' | 'onClick' | 'text' | 'timestamp'>

export const CloseButtonSegment = ({
    closeButton,
    onClick,
    text,
    timestamp,
    extended,
}: CloseButtonSegmentPropsType) => {
    if (!closeButton) {
        return null
    }

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
            <i
                onClick={onClick}
                className="fa fa-close n2o-alert-segment__icon-close d-flex align-items-start"
            />
        </section>
    )
}
type StacktraceSegmentPropsType = Pick<AlertSectionProps, 'stacktrace' | 'onClick' | 'stacktraceVisible' | 't'>

export const StacktraceSegment = ({ stacktrace, onClick, stacktraceVisible, t }: StacktraceSegmentPropsType) => {
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
                    {stacktraceVisible ? t?.('hide') : t?.('details')}
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

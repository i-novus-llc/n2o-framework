import React from 'react'
import classNames from 'classnames'

import { InlineSpinner } from '../../layouts/Spinner/InlineSpinner'

import { AlertProps } from './types'

type LoaderAlertPropsType = Pick<AlertProps, 'text' | 'severity' | 'className' | 'style' | 'animate' | 't'>

export const LoaderAlert = ({ text, severity, className, style, animate, t }: LoaderAlertPropsType) => (
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
            {text || `${t?.('loading')}...`}
        </div>
    </div>
)

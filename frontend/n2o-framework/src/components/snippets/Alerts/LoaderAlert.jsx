import classNames from 'classnames'
import React from 'react'

import InlineSpinner from '../Spinner/InlineSpinner'

import { AlertTypes } from './AlertsTypes'

export const LoaderAlert = ({ text, color, className, style, animate, t }) => (
    <div
        className={classNames(
            'n2o-alert',
            'n2o-alert-loader',
            'alert',
            'n2o-snippet',
            className,
            {
                [`alert-${color}`]: color,
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

LoaderAlert.propTypes = AlertTypes

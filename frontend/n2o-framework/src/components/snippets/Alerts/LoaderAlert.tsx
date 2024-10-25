import classNames from 'classnames'
import React from 'react'

import InlineSpinner from '../Spinner/InlineSpinner'

interface Props {
    text?: string
    severity?: string
    className?: string
    style?: React.CSSProperties
    animate?: boolean
    t(key: string): string
}

export const LoaderAlert = ({
    text,
    severity,
    className,
    style,
    animate,
    t,
}: Props) => (
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

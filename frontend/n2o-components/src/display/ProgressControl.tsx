import React from 'react'
import { Progress } from 'reactstrap'
import isUndefined from 'lodash/isUndefined'
import isArray from 'lodash/isArray'
import map from 'lodash/map'
import isObject from 'lodash/isObject'

import { TBaseProps } from '../types'

import '../styles/controls/ProgressControl.scss'

type ProgressControlProps = TBaseProps & {
    animated?: boolean,
    barClassName?: string,
    barText: string,
    bars: ProgressControlProps[],
    color?: string,
    id: string,
    max?: number,
    multi?: boolean,
    striped?: boolean,
    value: number | Record<string, number>
}

export const ProgressControl = (props: ProgressControlProps) => {
    const { multi = false, barClassName, bars, value, barText, max } = props

    const isMultiProgressControl = multi && !isUndefined(bars) && isArray(bars) && isObject(value)
    const barsCollection = isMultiProgressControl ? bars : []
    const mapProgressControls = (value: Record<string, number>) => map(
        barsCollection,
        (({ id, barText, ...bar }: ProgressControlProps) => (
            <div className="n2o-progress-control">
                <Progress
                    {...bar}
                    bar
                    key={id}
                    className={barClassName}
                    max={max}
                    value={value[id]}
                >
                    {barText}
                </Progress>
            </div>
        )),
    )

    return (
        <div className="n2o-progress-control">
            {isMultiProgressControl
                ? <Progress multi>{mapProgressControls(value)}</Progress>
                : <Progress {...props} value={Number(value)}>{barText}</Progress>
            }
        </div>
    )
}

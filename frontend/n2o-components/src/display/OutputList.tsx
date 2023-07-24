import React from 'react'
import map from 'lodash/map'
import classNames from 'classnames'

import { TBaseInputProps, TBaseProps } from '../types'

import { OutputListItem } from './OutputListItem'

import '../styles/components/OutputList.scss'

type OutputListProps = TBaseProps & TBaseInputProps<Array<Record<string, unknown>>> & {
    direction?: 'row' | 'column',
    labelFieldId?: string,
    linkFieldId?: string,
    separator?: string,
    target?: string
}

export const OutputList = (props: OutputListProps) => {
    const { value = [], className, direction = 'column', ...rest } = props

    const directionClassName = `n2o-output-list--${direction}`

    return (
        <ul
            className={classNames('n2o-output-list', className, directionClassName)}
        >
            {map(value, (item, index) => (
                <OutputListItem
                    key={index}
                    {...rest}
                    {...item}
                    isLast={index === value.length - 1}
                />
            ))}
        </ul>
    )
}

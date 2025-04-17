import React from 'react'
import map from 'lodash/map'
import isEmpty from 'lodash/isEmpty'
import classNames from 'classnames'

import { TBaseInputProps, TBaseProps } from '../types'
import { EMPTY_ARRAY } from '../utils/emptyTypes'

import { OutputListItem } from './OutputListItem'

import '../styles/components/OutputList.scss'

type Props = TBaseProps & TBaseInputProps<Array<Record<string, unknown>>> & {
    direction?: 'row' | 'column',
    labelFieldId?: string,
    linkFieldId?: string,
    separator?: string,
    target?: string
}

export const OutputList = ({ className, direction = 'column', value = EMPTY_ARRAY, ...rest }: Props) => (
    <ul className={classNames('n2o-output-list', className, `n2o-output-list--${direction}`)}>
        {map(value, (item, index) => {
            if (isEmpty(item)) { return null }

            return <OutputListItem key={index} {...rest} {...item} isLast={index === value.length - 1} />
        })}
    </ul>
)

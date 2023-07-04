import React, { memo, useMemo } from 'react'

import { RowContainerProps } from '../models/props'
// @ts-ignore - отсутствуют типы
import propsResolver from '../../../utils/propsResolver'

import { RowResolver } from './RowResolver'

export const RowContainer = memo<RowContainerProps>((props) => {
    const { elementAttributes, data, ...otherProps } = props
    const resolvedElementAttributes = useMemo(() => (
        propsResolver(elementAttributes, data) || {}
    ), [elementAttributes, data])

    return (
        <RowResolver
            {...otherProps}
            data={data}
            elementAttributes={resolvedElementAttributes}
        />
    )
})

RowContainer.displayName = 'RowContainer'

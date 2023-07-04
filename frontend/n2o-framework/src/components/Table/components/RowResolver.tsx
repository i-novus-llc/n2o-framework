import React, { VFC } from 'react'

import { RowResolverProps } from '../types/props'

import { DataRow } from './DataRow'

export const RowResolver: VFC<RowResolverProps> = (props) => {
    const { component: CustomRowComponent, ...otherProps } = props

    return CustomRowComponent ? (
        <CustomRowComponent
            {...otherProps}
        />
    ) : (
        <DataRow
            {...otherProps}
        />
    )
}

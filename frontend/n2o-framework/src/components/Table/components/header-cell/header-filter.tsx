import React, { memo } from 'react'

import { HeaderFilterProps } from '../../types/props'
// @ts-ignore ignore import error from js file
// eslint-disable-next-line import/no-named-as-default
import AdvancedTableFilter from '../filter/AdvancedTableFilter'
import { useTableRefProps } from '../../provider/TableRefProps'
import { useTableActions } from '../../provider/TableActions'

export const HeaderFilter = memo<HeaderFilterProps>(({ filterField, id }) => {
    const refTableProps = useTableRefProps()
    const { onChangeFilter } = useTableActions()
    const filterValue = refTableProps.current.filterValue?.[id]

    return (
        <AdvancedTableFilter
            id={id}
            onFilter={onChangeFilter}
            value={filterValue}
            field={filterField}
        />
    )
})

HeaderFilter.displayName = 'HeaderFilter'

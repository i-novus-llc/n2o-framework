import React, { memo } from 'react'

import { HeaderFilterProps } from '../../types/props'
// @ts-ignore - отсутсвует типизация
// eslint-disable-next-line import/no-named-as-default
import AdvancedTableFilter from '../filter/AdvancedTableFilter'
import { useTableRefProps } from '../../provider/TableRefProps'
import { useTableActions } from '../../provider/TableActions'

export const HeaderFilter = memo<HeaderFilterProps>(({ filterControl, id }) => {
    const refTableProps = useTableRefProps()
    const { onChangeFilter } = useTableActions()
    const filterValue = refTableProps.current.filterValue?.[id]

    return (
        <AdvancedTableFilter
            id={id}
            onFilter={onChangeFilter}
            value={filterValue}
            control={filterControl}
        />
    )
})

HeaderFilter.displayName = 'HeaderFilter'

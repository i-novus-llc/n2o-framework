import React, { memo } from 'react'

import { HeaderFilterProps } from '../../types/props'
import { AdvancedTableFilter } from '../filter/AdvancedTableFilter'
import { useTableRefProps } from '../../provider/TableRefProps'
import { useTableActions } from '../../provider/TableActions'

export const HeaderFilter = memo<HeaderFilterProps>((
    {
        filterField,
        id,
        validateFilterField,
        filterError,
    },
) => {
    const refTableProps = useTableRefProps()
    const { onChangeFilter } = useTableActions()
    const filterValue = refTableProps.current.filterValue?.[id]

    return (
        <AdvancedTableFilter
            id={id}
            onFilter={onChangeFilter}
            value={filterValue}
            field={filterField}
            validateFilterField={validateFilterField}
            error={filterError}
        />
    )
})

HeaderFilter.displayName = 'HeaderFilter'

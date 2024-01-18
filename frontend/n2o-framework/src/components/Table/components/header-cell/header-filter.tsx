import React, { memo, useMemo } from 'react'

import { HeaderFilterProps } from '../../types/props'
// @ts-ignore ignore import error from js file
// eslint-disable-next-line import/no-named-as-default
import AdvancedTableFilter from '../filter/AdvancedTableFilter'
import { useTableRefProps } from '../../provider/TableRefProps'
import { useTableActions } from '../../provider/TableActions'
import { getValidationClass } from '../../../../core/utils/getValidationClass'

export const HeaderFilter = memo<HeaderFilterProps>(({ filterField, id }) => {
    const refTableProps = useTableRefProps()
    const { onChangeFilter } = useTableActions()
    const filterValue = refTableProps.current.filterValue?.[id]
    const filterError = useMemo(() => {
        const message = refTableProps.current.filterErrors?.[id]?.[0]

        return ({
            message,
            validationClass: getValidationClass(message),
        })
        // Если не указать поле filterErrors которое берется из реф объекта, не будет отрабатывать при ререндере
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [id, refTableProps.current.filterErrors])

    return (
        <AdvancedTableFilter
            id={id}
            onFilter={onChangeFilter}
            value={filterValue}
            field={filterField}
            error={filterError}
        />
    )
})

HeaderFilter.displayName = 'HeaderFilter'

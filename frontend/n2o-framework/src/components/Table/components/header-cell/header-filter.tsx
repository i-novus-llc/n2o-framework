import React, { memo } from 'react'
import { useSelector } from 'react-redux'

import { HeaderFilterProps } from '../../types/props'
// @ts-ignore - отсутсвует типизация
// eslint-disable-next-line import/no-named-as-default
import AdvancedTableFilter from '../filter/AdvancedTableFilter'
import { useChangeFilter } from '../../hooks/useChangeFilter'
import { dataSourceModelByPrefixSelector } from '../../../../ducks/datasource/selectors'
import { ModelPrefix } from '../../../../core/datasource/const'
import { State } from '../../../../ducks/State'
import { useTableRefProps } from '../../provider/TableRefProps'

export const HeaderFilter = memo<HeaderFilterProps>(({ filterControl, id }) => {
    const refTableProps = useTableRefProps()
    const filterValue = useSelector((state: State) => (
        // @ts-ignore - при получении занчения из ModelPrefix.filter всегда приходит объект или undefined
        dataSourceModelByPrefixSelector(refTableProps.current.id, ModelPrefix.filter)(state)?.[id]
    ))

    const onFilter = useChangeFilter(refTableProps.current.id)

    return (
        <AdvancedTableFilter
            id={id}
            onFilter={onFilter}
            value={filterValue}
            control={filterControl}
        />
    )
})

HeaderFilter.displayName = 'HeaderFilter'

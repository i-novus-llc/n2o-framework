import React, { useContext } from 'react'

import { FilterButton } from '../FilterButton/FilterButton'
import { WidgetFilterContext } from '../../../../WidgetFilters'
import { type Props } from '../../../../../buttons/FactoryStandardButton'

export function FilterSearchButton(props: Props) {
    const { search }: { search(): void } = useContext(WidgetFilterContext)

    return <section><FilterButton {...props} onClick={search} /></section>
}

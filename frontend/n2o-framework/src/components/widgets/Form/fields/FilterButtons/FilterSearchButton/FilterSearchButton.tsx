import React, { useContext } from 'react'

import { FilterButton, Props as FilterButtonProps } from '../FilterButton/FilterButton'
import { WidgetFilterContext } from '../../../../WidgetFilters'

export type Props = FilterButtonProps

export function FilterSearchButton(props: Props) {
    const { search }: { search(): void } = useContext(WidgetFilterContext)

    return <section><FilterButton {...props} onClick={search} /></section>
}

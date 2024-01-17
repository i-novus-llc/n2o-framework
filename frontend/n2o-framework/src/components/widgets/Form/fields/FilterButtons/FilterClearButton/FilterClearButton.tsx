import React, { useContext, useCallback } from 'react'

import { FilterButton, Props as FilterButtonProps } from '../FilterButton/FilterButton'
// @ts-ignore ignore import error from js file
import { WidgetFilterContext } from '../../../../WidgetFilters'

export type Props = FilterButtonProps

export function FilterClearButton(props: Props) {
    const { reset }: { reset(): void } = useContext(WidgetFilterContext)

    const onClick = useCallback(() => reset(), [reset])

    return <section><FilterButton {...props} onClick={onClick} /></section>
}

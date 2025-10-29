import React, { useContext, useCallback } from 'react'

import { FilterButton } from '../FilterButton/FilterButton'
import { WidgetFilterContext } from '../../../../WidgetFilters'
import { type Props } from '../../../../../buttons/FactoryStandardButton'

export function FilterClearButton(props: Props) {
    const { reset }: { reset(): void } = useContext(WidgetFilterContext)

    const onClick = useCallback(() => reset(), [reset])

    return <section><FilterButton {...props} onClick={onClick} /></section>
}

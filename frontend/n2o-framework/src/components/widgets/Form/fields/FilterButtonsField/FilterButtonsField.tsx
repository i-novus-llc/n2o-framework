import React, { useContext } from 'react'

import { Buttons, ButtonsProps } from '../../../../snippets/Filter/Buttons'
import { WidgetFiltersContext } from '../../../WidgetFiltersContext'
import { FieldProps } from '../types'

/**
 * Компонент обертка для встраивания кнопок фильтра в любое место формы, как Field.
 * Методы берутся из контекста, поэтому обязательно использовать внутри WidgetFilters
 */

export interface FilterButtonsFieldProps extends FieldProps, Omit<ButtonsProps, 'onSearch' | 'onReset'> {
    fetchOnClear?: boolean
}

export function FilterButtonsField(props: FilterButtonsFieldProps) {
    const {
        className,
        visible = true,
        disabled = false,
        searchLabel,
        resetLabel,
        fetchOnClear = true,
    } = props
    const { filter, reset } = useContext(WidgetFiltersContext)

    return (
        <Buttons
            className={className}
            visible={visible}
            disabled={disabled}
            searchLabel={searchLabel}
            resetLabel={resetLabel}
            onSearch={filter}
            onReset={() => reset(fetchOnClear)}
        />
    )
}

export default FilterButtonsField

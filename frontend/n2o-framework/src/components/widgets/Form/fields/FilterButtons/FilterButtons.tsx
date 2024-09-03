import React from 'react'
import { ButtonGroup } from 'reactstrap'

import { isEmptyModel } from '../../../../../utils/isEmptyModel'

import { FilterSearchButton, Props as SearchProps } from './FilterSearchButton/FilterSearchButton'
import { FilterClearButton, Props as ClearProps } from './FilterClearButton/FilterClearButton'

interface Props {
    search: SearchProps
    clear: ClearProps
    model: Record<string, unknown>
    visible?: boolean
}

export function FilterButtons({ search, clear, model, visible = true }: Props) {
    if (!visible) { return null }

    return (
        <ButtonGroup>
            <FilterSearchButton {...search} />
            <FilterClearButton {...clear} disabled={isEmptyModel(model)} />
        </ButtonGroup>
    )
}

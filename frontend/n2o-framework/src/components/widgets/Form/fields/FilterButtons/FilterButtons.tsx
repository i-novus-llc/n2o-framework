import React from 'react'
import isEmpty from 'lodash/isEmpty'
import { ButtonGroup } from 'reactstrap'

import { FilterSearchButton, Props as SearchProps } from './FilterSearchButton/FilterSearchButton'
import { FilterClearButton, Props as ClearProps } from './FilterClearButton/FilterClearButton'

interface Props {
    search: SearchProps
    clear: ClearProps
    model: Record<string, unknown>
    visible?: boolean
}

export function FilterButtons(props: Props) {
    const { search, clear, model, visible = true } = props

    if (!visible) { return null }

    return (
        <ButtonGroup>
            <FilterSearchButton {...search} />
            <FilterClearButton {...clear} disabled={isEmpty(model)} />
        </ButtonGroup>
    )
}

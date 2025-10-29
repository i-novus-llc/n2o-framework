import React from 'react'
import { ButtonGroup } from 'reactstrap'

import { isEmptyModel } from '../../../../../utils/isEmptyModel'
import { type Model } from '../../../../../ducks/models/selectors'
import { type Props as FilterButtonProps } from '../../../../buttons/FactoryStandardButton'

import { FilterSearchButton } from './FilterSearchButton/FilterSearchButton'
import { FilterClearButton } from './FilterClearButton/FilterClearButton'

interface Props {
    search: FilterButtonProps
    clear: FilterButtonProps
    model: Model
    visible?: boolean
}

export function FilterButtons(props: Props) {
    const { search, clear, model, visible = true } = props

    if (!visible) { return null }

    return (
        <ButtonGroup>
            <FilterSearchButton {...search} />
            <FilterClearButton {...clear} disabled={isEmptyModel(model)} />
        </ButtonGroup>
    )
}

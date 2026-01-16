import React, { VFC } from 'react'

import { Selection } from '../../../../ducks/table/Table'
import { CustomCellComponentProps } from '../../types/props'

import { RadioCell } from './radio'
import { CheckboxCell } from './checkbox'

export const SelectionCell: VFC<CustomCellComponentProps> = ({ selection, ...props }) => (
    selection === Selection.Radio
        ? <RadioCell {...props} />
        : <CheckboxCell {...props} />
)

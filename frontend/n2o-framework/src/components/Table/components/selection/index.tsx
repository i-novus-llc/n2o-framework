import React, { VFC } from 'react'

import { SelectionType } from '../../enum'
import { CustomCellComponentProps } from '../../types/props'

import { RadioCell } from './radio'
import { CheckboxCell } from './checkbox'

export const SelectionCell: VFC<CustomCellComponentProps> = ({ selection, ...props }) => (
    selection === SelectionType.Radio
        ? <RadioCell {...props} />
        : <CheckboxCell {...props} />
)

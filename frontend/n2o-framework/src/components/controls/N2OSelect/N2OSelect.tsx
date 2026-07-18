import React from 'react'
import classNames from 'classnames'

import { InputSelect } from '../InputSelect/InputSelect'

import { type N2OSelectProps, Type } from './types'

export function N2OSelect(props: N2OSelectProps) {
    const { type, className } = props

    const multiProps = {} as Pick<N2OSelectProps, 'maxTagCount' | 'multiSelect' | 'hasCheckboxes'>

    if (type === Type.CHECKBOXES) {
        multiProps.maxTagCount = 0
        multiProps.multiSelect = true
        multiProps.hasCheckboxes = true
    }

    return (
        <InputSelect
            {...props}
            className={classNames('n2o-select', className)}
            {...multiProps}
            readOnly
        />
    )
}

N2OSelect.displayName = 'N2OSelect'

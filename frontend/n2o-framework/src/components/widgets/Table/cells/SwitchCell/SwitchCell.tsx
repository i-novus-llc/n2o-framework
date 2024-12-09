import React from 'react'
import get from 'lodash/get'

import { Factory } from '../../../../../core/factory/Factory'
import { CELLS } from '../../../../../core/factory/factoryLevels'

import { type Props } from './types'

export function SwitchCell({
    model,
    switchFieldId,
    switchList,
    switchDefault,
    style,
    ...props
}: Props) {
    const cellType = get(model, switchFieldId) as string
    const cellProps = get(switchList, cellType, switchDefault)
    const cellStyle = get(cellProps, 'elementAttributes.style', {}) as Props['style']

    return <Factory level={CELLS} model={model} {...props} {...cellProps} style={{ ...style, ...cellStyle }} />
}

export default SwitchCell

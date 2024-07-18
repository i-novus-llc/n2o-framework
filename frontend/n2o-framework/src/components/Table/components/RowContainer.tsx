import React, { memo, useMemo } from 'react'

import { RowContainerProps } from '../types/props'
import { useResolved, useResolver } from '../../../core/Expression/useResolver'

import { RowResolver } from './RowResolver'

export const RowContainer = memo<RowContainerProps>((props) => {
    const {
        elementAttributes,
        data,
        click,
        disabled,
        ...rest
    } = props
    const resolveProps = useResolver()
    const resolvedElementAttributes = useResolved(elementAttributes, data)
    const resolvedClick = useMemo(() => {
        if (disabled) { return undefined }

        if (typeof click === 'undefined') { return click }

        const { enablingCondition } = click

        if (!enablingCondition || resolveProps(`\`${enablingCondition}\``, data)) { return click }

        return undefined
    }, [disabled, click, data, resolveProps])

    return (
        <RowResolver
            {...rest}
            data={data}
            elementAttributes={resolvedElementAttributes}
            click={resolvedClick}
            disabled={disabled}
        />
    )
})

RowContainer.displayName = 'RowContainer'

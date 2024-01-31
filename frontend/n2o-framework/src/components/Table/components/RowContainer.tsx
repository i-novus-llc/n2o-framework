import React, { memo, useMemo } from 'react'

import { RowContainerProps } from '../types/props'
// @ts-ignore - отсутствуют типы
import propsResolver from '../../../utils/propsResolver'
import evalExpression from '../../../utils/evalExpression'
import { Row } from '../types/row'

import { RowResolver } from './RowResolver'

const resolveClick = (click: Row['click'], data: RowContainerProps['data']) => {
    if (typeof click === 'undefined') { return click }

    const { enablingCondition } = click

    if (!enablingCondition || evalExpression(enablingCondition, data)) { return click }

    return undefined
}

export const RowContainer = memo<RowContainerProps>((props) => {
    const {
        elementAttributes,
        data,
        click,
        hasSecurityAccess,
        ...rest
    } = props

    const resolvedElementAttributes = useMemo(() => (
        propsResolver(elementAttributes, data) || {}
    ), [elementAttributes, data])

    return (
        <RowResolver
            {...rest}
            data={data}
            elementAttributes={resolvedElementAttributes}
            click={hasSecurityAccess ? resolveClick(click, data) : undefined}
            hasSecurityAccess={hasSecurityAccess}
        />
    )
})

RowContainer.displayName = 'RowContainer'

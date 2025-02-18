import React from 'react'
import get from 'lodash/get'
import { StatusText } from '@i-novus/n2o-components/lib/display/StatusText/StatusText'

import { withTooltip } from '../../withTooltip'

import { type Props } from './types'

/**
 * Ячейка таблицы типа статус
 * @reactProps {string} id
 * @reactProps {object} model - модель данных
 * @reactProps {string} fieldKey - ключ модели для этой ячейки
 * @reactProps {string} color - цветовая схема бейджа(["primary", "secondary", "success", "danger", "warning", "info", "light", "dark", "white"])
 * @example
 * <StatusCell model={model} filedKey={'name'} color="info"/>
 */

function StatusCellBody({
    id,
    className,
    color,
    fieldKey,
    textPosition,
    forwardedRef,
    visible = true,
    model,
}: Props) {
    if (!visible) { return null }

    const statusText = get(model, fieldKey || id) as string | undefined

    return (
        <div ref={forwardedRef} className="d-inline-flex">
            <StatusText
                text={statusText}
                textPosition={textPosition}
                color={color}
                className={className}
            />
        </div>
    )
}

export const StatusCell = withTooltip(StatusCellBody)
export default StatusCell

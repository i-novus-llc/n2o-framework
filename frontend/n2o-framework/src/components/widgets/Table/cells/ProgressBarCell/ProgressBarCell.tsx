import React from 'react'
import { Progress } from 'reactstrap'

import { withTooltip } from '../../withTooltip'

import { type Props } from './types'

/**
 * Ячейка таблицы с прогресс баром
 * @reactProps {string} id - id ячейки
 * @reactProps {object} model - модель строки
 * @reactProps {boolean} animated - флаг animated
 * @reactProps {boolean} striped - флаг striped
 * @reactProps {string} color - цвет прогресс бара
 * @reactProps {string} size - размер прогресс бара
 */
function ProgressBarCellBody({
    id,
    color,
    model,
    size = 'default',
    visible = true,
    animated = false,
    striped = false,
}: Props) {
    if (!visible) { return null }

    return <Progress value={model[id]} className={size} animated={animated} striped={striped} color={color} />
}

const ProgressBarCell = withTooltip(ProgressBarCellBody)

ProgressBarCell.displayName = 'ProgressBarCell'

export { ProgressBarCell }
export default ProgressBarCell

import React from 'react'
import get from 'lodash/get'
import map from 'lodash/map'
import isArray from 'lodash/isArray'

import { WithCell } from '../../withCell'
import { DefaultCell } from '../DefaultCell'
import { getNoun } from '../../../../../utils/getNoun'
import { Tooltip } from '../../../../snippets/Tooltip/TooltipHOC'

import { ListTextCellTrigger } from './ListTextCellTrigger'
import { replacePlaceholder } from './utils'
import { ListTextCellProps as Props } from './types'

function ListTextCellBody({
    model,
    label,
    oneLabel,
    fewLabel,
    manyLabel,
    fieldKey = '',
    disabled = false,
    trigger = 'hover',
    labelDashed = false,
    placement = 'bottom',
}: Props) {
    const list = get(model, fieldKey) as string[]

    if (!list.length) { return <DefaultCell disabled={disabled} className="list-text-cell" /> }

    const tooltipVisible = model && fieldKey && isArray(list)
    const listLength = tooltipVisible ? list.length : 0

    const currentLabel = getNoun(listLength, oneLabel || label, fewLabel || label, manyLabel || label)

    const hint = map(list, (item, index) => (
        <div key={index} className="list-text-cell__tooltip-container__body">{item}</div>
    ))

    return (
        <DefaultCell disabled={disabled} className="list-text-cell">
            {list.length === 1
                ? list[0]
                : (
                    <Tooltip hint={tooltipVisible ? hint : null} placement={placement} trigger={trigger} className="n2o-cell-tooltip">
                        <ListTextCellTrigger
                            label={replacePlaceholder(currentLabel, listLength)}
                            labelDashed={labelDashed}
                        />
                    </Tooltip>
                )
            }
        </DefaultCell>
    )
}

const ListTextCell = WithCell(ListTextCellBody)

ListTextCell.displayName = 'ListTextCell'
export { ListTextCell }
export default ListTextCell

import React, { useCallback } from 'react'
import get from 'lodash/get'
import set from 'lodash/set'

import { WithCell } from '../../withCell'
import { Rating } from '../../../../controls/Rating/Rating'

import { type Props } from './types'

const RatingCellBody = ({
    max,
    half,
    showTooltip,
    fieldKey,
    id,
    model,
    callAction,
    visible = true,
    readonly = false,
}: Props) => {
    const handleChange = useCallback(
        (rating) => {
            const data = set({ ...model }, fieldKey || id, rating)

            callAction(data)
        },
        [callAction, model, fieldKey, id],
    )

    if (!visible) { return null }

    return (
        <Rating
            max={max}
            rating={model && get(model, fieldKey || id)}
            half={half}
            showTooltip={showTooltip}
            onChange={handleChange}
            readonly={readonly}
        />
    )
}

export const RatingCell = WithCell(RatingCellBody)
export default RatingCell

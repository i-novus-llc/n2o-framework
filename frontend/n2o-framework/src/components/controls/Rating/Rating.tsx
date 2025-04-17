import React from 'react'
import { Rating as SnippetRating, type Props as SnippetRatingProps } from '@i-novus/n2o-components/lib/inputs/Rating'

import { NOOP_FUNCTION } from '../../../utils/emptyTypes'

export const Rating = ({
    value,
    rating = 0,
    max = 5,
    half = false,
    showTooltip = false,
    onChange = NOOP_FUNCTION,
    readonly,
    disabled,
}: SnippetRatingProps) => (
    <SnippetRating
        value={value}
        max={max}
        rating={rating}
        half={half}
        showTooltip={showTooltip}
        onChange={onChange}
        readonly={readonly || disabled}
    />
)

export default Rating

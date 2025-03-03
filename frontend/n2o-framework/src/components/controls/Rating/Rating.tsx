import React from 'react'
import { Rating as SnippetRating, type Props as SnippetRatingProps } from '@i-novus/n2o-components/lib/inputs/Rating'

export const Rating = ({
    value,
    rating = 0,
    max = 5,
    half = false,
    showTooltip = false,
    onChange = () => {},
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

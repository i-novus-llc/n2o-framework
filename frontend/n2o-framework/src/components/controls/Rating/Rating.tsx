import React from 'react'

import { SnippetRating, Props as SnippetRatingProps } from '../../snippets/SnippetRating/SnippetRating'

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

import React from 'react'
import PropTypes from 'prop-types'

import { SnippetRating } from '../../snippets/SnippetRating/SnippetRating'

const Rating = ({
    value,
    rating,
    max,
    half,
    showTooltip,
    onChange,
    readonly,
}) => (
    <SnippetRating
        value={value}
        max={max}
        rating={rating}
        half={half}
        showTooltip={showTooltip}
        onChange={onChange}
        readonly={readonly}
    />
)

Rating.propTypes = {
    /**
     * Максимальное значение
     */
    max: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
    /**
     * Значение
     */
    rating: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
    value: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
    /**
     * Флаг включения выбора по половинке
     */
    half: PropTypes.bool,
    /**
     * Флаг показа подсказки
     */
    showTooltip: PropTypes.bool,
    /**
     * Callback на изменение
     */
    onChange: PropTypes.func,
    /**
     * Флаг только для чтения
     */
    readonly: PropTypes.bool,
}

Rating.defaultProps = {
    max: 5,
    half: false,
    rating: 0,
    showTooltip: false,
    onChange: () => {},
}

export default Rating

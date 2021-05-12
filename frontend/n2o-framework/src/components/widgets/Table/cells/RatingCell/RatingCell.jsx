import React, { useCallback } from 'react'
import PropTypes from 'prop-types'
import get from 'lodash/get'
import set from 'lodash/set'

import withCell from '../../withCell'
import Rating from '../../../../controls/Rating/Rating'

const RatingCell = ({
    visible,
    max,
    half,
    showTooltip,
    fieldKey,
    id,
    readonly,
    model,
    callAction,
}) => {
    const handleChange = useCallback(
        (rating) => {
            const data = set(
                {
                    ...model,
                },
                fieldKey || id,
                rating,
            )

            callAction(data)
        },
        [callAction, model, fieldKey, id],
    )

    return visible ? (
        <Rating
            max={max}
            rating={model && get(model, fieldKey || id)}
            half={half}
            showTooltip={showTooltip}
            onChange={handleChange}
            readonly={readonly}
        />
    ) : null
}

RatingCell.propTypes = {
    /**
   * ID ячейки
   */
    id: PropTypes.string,
    /**
   * Модель данных
   */
    model: PropTypes.object,
    /**
   * Ключ значения из модели
   */
    fieldKey: PropTypes.string,
    /**
   * Флаг видимости
   */
    visible: PropTypes.bool,
    /**
   * Максимально значение рейтинга
   */
    max: PropTypes.number,
    /**
   * Значение рейтинга
   */
    rating: PropTypes.number,
    /**
   * Дробные числа рейтинга
   */
    half: PropTypes.bool,
    /**
   * Флаг показа подсказки
   */
    showTooltip: PropTypes.bool,
    /**
   * Флаг только для чтения
   */
    readonly: PropTypes.bool,
}

RatingCell.defaultProps = {
    visible: true,
    readonly: false,
}

export { RatingCell }
export default withCell(RatingCell)

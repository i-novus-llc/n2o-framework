import React from 'react'
import PropTypes from 'prop-types'
import compose from 'recompose/compose'
import set from 'lodash/set'
import get from 'lodash/get'
import withHandlers from 'recompose/withHandlers'

import withCell from '../../withCell'
import Rating from '../../../../controls/Rating/Rating'

const RatingCell = ({
    visible,
    max,
    half,
    showTooltip,
    handleChange,
    model,
    fieldKey,
    id,
}) => (visible ? (
    <Rating
        max={max}
        rating={model && get(model, fieldKey || id)}
        half={half}
        showTooltip={showTooltip}
        onChange={rating => handleChange(rating)}
    />
) : null)

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
}

RatingCell.defaultProps = {
    visible: true,
}

export { RatingCell }
export default compose(
    withCell,
    withHandlers({
        handleChange: ({
            callActionImpl,
            action,
            model,
            fieldKey,
            id,
        }) => (rating) => {
            const data = set(
                {
                    ...model,
                },
                fieldKey || id,
                rating,
            )

            callActionImpl(rating, { action, model: data })
        },
    }),
)(RatingCell)

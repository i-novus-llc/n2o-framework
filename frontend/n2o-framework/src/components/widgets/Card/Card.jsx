import React from 'react'
import PropTypes from 'prop-types'
import map from 'lodash/map'
import flow from 'lodash/flow'

// eslint-disable-next-line import/no-named-as-default
import CardItem from './CardItem'
import CardLayout from './CardLayout'

/**
 * Контейнер для карточек
 * @param {boolean} linear - Линейное отображение каждого элемента
 * @param {boolean} circle - Закругление изображения
 * @param {array} items - данные
 * @param {boolean} inverse: - Инверсия цвета текста,
 * @param {boolean} outline: - применение свойства color к бордеру,
 * @param {node} children - элемент потомок компонента Card
 * @param {...any} rest
 * @returns {*}
 * @constructor
 */
function Card({ linear, circle, items, children, outline, inverse, ...rest }) {
    const setPropsAllItem = x => ({
        ...x,
        linear,
        circle,
        outline,
        inverse,
    })

    return (
        <CardLayout {...rest}>
            {children ||
        map(
            items,
            flow(
                setPropsAllItem,
                CardItem,
            ),
        )}
        </CardLayout>
    )
}

Card.propTypes = {
    /**
   * Массив элементов
   */
    items: PropTypes.array,
    /**
   * Линейное отображение каждого элемента
   */
    linear: PropTypes.bool,
    /**
   * Закругление изображения
   */
    circle: PropTypes.bool,
    children: PropTypes.node,
    /**
   * Применение свойства color к бордеру,
   */
    outline: PropTypes.bool,
    /**
   * Инверсия цвета текста
   */
    inverse: PropTypes.bool,
}

Card.defaultProps = {
    linear: false,
    inverse: false,
    outline: false,
}

Card.Layout = CardLayout
Card.Item = CardItem

export default Card

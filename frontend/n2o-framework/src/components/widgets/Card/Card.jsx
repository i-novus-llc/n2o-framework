import React from 'react'
import map from 'lodash/map'
import flow from 'lodash/flow'

import CardItem from './CardItem'
import CardLayout from './CardLayout'

/**
 * Контейнер для карточек
 * @param linear {boolean} - Линейное отображение каждого элемента
 * @param circle {boolean} - Закругление изображения
 * @param items {array} - данные
 * @param inverse {boolean} - Инверсия цвета текста,
 * @param outline {boolean} - применение свойства color к бордеру,
 * @param {node} children - элемент потомок компонента Card
 * @param rest {...any} rest
 * @returns {*}
 * @constructor
 */
function Card({ linear = false, circle, items, children, outline = false, inverse = false, ...rest }) {
    const setPropsAllItem = item => ({ ...item, linear, circle, outline, inverse })

    return <CardLayout {...rest}>{children || map(items, flow(setPropsAllItem, CardItem))}</CardLayout>
}

Card.Layout = CardLayout
Card.Item = CardItem

export default Card

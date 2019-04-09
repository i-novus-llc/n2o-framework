import React from 'react';
import PropTypes from 'prop-types';
import { map, flow, some, isEmpty } from 'lodash';
import CardItem from './CardItem';
import CardLayout from './CardLayout';

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
  });

  return (
    <CardLayout {...rest}>
      {children ||
        map(
          items,
          flow(
            setPropsAllItem,
            CardItem
          )
        )}
    </CardLayout>
  );
}

Card.propTypes = {
  items: PropTypes.array,
  linear: PropTypes.bool,
  circle: PropTypes.bool,
  children: PropTypes.node,
  outline: PropTypes.bool,
  inverse: PropTypes.bool,
};

Card.defaultProps = {
  linear: false,
  inverse: false,
  outline: false,
};

Card.Layout = CardLayout;
Card.Item = CardItem;

export default Card;

import React from 'react';
import { mapProps } from 'recompose';
import PropTypes from 'prop-types';
import cx from 'classnames';
import { Card, CardImg, CardText, CardBody, CardTitle, CardSubtitle } from 'reactstrap';
import { extend, map, pick, uniqueId } from 'lodash';

/**
 * Карточка
 * @param (string) header - заголовок
 * @param (string) meta - подзаголовок
 * @param (string) text - отображаемый текст
 * @param (string) image - url изображения
 * @param (node) extra - часть карточки для расширения другими обьектати
 * @param (string) tag - Возможность передачи компонента для переопределения элемента по умолчанию
 * @param (string) color - цвет карточки
 * @param (string) inverse - инвертировать цвет текста
 * @param (string) outline - применить свойство color к бордеру карточки.
 * @param (string) className - кастомный класс
 * @param (object) datasource - данные
 * @param (array) row - Порядок следования и отображения элементов карточки.
 * Пример row = {['header', 'meta', 'text', 'image', 'extra']}
 * @returns {*}
 * @constructor
 */

const items = {
  header: ({ header }) =>
    header && (
      <CardBody key={uniqueId('title_')}>
        <CardTitle>{header}</CardTitle>
      </CardBody>
    ),
  meta: ({ meta }) =>
    meta && (
      <CardBody key={uniqueId('subtitle_')}>
        <CardSubtitle>{meta}</CardSubtitle>
      </CardBody>
    ),
  text: ({ text }) =>
    text && (
      <CardBody key={uniqueId('text_')}>
        <CardText className="text">{text}</CardText>
      </CardBody>
    ),
  image: ({ image }) =>
    image && (
      <div key={uniqueId('image_')} className="card-image">
        <CardImg src={image} />
      </div>
    ),
  extra: ({ extra }) => extra && <CardBody key={uniqueId('extra_')}>{extra}</CardBody>
};

function CardItem(props) {
  const { children, rows, linear, className, circle } = props;
  const orderedItems = map(rows, row => items[row](props));
  const cardProps = pick(props, ['tag', 'inverse', 'outline', 'color']);

  return (
    <Card className={cx('n2o-card', { linear, circle }, className)} {...cardProps}>
      {children || orderedItems}
    </Card>
  );
}

CardItem.propTypes = {
  header: PropTypes.string,
  meta: PropTypes.string,
  text: PropTypes.string,
  image: PropTypes.string,
  linear: PropTypes.bool,
  circle: PropTypes.bool,
  extra: PropTypes.oneOfType([PropTypes.node, PropTypes.string]),
  children: PropTypes.node,
  tag: PropTypes.oneOfType([PropTypes.func, PropTypes.string]),
  inverse: PropTypes.bool,
  outline: PropTypes.bool,
  color: PropTypes.string,
  className: PropTypes.string,
  rows: PropTypes.array,
  datasource: PropTypes.object
};

CardItem.defaultProps = {
  rows: ['image', 'header', 'meta', 'text', 'extra'],
  linear: false,
  inverse: false,
  outline: false
};

export default mapProps(({ datasource, ...rest }) => extend(datasource, rest))(CardItem);

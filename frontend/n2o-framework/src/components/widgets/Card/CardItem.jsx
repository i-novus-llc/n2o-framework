import React from 'react'
import { compose, setDisplayName, mapProps } from 'recompose'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import extend from 'lodash/extend'
import map from 'lodash/map'
import pick from 'lodash/pick'
import uniqueId from 'lodash/uniqueId'
import Card from 'reactstrap/lib/Card'
import CardImg from 'reactstrap/lib/CardImg'
import CardText from 'reactstrap/lib/CardText'
import CardBody from 'reactstrap/lib/CardBody'
import CardTitle from 'reactstrap/lib/CardTitle'
import CardSubtitle from 'reactstrap/lib/CardSubtitle'

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
    header: ({ header }) => header && (
        <CardBody key={uniqueId('title_')}>
            <CardTitle>{header}</CardTitle>
        </CardBody>
    ),
    meta: ({ meta }) => meta && (
        <CardBody key={uniqueId('subtitle_')}>
            <CardSubtitle>{meta}</CardSubtitle>
        </CardBody>
    ),
    text: ({ text }) => text && (
        <CardBody key={uniqueId('text_')}>
            <CardText className="text">{text}</CardText>
        </CardBody>
    ),
    image: ({ image }) => image && (
        <div key={uniqueId('image_')} className="card-image">
            <CardImg src={image} />
        </div>
    ),
    extra: ({ extra }) => extra && <CardBody key={uniqueId('extra_')}>{extra}</CardBody>,
}

function CardItem(props) {
    const { children, rows, linear, className, circle } = props
    const orderedItems = map(rows, row => items[row](props))
    const cardProps = pick(props, ['tag', 'inverse', 'outline', 'color'])

    return (
        <Card
            className={classNames('n2o-card', { linear, circle }, className)}
            {...cardProps}
        >
            {children || orderedItems}
        </Card>
    )
}

CardItem.propTypes = {
    /**
     * Заголовок
     */
    header: PropTypes.string,
    /**
     * Подзаголовок
     */
    meta: PropTypes.string,
    /**
     * Отображаемый текст
     */
    text: PropTypes.string,
    /**
     * URL изображения
     */
    image: PropTypes.string,
    /**
     * Линейное отображение
     */
    linear: PropTypes.bool,
    /**
     * Закругление изображения
     */
    circle: PropTypes.bool,
    /**
     * Часть карточки для расширения другими обьектати
     */
    extra: PropTypes.oneOfType([PropTypes.node, PropTypes.string]),
    children: PropTypes.node,
    /**
     * Возможность передачи компонента для переопределения элемента по умолчанию
     */
    tag: PropTypes.oneOfType([PropTypes.func, PropTypes.string]),
    /**
     * Инвертировать цвет текста
     */
    inverse: PropTypes.bool,
    /**
     * Применить свойство color к бордеру карточки
     */
    outline: PropTypes.bool,
    /**
     * Цвет карточки
     */
    color: PropTypes.string,
    /**
     * Класс
     */
    className: PropTypes.string,
    rows: PropTypes.array,
    /**
     * Данные
     */
    datasource: PropTypes.object,
}

CardItem.defaultProps = {
    rows: ['image', 'header', 'meta', 'text', 'extra'],
    linear: false,
    inverse: false,
    outline: false,
}

export { CardItem }
export default compose(
    setDisplayName('CardItem'),
    mapProps(({ datasource, ...rest }) => extend(datasource, rest)),
)(CardItem)

import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import map from 'lodash/map'
import get from 'lodash/get'

import CardsCell from './CardsCell'

const getJustifyContent = (align) => {
    if (align === 'top') {
        return 'flex-start'
    }
    if (align === 'bottom') {
        return 'flex-end'
    }

    return 'center'
}

/**
 * Cards
 * @reactProps {string} className - имя css класса карточки
 * @reactProps {array} cards - массив объектов cell из которых состоит виджет
 * @reactProps {array} data - данные объектов cell
 * @reactProps {number} id - id виджета
 * @reactProps {string} align - позиция элементов по горизонтали
 */
export function Cards(props) {
    const {
        cards,
        className,
        data,
        id,
        onResolve,
        dispatch,
        align,
        height,
    } = props

    const style = {
        justifyContent: getJustifyContent(align),
    }

    const renderCard = (dataItem, index) => (
        <div className={classNames('n2o-cards col-12', className)} style={{ height }}>
            {map(cards, card => renderCardsItem(card, dataItem, index))}
        </div>
    )

    const renderCell = (cell, dataItem, index) => (
        <CardsCell
            index={index}
            widgetId={id}
            model={dataItem}
            onResolve={onResolve}
            dispatch={dispatch}
            {...cell}
        />
    )

    const renderCardsItem = (element, dataItem, index) => {
        const { content = [], col } = element

        return (
            <div className={classNames('n2o-cards__item', `col-${col}`)} style={style}>
                {map(content, (cell, { width }) => (get(cell, 'src') === 'ImageCell' ? (
                    <div className="n2o-cards__image" style={width ? { width } : {}}>
                        {renderCell(cell, dataItem, index)}
                    </div>
                ) : (
                    renderCell(cell, dataItem, index)
                )))}
            </div>
        )
    }

    return (
        <div className={classNames('n2o-cards__container col-12', className)}>
            {data && data.length
                ? map(data, (item, index) => renderCard(item, index))
                : ''}
        </div>
    )
}

Cards.defaultProps = {
    height: '450px',
}

Cards.propTypes = {
    /**
     * имя css класса карточки
     */
    className: PropTypes.string,
    /**
     * массив объектов cell из которых состоит виджет
     */
    cards: PropTypes.array,
    /**
     * данные объектов cell
     */
    data: PropTypes.array,
    /**
     * id виджета
     */
    id: PropTypes.number,
    /**
     * позиция элементов по горизонтали
     */
    align: PropTypes.string,
    /**
     * высота компонента
     */
    height: PropTypes.string,
    onResolve: PropTypes.func,
    dispatch: PropTypes.func,
}

export default Cards

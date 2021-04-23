import React from 'react'
import PropTypes from 'prop-types'
import cn from 'classnames'
import map from 'lodash/map'
import get from 'lodash/get'

import CardsCell from './CardsCell'

/**
 * Cards
 * @reactProps {string} className - имя css класса карточки
 * @reactProps {array} cards - массив объектов cell из которых состоит виджет
 * @reactProps {array} data - данные объектов cell
 * @reactProps {number} id - id виджета
 * @reactProps {string} align - позиция элементов по горизонтали
 */

function Cards(props) {
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
        justifyContent:
      align === 'top'
          ? 'flex-start'
          : align === 'bottom'
              ? 'flex-end'
              : 'center',
    }

    const renderCard = (dataItem, index) => (
        <div className={cn('n2o-cards col-12', className)} style={{ height }}>
            {map(cards, card => renderCardsItem(card, dataItem, index))}
        </div>
    )

    const renderCell = (cell, dataItem, index) => (
        <CardsCell
            className={cn('n2o-cards__cell', cell.className)}
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
            <div className={cn('n2o-cards__item', `col-${col}`)} style={style}>
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
        <div className={cn('n2o-cards__container col-12', className)}>
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
}

export default Cards

import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

import { Card } from './Card'

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

    if (!data?.length || !cards?.length) {
        return null
    }

    const getJustifyContent = (align) => {
        switch (align) {
            case 'top':
                return 'flex-start'
            case 'bottom':
                return 'flex-end'
            default:
                return 'center'
        }
    }

    return (
        <div className={classNames('n2o-cards__container col-12', className)}>
            {data.map((model, index) => (
                <div className="n2o-cards col-12" style={{ height }}>
                    {cards.map(card => (
                        <Card
                            card={card}
                            model={model}
                            index={index}
                            id={id}
                            dispatch={dispatch}
                            onResolve={onResolve}
                            alignStyle={{ justifyContent: getJustifyContent(align) }}
                        />
                    ))}
                </div>
            ))}
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

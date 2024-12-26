import React from 'react'
import classNames from 'classnames'

import { Card } from './Card'
import { type CardsProps } from './types'

const getJustifyContent = (align: CardsProps['align']) => {
    switch (align) {
        case 'top':
            return 'flex-start'
        case 'bottom':
            return 'flex-end'
        default:
            return 'center'
    }
}

/**
 * Cards
 * @reactProps {string} className - имя css класса карточки
 * @reactProps {array} cards - массив объектов cell из которых состоит виджет
 * @reactProps {array} data - данные объектов cell
 * @reactProps {number} id - id виджета
 * @reactProps {string} align - позиция элементов по горизонтали
 * @reactProps {string} datasource - datasource key
 */
export function Cards({
    cards,
    className,
    data,
    id,
    onResolve,
    dispatch,
    align,
    datasource,
    height = '450px',
}: CardsProps) {
    if (!data?.length || !cards?.length) { return null }

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
                            datasource={datasource}
                        />
                    ))}
                </div>
            ))}
        </div>
    )
}

export default Cards

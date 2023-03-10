import React from 'react'
import classNames from 'classnames'
import PropTypes from 'prop-types'

import CardsCell from './CardsCell'

export function Card({ card, index, id, onResolve, dispatch, alignStyle, datasource, model = {} }) {
    const { content = [], col } = card

    return (
        <div className={classNames('n2o-cards__item', `col-${col}`)} style={alignStyle}>
            {content.map(({ component, className, style }) => {
                if (!component) {
                    return null
                }

                const wrapperClassName = classNames(
                    'd-flex',
                    className,
                    {
                        'n2o-cards__image': component.src === 'ImageCell',
                    },
                )

                const { id: key } = model

                return (
                    <div className={wrapperClassName} style={style} key={component.src + index}>
                        <CardsCell
                            index={index}
                            widgetId={id}
                            model={model}
                            onResolve={onResolve}
                            dispatch={dispatch}
                            datasource={datasource}
                            key={key}
                            {...component}
                        />
                    </div>
                )
            })}
        </div>
    )
}

Card.propTypes = {
    card: PropTypes.object,
    model: PropTypes.object,
    index: PropTypes.number,
    id: PropTypes.string,
    onResolve: PropTypes.func,
    dispatch: PropTypes.func,
    alignStyle: PropTypes.object,
    datasource: PropTypes.string,
}

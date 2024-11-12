import React, { CSSProperties } from 'react'
import classNames from 'classnames'

import { CardsCell, type Props as CardCellProps } from './CardsCell'

type CommonProps = 'index' | 'datasource' | 'model' | 'dispatch' | 'onResolve' | 'id' | 'className'

export interface Props extends Pick<CardCellProps, CommonProps> {
    card: {
        content: CardCellProps[]
        col: string
    }
    alignStyle: CSSProperties
}

export function Card({ card, index, id, onResolve, dispatch, alignStyle, datasource, model }: Props) {
    const { content = [], col } = card

    return (
        <div className={classNames('n2o-cards__item', `col-${col}`)} style={alignStyle}>
            {content.map(({ component, className, style }) => {
                if (!component) { return null }

                const wrapperClassName = classNames('d-flex', className, { 'n2o-cards__image': component?.src === 'ImageCell' })

                const { id: key } = model || {}

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

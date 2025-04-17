import React from 'react'
import { Button } from 'reactstrap'

import { NOOP_FUNCTION } from '../../../utils/emptyTypes'

import { type ListMoreButtonProps } from './types'

/**
 * Кнопка "Загрузить еще"
 */
export function ListMoreButton({ onClick = NOOP_FUNCTION, style }: ListMoreButtonProps) {
    return (
        <div style={style} className="n2o-widget-list-more-button">
            <Button onClick={onClick}>Загрузить еще</Button>
        </div>
    )
}

export default ListMoreButton

import React, { MouseEvent, ReactNode } from 'react'
import classNames from 'classnames'

import { RenderBodyPagingArgs } from './types'

type Props = {
    // функция клика по кнопке пагинации
    active?: boolean,
    // текущая страница
    disabled?: boolean,
    eventKey: RenderBodyPagingArgs['activePage'],
    label: string | number | ReactNode | null,
    // флаг неактивноти кнопки пагинации
    noBorder?: boolean,
    // текст внутри кнопки пагинации
    onSelect(eventKey: RenderBodyPagingArgs['activePage'], event: MouseEvent<HTMLElement>): void,
    tabIndex: number // флаг включения/выключения бордера
}

export const PaginationButton = ({ disabled, onSelect, eventKey, label, active, noBorder, tabIndex }: Props) => {
    const handleClick = (e: MouseEvent<HTMLElement>) => {
        e.preventDefault()

        if (disabled) {
            return
        }

        if (onSelect) {
            onSelect(eventKey, e)
        }
    }

    return (
        <li
            className={classNames('page-item', { active, disabled })}
        >
            <button
                type="button"
                onClick={handleClick}
                className={classNames('page-link link-button', noBorder ? 'no-border' : '')}
                tabIndex={tabIndex}
            >
                { label }
            </button>
        </li>
    )
}

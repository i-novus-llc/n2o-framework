import React, { MouseEvent, ReactNode } from 'react'
import classNames from 'classnames'

import { RenderBodyPagingArgs } from './types'

type Props = {
    tabIndex: number,
    eventKey: RenderBodyPagingArgs['activePage'],
    label: string | number | ReactNode | null, // текст внутри кнопки пагинации
    onSelect(eventKey: RenderBodyPagingArgs['activePage'], event: MouseEvent<HTMLElement>): void, // функция клика по кнопке пагинации
    active?: boolean, // текущая страница
    disabled?: boolean, // флаг неактивноти кнопки пагинации
    noBorder?: boolean, // флаг включения/выключения бордера
}

export const PaginationButton = (props: Props) => {
    const { disabled, onSelect, eventKey, label, active, noBorder, tabIndex } = props

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
        // eslint-disable-next-line jsx-a11y/no-noninteractive-element-interactions
        <li
            className={classNames('page-item', { active, disabled })}
            onClick={handleClick}
        >
            {/* eslint-disable-next-line jsx-a11y/anchor-is-valid,jsx-a11y/control-has-associated-label */}
            <a
                className={classNames('page-link', noBorder ? 'no-border' : '')}
                href="#"
                tabIndex={tabIndex}
            >
                { label }
            </a>
        </li>
    )
}

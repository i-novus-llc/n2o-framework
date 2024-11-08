import React, { ReactNode } from 'react'
import classNames from 'classnames'
import { Button } from 'reactstrap'

export interface Props {
    children: ReactNode
    id: string
    onClick(event: Event, id: string): void
    className?: string
    isToolBar?: boolean
    active?: boolean
    disabled?: boolean
}

/**
 * Компонент элемента меню для {@link Panel}
 * @reactProps {string} id - параметр, который уйдёт аргументов в событии по клику
 * @reactProps {boolean} active - выбран лм элемент
 * @reactProps {boolean} disabled - неактивен ли элемент
 * @reactProps {function} onClick - событие при клике
 * @reactProps (string} className - имя класса
 * @reactProps {boolean} isToolBar - флаг элемента тулбара
 * @reactProps {node} children - элемент вставляемый в PanelNavItem
 */
export function PanelNavItem({
    children,
    id,
    onClick,
    className,
    isToolBar = false,
    active = false,
    disabled = false,
}: Props) {
    const handleClick = (event: MouseEvent) => {
        event.preventDefault()
        if (onClick) { onClick(event, id) }
    }

    return (
        <li className={classNames('nav-item', 'panel-block-flex', className)}>
            <Button
                className={classNames(
                    'nav-link panel-block-flex panel-heading-link',
                    className,
                    { active },
                )}
                color={isToolBar ? 'primary' : 'link'}
                // @ts-ignore не сходятся типы с reactstrap, проблема в event: MouseEvent
                onClick={handleClick}
                disabled={disabled}
                size="sm"
            >
                {children}
            </Button>
        </li>
    )
}

export default PanelNavItem

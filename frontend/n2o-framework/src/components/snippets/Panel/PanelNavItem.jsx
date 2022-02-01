import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import { Button } from 'reactstrap'

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
    id,
    onClick,
    active,
    disabled,
    className,
    isToolBar,
    children,
}) {
    const handleClick = (e) => {
        e.preventDefault()
        if (onClick) {
            onClick(e, id)
        }
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
                onClick={handleClick}
                disabled={disabled}
                size="sm"
            >
                {children}
            </Button>
        </li>
    )
}

PanelNavItem.propTypes = {
    id: PropTypes.string,
    active: PropTypes.bool,
    disabled: PropTypes.bool,
    onClick: PropTypes.func,
    className: PropTypes.string,
    isToolBar: PropTypes.bool,
    children: PropTypes.node,
}

PanelNavItem.defaultProps = {
    active: false,
    disabled: false,
    isToolBar: false,
}

export default PanelNavItem

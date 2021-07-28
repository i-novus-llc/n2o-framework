import React from 'react'
import classNames from 'classnames'

import { SimpleSidebar } from '../../../plugins/SideBar/SidebarContainer'

/**
 * Обёртка над SimpleSidebar, отвечающая за скрытие/раскрытие по ховеру и перекрытием контента при overlay
 * TODO выглядит сложновато надо зарефачить
 */
export const Sidebar = ({ ...props }) => {
    const {
        controlled,
        overlay,
        toggleOnHover,
        openSideBar,
        closeSideBar,
        fullSizeHeader,
        sidebarOpen,
        className,
        fixed,
        defaultState,
        side,
    } = props

    const sidebarProps = {
        ...props,
        controlled,
        onMouseEnter: !overlay && toggleOnHover && openSideBar,
        onMouseLeave: !overlay && toggleOnHover && closeSideBar,
        className: classNames(
            className,
            {
                'n2o-fixed-sidebar': fixed && !fullSizeHeader,
            },
        ),
        sidebarOpen,
    }

    if (overlay) {
        return (
            <div
                onMouseEnter={openSideBar}
                onMouseLeave={closeSideBar}
                className={classNames(
                    'n2o-sidebar',
                    {
                        [defaultState]: !sidebarOpen && defaultState === 'none',
                        'n2o-sidebar-overlay': overlay,
                        'overlay-right': overlay && side === 'right',
                    },
                )}
            >
                <SimpleSidebar {...sidebarProps} />
            </div>
        )
    }

    return (
        <SimpleSidebar {...sidebarProps} />
    )
}

Sidebar.propTypes = SimpleSidebar.propTypes

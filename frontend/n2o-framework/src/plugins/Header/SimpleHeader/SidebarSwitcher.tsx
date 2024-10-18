import React from 'react'
import classNames from 'classnames'

interface Props {
    defaultIcon: string
    toggledIcon: string
    sidebarOpen: boolean
    toggleSidebar(): void
}

export function SidebarSwitcher({
    defaultIcon,
    toggledIcon,
    sidebarOpen,
    toggleSidebar,
}: Props) {
    return (
        <i
            className={classNames('n2o-sidebar-switcher', sidebarOpen ? toggledIcon : defaultIcon)}
            aria-hidden="true"
            onClick={toggleSidebar}
        />
    )
}

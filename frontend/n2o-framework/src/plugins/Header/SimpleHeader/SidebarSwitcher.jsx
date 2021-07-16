import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

export function SidebarSwitcher({
    defaultIcon,
    toggledIcon,
    sidebarOpen,
    toggleSidebar,
}) {
    return (
        <i
            className={classNames('n2o-sidebar-switcher', sidebarOpen ? toggledIcon : defaultIcon)}
            aria-hidden="true"
            onClick={toggleSidebar}
        />
    )
}
SidebarSwitcher.propTypes = {
    defaultIcon: PropTypes.string,
    toggledIcon: PropTypes.string,
    sidebarOpen: PropTypes.bool,
    toggleSidebar: PropTypes.func,
}

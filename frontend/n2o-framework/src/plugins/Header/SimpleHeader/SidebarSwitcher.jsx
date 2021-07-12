import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

export function SidebarSwitcher({
    defaultIcon = 'fa fa-bars',
    toggledIcon = 'fa fa-times',
    sidebarOpen,
    toggleSidebar,
}) {
    return (
        <i
            className={classNames('n2o-sidebar-switcher', {
                [defaultIcon]: sidebarOpen,
                [toggledIcon]: !sidebarOpen,
            })}
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

SidebarSwitcher.defaultProps = {
    defaultIcon: 'fa fa-times',
    toggledIcon: 'fa fa-bars',
}

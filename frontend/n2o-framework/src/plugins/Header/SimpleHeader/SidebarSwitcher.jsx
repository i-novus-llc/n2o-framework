import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

export function SidebarSwitcher({
    defaultIcon,
    toggleIcon,
    sidebarOpen,
    toggleSidebar,
}) {
    return (
        <i
            className={classNames('n2o-sidebar-switcher', {
                [defaultIcon]: sidebarOpen,
                [toggleIcon]: !sidebarOpen,
            })}
            aria-hidden="true"
            onClick={toggleSidebar}
        />
    )
}
SidebarSwitcher.propTypes = {
    defaultIcon: PropTypes.string,
    toggleIcon: PropTypes.string,
    sidebarOpen: PropTypes.bool,
    toggleSidebar: PropTypes.func,
}

SidebarSwitcher.defaultProps = {
    defaultIcon: 'fa fa-times',
    toggleIcon: 'fa fa-bars',
}

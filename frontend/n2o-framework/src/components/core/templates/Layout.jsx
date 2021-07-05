import React, { useCallback, useState } from 'react'
import isEmpty from 'lodash/isEmpty'
import classNames from 'classnames'
import PropTypes from 'prop-types'

import { SimpleSidebar } from '../../../plugins/SideBar/SidebarContainer'

import { layoutContainerClasses } from './utils'
import { FullSizeSidebar } from './FullSizeSidebar'
import { FullSizeHeader } from './FullSizeHeader'

export function Layout({ children, layout, header, sidebar, footer, ...rest }) {
    const [sidebarOpen, setSidebarOpen] = useState(false)

    const { fullSizeHeader, fixed } = layout
    const controlled = header.sidebarSwitcher

    const { toggleOnHover, side, overlay, defaultState, className } = sidebar
    const toggleSidebar = useCallback(() => setSidebarOpen(sidebarOpen => !sidebarOpen), [])
    const openSideBar = useCallback(() => setSidebarOpen(true), [])
    const closeSideBar = useCallback(() => setSidebarOpen(false), [])
    const layoutClassName = layoutContainerClasses(header, sidebar, fullSizeHeader, fixed, side)

    const sidebarProps = {
        controlled,
        sidebarOpen,
        onMouseEnter: toggleOnHover && openSideBar,
        onMouseLeave: toggleOnHover && closeSideBar,
        ...sidebar,
        ...rest,
        className: classNames(
            className,
            {
                'n2o-fixed-sidebar': fixed && !fullSizeHeader,
                'n2o-sidebar-overlay': overlay,
            },
        ),
    }

    const layoutProps = {
        layoutClassName,
        header,
        sidebar,
        side,
        toggleSidebar,
        sidebarOpen,
        layoutChildren: children,
        fixed,
        footer,
        ...rest,
    }

    const WrappedSidebar = useCallback(() => {
        if (isEmpty(sidebar)) {
            return <></>
        }

        if (overlay) {
            return (
                <div className={classNames(
                    'n2o-sidebar',
                    defaultState,
                )}
                >
                    <SimpleSidebar {...sidebarProps} />
                </div>
            )
        }

        return <SimpleSidebar {...sidebarProps} />
    }, [sidebar, overlay, sidebarProps, defaultState])

    return (
        fullSizeHeader ? (
            <FullSizeHeader {...layoutProps}>
                <WrappedSidebar />
            </FullSizeHeader>
        ) : (
            <FullSizeSidebar {...layoutProps}>
                <WrappedSidebar />
            </FullSizeSidebar>
        )
    )
}

Layout.defaultProps = {
    header: {},
    sidebar: {},
    footer: {},
}

Layout.propTypes = {
    children: PropTypes.oneOfType([
        PropTypes.arrayOf(PropTypes.node),
        PropTypes.node,
    ]),
    layout: PropTypes.shape({
        fullSizeHeader: PropTypes.bool,
        fixed: PropTypes.bool,
    }).isRequired,
    header: PropTypes.object,
    sidebar: PropTypes.object,
    footer: PropTypes.object,
}

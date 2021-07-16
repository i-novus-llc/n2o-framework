import React, { useCallback, useState } from 'react'
import isEmpty from 'lodash/isEmpty'
import classNames from 'classnames'
import PropTypes from 'prop-types'

import { SimpleSidebar } from '../../../plugins/SideBar/SidebarContainer'

import { layoutContainerClasses } from './utils'
import { FullSizeSidebar } from './FullSizeSidebar'
import { FullSizeHeader } from './FullSizeHeader'

const WrappedSidebar = ({ ...props }) => {
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

WrappedSidebar.propTypes = SimpleSidebar.propTypes

export function Layout({ children, layout, header, sidebar, footer, ...rest }) {
    const [sidebarOpen, setSidebarOpen] = useState(false)

    const { fullSizeHeader, fixed } = layout
    const controlled = header.sidebarSwitcher || sidebar.toggleOnHover

    const { side } = sidebar
    const toggleSidebar = useCallback(() => setSidebarOpen(sidebarOpen => !sidebarOpen), [])
    const openSideBar = useCallback(() => setSidebarOpen(true), [])
    const closeSideBar = useCallback(() => setSidebarOpen(false), [])
    const layoutClassName = layoutContainerClasses(header, sidebar, fullSizeHeader, fixed, side)

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

    const sidebarComponent = !isEmpty(sidebar) && (
        <WrappedSidebar
            {...sidebar}
            {...rest}
            controlled={controlled}
            openSideBar={openSideBar}
            closeSideBar={closeSideBar}
            fullSizeHeader={fullSizeHeader}
            sidebarOpen={sidebarOpen}
        />
    )

    return (
        <div className={layoutClassName}>
            {
                fullSizeHeader ? (
                    <FullSizeHeader {...layoutProps}>
                        { sidebarComponent }
                    </FullSizeHeader>
                ) : (
                    <FullSizeSidebar {...layoutProps}>
                        { sidebarComponent }
                    </FullSizeSidebar>
                )
            }
        </div>
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

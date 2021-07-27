import React, { useCallback, useState } from 'react'
import isEmpty from 'lodash/isEmpty'
import classNames from 'classnames'
import PropTypes from 'prop-types'

import Footer from '../../../plugins/Footer/Footer'
import SimpleHeader from '../../../plugins/Header/SimpleHeader/SimpleHeader'

import { layoutContainerClasses } from './utils'
import { Layout as FullSizeSidebar } from './layout/FullSizeSidebar'
import { Layout as FullSizeHeader } from './layout/FullSizeHeader'
import { Sidebar } from './Sidebar'

export function Page({
    children: content,
    layout: layoutProps,
    header: headerProps,
    sidebar: sidebarProps,
    footer: footerProps,
    ...rest
}) {
    const [sidebarOpened, setSidebarOpened] = useState(false)

    const { fullSizeHeader, fixed } = layoutProps
    const controlled = headerProps.sidebarSwitcher || sidebarProps.toggleOnHover

    const { side } = sidebarProps
    const toggleSidebar = useCallback(() => setSidebarOpened(sidebarOpen => !sidebarOpen), [])
    const openSideBar = useCallback(() => setSidebarOpened(true), [])
    const closeSideBar = useCallback(() => setSidebarOpened(false), [])
    const layoutClassName = layoutContainerClasses(headerProps, sidebarProps, fullSizeHeader, fixed, side)

    const sidebar = !isEmpty(sidebarProps) && (
        <Sidebar
            {...sidebarProps}
            controlled={controlled}
            openSideBar={openSideBar}
            closeSideBar={closeSideBar}
            fullSizeHeader={fullSizeHeader}
            sidebarOpen={sidebarOpened}
        />
    )
    const footer = !isEmpty(footerProps) && (<Footer {...footerProps} />)
    const header = !isEmpty(headerProps) && (
        <SimpleHeader
            toggleSidebar={toggleSidebar}
            sidebarOpen={sidebarOpened}
            {...headerProps}
            className={classNames('flex-grow-0', { [headerProps.className]: headerProps.className })}
            color={headerProps.className}
        />
    )

    const Layout = fullSizeHeader ? FullSizeHeader : FullSizeSidebar

    return (
        <div className={layoutClassName}>
            <Layout
                header={header}
                sidebar={sidebar}
                footer={footer}
                fixed={fixed}
                side={side}
            >
                {content}
            </Layout>
        </div>
    )
}

Page.defaultProps = {
    header: {},
    sidebar: {},
    footer: {},
}

Page.propTypes = {
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

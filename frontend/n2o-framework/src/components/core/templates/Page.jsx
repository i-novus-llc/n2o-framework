import React, { useCallback, useState } from 'react'
import isEmpty from 'lodash/isEmpty'
import classNames from 'classnames'

import { Factory } from '../../../core/factory/Factory'
import { TEMPLATES } from '../../../core/factory/factoryLevels'

import { Layout as FullSizeSidebar } from './layout/FullSizeSidebar'
import { Layout as FullSizeHeader } from './layout/FullSizeHeader'

const layoutContainerClasses = (header, sidebar, fullSizeHeader, fixed, side) => classNames(
    'n2o-layout-container flex-grow-1',
    { 'n2o-layout-with-header': !isEmpty(header),
        'n2o-layout-with-sidebar': !isEmpty(sidebar),
        'n2o-layout-full-size-header d-flex flex-column': fullSizeHeader,
        'n2o-layout-full-size-sidebar d-flex': !fullSizeHeader,
        'n2o-layout-fixed': fixed,
        'flex-row-reverse': !fullSizeHeader && side === 'right' },
)

export function Page({
    children,
    layout: layoutProps,
    header: headerProps = {},
    sidebar: sidebarProps = {},
    footer: footerProps = {},
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
        <Factory
            level={TEMPLATES}
            {...sidebarProps}
            controlled={controlled}
            openSideBar={openSideBar}
            closeSideBar={closeSideBar}
            fullSizeHeader={fullSizeHeader}
            sidebarOpen={sidebarOpened}
        />
    )
    const footer = !isEmpty(footerProps) && (<Factory {...footerProps} />)

    const header = !isEmpty(headerProps) && (
        <Factory
            toggleSidebar={toggleSidebar}
            sidebarOpen={sidebarOpened}
            {...headerProps}
            className={classNames('flex-grow-0', { [headerProps.className]: headerProps.className })}
            color={headerProps.className}
        />
    )

    const Layout = fullSizeHeader ? FullSizeHeader : FullSizeSidebar

    return (
        <Layout
            className={layoutClassName}
            header={header}
            sidebar={sidebar}
            footer={footer}
            fixed={fixed}
            side={side}
        >
            {children}
        </Layout>
    )
}

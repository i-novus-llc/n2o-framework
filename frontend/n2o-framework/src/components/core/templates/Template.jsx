import React, { useState, useCallback } from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

import SimpleHeader from '../../../plugins/Header/SimpleHeader/SimpleHeader'
import Sidebar from '../../../plugins/SideBar/SidebarContainer'
import Footer from '../../../plugins/Footer/Footer'
// eslint-disable-next-line import/no-named-as-default
import MenuContainer from '../../../plugins/Menu/MenuContainer'

function Layout({ children, layout, header, sidebar, footer, ...rest }) {
    const [sidebarOpen, setSidebarOpen] = useState(true)

    const { fullSizeHeader, fixed } = layout
    const controlled = header.sidebarSwitcher

    const toggleSidebar = useCallback(() => setSidebarOpen(sidebarOpen => !sidebarOpen), [])

    const layoutContainerClasses = classNames(
        'n2o-layout-container',
        { 'n2o-layout-with-header': header,
            'n2o-layout-with-sidebar': sidebar,
            'n2o-layout-full-size-header d-flex flex-column': fullSizeHeader,
            'n2o-layout-full-size-sidebar d-flex': !fullSizeHeader,
            'n2o-layout-fixed': fixed,
        },
    )

    if (fullSizeHeader) {
        return (
            <div className={layoutContainerClasses}>
                {header && (
                    <SimpleHeader
                        toggleSidebar={toggleSidebar}
                        sidebarOpen={sidebarOpen}
                        {...header}
                        {...rest}
                    />
                )}
                <div className="w-100 d-flex flex-row overflow-auto">
                    {sidebar && (
                        <Sidebar
                            controlled={controlled}
                            sidebarOpen={sidebarOpen}
                            {...sidebar}
                            {...rest}
                        />
                    )}
                    <div className={classNames('w-100', { 'application-body-container-fixed': fixed })}>
                        <div className="application-body container-fluid">{children}</div>
                        {footer && <Footer {...footer} />}
                    </div>
                </div>
            </div>
        )
    }

    return (
        <div className={layoutContainerClasses}>
            {sidebar && (
                <Sidebar
                    controlled={controlled}
                    sidebarOpen={sidebarOpen}
                    className={classNames({ 'n2o-fixed-sidebar': fixed })}
                    {...sidebar}
                    {...rest}
                />
            )}
            <div className={classNames('w-100 d-flex flex-column', { 'vh-100': fixed })}>
                {header && (
                    <SimpleHeader
                        toggleSidebar={toggleSidebar}
                        sidebarOpen={sidebarOpen}
                        {...header}
                        {...rest}
                    />
                )}
                <div className={classNames({ 'd-flex': sidebar, 'application-body-container-fixed': fixed })}>
                    <div className="application-body container-fluid">{children}</div>
                </div>
                {footer && <Footer {...footer} />}
            </div>
        </div>
    )
}

function Template({ children }) {
    return (
        <div className="application">
            <MenuContainer render={config => <Layout {...config}>{children}</Layout>} />
        </div>
    )
}

Template.propTypes = {
    children: PropTypes.oneOfType([
        PropTypes.arrayOf(PropTypes.node),
        PropTypes.node,
    ]),
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

export default Template

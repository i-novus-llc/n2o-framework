import React from 'react'
import classNames from 'classnames'
import isEmpty from 'lodash/isEmpty'

import Footer from '../../../plugins/Footer/Footer'
import SimpleHeader from '../../../plugins/Header/SimpleHeader/SimpleHeader'

import { layoutTypes } from './utils'

export function FullSizeSidebar({
    layoutClassName,
    sidebar,
    fixed,
    header,
    sidebarOpen,
    toggleSidebar,
    layoutChildren,
    children,
    footer,
    ...rest }) {
    return (
        <div className={layoutClassName}>
            {children}
            <div className={classNames('w-100 d-flex flex-column', { 'vh-100': fixed })}>
                {
                    !isEmpty(header) && (
                        <SimpleHeader
                            toggleSidebar={toggleSidebar}
                            sidebarOpen={sidebarOpen}
                            {...header}
                            {...rest}
                            className={classNames('flex-grow-0', { [header.className]: header.className })}
                        />
                    )
                }
                <div className={classNames({ 'd-flex w-100 flex-grow-1': sidebar, 'application-body-container-fixed': fixed })}>
                    <div className="application-body container-fluid">{layoutChildren}</div>
                </div>
                {!isEmpty(footer) && <Footer {...footer} />}
            </div>
        </div>
    )
}

FullSizeSidebar.propTypes = {
    ...layoutTypes,
}

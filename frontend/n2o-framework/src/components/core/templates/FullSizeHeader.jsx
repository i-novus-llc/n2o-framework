import React from 'react'
import isEmpty from 'lodash/isEmpty'
import classNames from 'classnames'

import SimpleHeader from '../../../plugins/Header/SimpleHeader/SimpleHeader'
import Footer from '../../../plugins/Footer/Footer'

import { layoutTypes } from './utils'

export function FullSizeHeader({
    layoutClassName,
    header,
    toggleSidebar,
    sidebarOpen,
    side,
    children,
    fixed,
    layoutChildren,
    footer,
    ...rest }) {
    return (
        <div className={layoutClassName}>
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
            <div className={classNames(
                'w-100 d-flex overflow-auto flex-grow-1',
                {
                    'flex-row': side === 'left',
                    'flex-row-reverse': side === 'right',
                },
            )}
            >
                {children}
                <div className={classNames('w-100 d-flex flex-column', { 'application-body-container-fixed': fixed })}>
                    <div className="application-body container-fluid">{layoutChildren}</div>
                </div>
            </div>
            {!isEmpty(footer) && <Footer {...footer} />}
        </div>
    )
}

FullSizeHeader.propTypes = {
    ...layoutTypes,
}
